package io.silv.hsrdmgcalc

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.getSystemService
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.ImageSource
import coil.disk.DiskCache
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.key.Keyer
import coil.request.Options
import coil.util.DebugLogger
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.StorageItem
import io.silv.data.character.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okio.Path.Companion.toOkioPath
import okio.Source
import okio.buffer
import okio.sink
import okio.source
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.io.ByteArrayInputStream
import java.io.File

class App: Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()
            workManagerFactory()
            modules(appModule)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this).apply {

            val diskCacheInit = { CoilDiskCache.get(this@App) }

            components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(StorageItemFectcher.CharacterFetcher(lazy(diskCacheInit)))
                add(StorageItemKeyer)
            }
            diskCache(diskCacheInit)
            crossfade(
                (
                        300 *
                                Settings.Global.getFloat(
                                    this@App.contentResolver,
                                    Settings.Global.ANIMATOR_DURATION_SCALE,
                                    1f,
                                )
                        ).toInt(),
            )
            allowRgb565(isLowRamDevice(this@App))
            // Coil spawns a new thread for every image load by default
            fetcherDispatcher(Dispatchers.IO.limitedParallelism(8))
            decoderDispatcher(Dispatchers.IO.limitedParallelism(2))
            transformationDispatcher(Dispatchers.IO.limitedParallelism(2))
            logger(DebugLogger())
        }
            .build()
    }

    private fun isLowRamDevice(context: Context): Boolean {
        val memInfo = ActivityManager.MemoryInfo()
        context.getSystemService<ActivityManager>()!!.getMemoryInfo(memInfo)
        val totalMemBytes = memInfo.totalMem
        return totalMemBytes < 3L * 1024 * 1024 * 1024
    }
}

/**
 * Direct copy of Coil's internal SingletonDiskCache so that [MangaCoverFetcher] can access it.
 */
private object CoilDiskCache {

    private const val FOLDER_NAME = "image_cache"
    private var instance: DiskCache? = null

    @Synchronized
    fun get(context: Context): DiskCache {
        return instance ?: run {
            val safeCacheDir = context.cacheDir.apply { mkdirs() }
            // Create the singleton disk cache instance.
            DiskCache.Builder()
                .directory(safeCacheDir.resolve(FOLDER_NAME))
                .build()
                .also { instance = it }
        }
    }
}



class StorageItemFectcher(
    private val item: StorageItem,
    private val options: Options,
    private val storageLazy: Lazy<Storage>,
    private val diskCacheKeyLazy: Lazy<String>,
    private val customImageFileLazy: Lazy<File>,
    private val imageFileLazy: Lazy<File?>,
    private val diskCacheLazy: Lazy<DiskCache>,
) : Fetcher {

    private val diskCacheKey: String
        get() = diskCacheKeyLazy.value

    private val storage: Storage
        get() = storageLazy.value

    override suspend fun fetch(): FetchResult {

        // Use custom cover if exists
        val useCustomCover = options.parameters.value(USE_CUSTOM_COVER) ?: false
        if (useCustomCover) {
            val customCoverFile = customImageFileLazy.value
            if (customCoverFile.exists()) {
                return fileLoader(customCoverFile)
            }
        }
        // Check if the file returned points to a valid file path
        val imageCacheFile = imageFileLazy.value ?: error("No cover specified")

        // Check if the file path already has an existing file meaning the image exists
        if (imageCacheFile.exists() && options.diskCachePolicy.readEnabled) {
            return fileLoader(imageCacheFile)
        }

        var snapshot = readFromDiskCache()

        try {
            // Fetch from disk cache
            if (snapshot != null) {
                val snapshotCoverCache = moveSnapshotToCoverCache(snapshot, imageCacheFile)
                if (snapshotCoverCache != null) {
                    // Read from cover cache after added to library
                    return fileLoader(snapshotCoverCache)
                }

                // Read from snapshot
                return SourceResult(
                    source = snapshot.toImageSource(),
                    mimeType = "image/*",
                    dataSource = DataSource.DISK,
                )
            }
            // Fetch from network
            val bucket = storage[item.bucketId]
            val response = bucket.downloadPublic(item.path)

            try {
                // Read from cover cache after library manga cover updated
                val responseCoverCache = writeResponseToCoverCache(
                    ByteArrayInputStream(response),
                    imageCacheFile
                )
                if (responseCoverCache != null) {
                    return fileLoader(responseCoverCache)
                }

                // Read from disk cache
                snapshot = writeToDiskCache(response)
                if (snapshot != null) {
                    return SourceResult(
                        source = snapshot.toImageSource(),
                        mimeType = "image/*",
                        dataSource = DataSource.NETWORK,
                    )
                }

                // Read from response if cache is unused or unusable
                return SourceResult(
                    source = ImageSource(
                        source = ByteArrayInputStream(response).source().buffer(),
                        context = options.context
                    ),
                    mimeType = "image/*",
                    dataSource = DataSource.NETWORK,
                )
            } catch (e: Exception) {
                throw e
            }
        } catch (e: Exception){
            snapshot?.close()
            throw e
        }
    }

    private fun fileLoader(file: File): FetchResult {
        return SourceResult(
            source = ImageSource(
                file = file.toOkioPath(),
                diskCacheKey = diskCacheKey
            ),
            mimeType = "image/*",
            dataSource = DataSource.DISK,
        )
    }


    @OptIn(ExperimentalCoilApi::class)
    private fun moveSnapshotToCoverCache(
        snapshot: DiskCache.Snapshot,
        cacheFile: File?,
    ): File? {
        if (cacheFile == null) return null
        return try {
            diskCacheLazy.value.run {
                fileSystem.source(snapshot.data).use { input ->
                    writeSourceToCoverCache(input, cacheFile)
                }
                remove(diskCacheKey)
            }
            cacheFile.takeIf { it.exists() }
        } catch (e: Exception) {
            Log.e(
                "moveSnapshotToCoverCache",
                "Failed to write snapshot data to cover cache ${cacheFile.name}"
            )
            null
        }
    }

    private fun writeResponseToCoverCache(
        response: ByteArrayInputStream,
        cacheFile: File?,
    ): File? {
        if (cacheFile == null || !options.diskCachePolicy.writeEnabled) return null
        return try {
            response.source().use { input ->
                writeSourceToCoverCache(input, cacheFile)
            }
            cacheFile.takeIf { it.exists() }
        } catch (e: Exception) {
            Log.e(
                "writeResponseToCoverCache",
                "Failed to write response data to cover cache ${cacheFile.name}"
            )
            null
        }
    }

    private fun writeSourceToCoverCache(
        input: Source,
        cacheFile: File,
    ) {
        cacheFile.parentFile?.mkdirs()
        cacheFile.delete()
        try {
            cacheFile.sink().buffer().use { output ->
                output.writeAll(input)
            }
        } catch (e: Exception) {
            cacheFile.delete()
            throw e
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun readFromDiskCache(): DiskCache.Snapshot? {
        return if (options.diskCachePolicy.readEnabled) {
            diskCacheLazy.value.openSnapshot(diskCacheKey)
        } else {
            null
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun writeToDiskCache(
        response: ByteArray
    ): DiskCache.Snapshot? {
        val editor = diskCacheLazy.value.openEditor(diskCacheKey) ?: return null
        try {
            diskCacheLazy.value.fileSystem.write(editor.data) {
                write(response)
            }
            return editor.commitAndOpenSnapshot()
        } catch (e: Exception) {
            runCatching { editor.abort() }
            throw e
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun DiskCache.Snapshot.toImageSource(): ImageSource {
        return ImageSource(file = data, diskCacheKey = diskCacheKey, closeable = this)
    }

    class CharacterFetcher(
        private val diskCacheLazy: Lazy<DiskCache>,
    ) : Fetcher.Factory<Character>, KoinComponent {

        private val imageCache by inject<StorageItemCache>()
        private val storage= inject<Storage>()

        override fun create(
            data: Character,
            options: Options,
            imageLoader: ImageLoader,
        ): Fetcher {

            val item = StorageItem(
                path = "character_${data.name}.webp",
                bucketId = "images",
                authenticated = false
            )

            return StorageItemFectcher(
                storageLazy = storage,
                item = item,
                options = options,
                diskCacheKeyLazy = lazy { StorageItemKeyer.key(item, options) },
                customImageFileLazy = lazy { imageCache.getCustomItemFile(item) },
                imageFileLazy = lazy { imageCache.getItemFile(item) },
                diskCacheLazy = diskCacheLazy
            )
        }
    }

    companion object {
        const val USE_CUSTOM_COVER = "use_custom_cover"
    }
}

object StorageItemKeyer : Keyer<StorageItem> {

    override fun key(
        data: StorageItem,
        options: Options,
    ): String {
        return data.path
    }
}