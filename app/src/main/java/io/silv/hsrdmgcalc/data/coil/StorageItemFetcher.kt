package io.silv.hsrdmgcalc.data.coil

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.disk.DiskCache
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.memory.MemoryCache
import coil.request.Options
import coil.size.isOriginal
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.StorageItem
import okio.Path.Companion.toOkioPath
import okio.Source
import okio.buffer
import okio.sink
import okio.source
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.ByteArrayInputStream
import java.io.File


/**
 * Class used to fetch Storage items using coil and store the images
 * using the files provided. First tries to load the [customImageFileLazy] & [imageFileLazy]
 * then from [DiskCache].
 * If no image is found fetches from Supabase bucket api and writes to local storage if possible.
 *
 * @param item Storage item to load.
 * @param options [Options]
 * @param storageLazy lazy loaded Supabase Storage instance used to get bucket api.
 * @param diskCacheKeyLazy key used to find the storage item from [diskCacheLazy].
 * @param diskCacheLazy disk cached for coil should be [io.silv.hsrdmgcalc.App.CoilDiskCache].
 * @param customImageFileLazy user specified custom image file for storage item.
 * @param imageFileLazy default location to store the image after it is loaded from Supabase.
 */
class StorageItemFetcher(
    private val item: StorageItem,
    private val options: Options,
    private val storageLazy: Lazy<Storage>,
    private val diskCacheKeyLazy: Lazy<String>,
    private val diskCacheLazy: Lazy<DiskCache>,
    private val memCacheLazy: Lazy<MemoryCache>,
    private val memCacheKeyLazy: Lazy<MemoryCache.Key>,
    private val customImageFileLazy: Lazy<File>,
    private val imageFileLazy: Lazy<File?>,
    private val context: Context,
) : Fetcher {

    private val diskCacheKey: String
        get() = diskCacheKeyLazy.value

    private val memCacheKey: MemoryCache.Key
        get() = memCacheKeyLazy.value

    private val storage: Storage
        get() = storageLazy.value

    private val memCache: MemoryCache
        get() = memCacheLazy.value

    override suspend fun fetch(): FetchResult {

        if (options.memoryCachePolicy.readEnabled) {
            memCache[memCacheKey]?.let {
                return DrawableResult(
                    drawable = it.bitmap.toDrawable(context.resources),
                    isSampled = options.size.isOriginal,
                    dataSource = DataSource.MEMORY_CACHE
                )
           }
        }

        // Use custom cover if exists
        val useCustomCover = options.parameters.value(USE_CUSTOM_IMAGE) ?: false
        if (useCustomCover) {
            val customCoverFile = customImageFileLazy.value
            if (customCoverFile.exists()) {
                return fileLoader(customCoverFile)
            }
        }

        val imageCacheFile = imageFileLazy.value

        // Check if the file path already has an existing file meaning the image exists
        if (imageCacheFile?.exists() == true && options.diskCachePolicy.readEnabled) {
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
            } finally {
                if (options.memoryCachePolicy.writeEnabled) {
                    val bmp = with(
                        BitmapFactory.Options().apply { inMutable = true }
                    ){
                        BitmapFactory.decodeByteArray(
                            response, 0,
                            response.size,
                            this
                        )
                    }
                    memCache[memCacheKey] = MemoryCache.Value(bitmap = bmp)
                }
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
        ).also {
            if (options.memoryCachePolicy.writeEnabled) {
                val options = BitmapFactory.Options()
                options.inMutable = true
                val data = file.readBytes()
                val bmp = BitmapFactory.decodeByteArray(
                    data,
                    0,
                    data.size,
                    options
                )

                memCache[memCacheKey] = MemoryCache.Value(bitmap = bmp)
            }
        }
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

    class Factory(
        private val diskCacheLazy: Lazy<DiskCache>,
        private val memCacheLazy: Lazy<MemoryCache>,
        private val context: Context,
    ) : Fetcher.Factory<StorageItem>, KoinComponent {

        private val imageCache by inject<StorageItemCache>()
        private val storage= inject<Storage>()

        override fun create(
            data: StorageItem,
            options: Options,
            imageLoader: ImageLoader,
        ): Fetcher {

            return StorageItemFetcher(
                storageLazy = storage,
                item = data,
                options = options,
                diskCacheKeyLazy = lazy { StorageItemKeyer.key(data, options) },
                customImageFileLazy = lazy { imageCache.getCustomImageFile(data.path, data.bucketId) },
                imageFileLazy = lazy { imageCache.getImageFile(data.path, data.bucketId) },
                diskCacheLazy = diskCacheLazy,
                memCacheKeyLazy = lazy { MemoryCache.Key(StorageItemKeyer.key(data, options)) },
                memCacheLazy = memCacheLazy,
                context = context
            )
        }
    }

    companion object {
        const val USE_CUSTOM_IMAGE = "use_custom_image"
    }
}
