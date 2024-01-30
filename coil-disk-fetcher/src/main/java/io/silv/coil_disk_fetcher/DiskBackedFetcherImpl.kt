package io.silv.coil_disk_fetcher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
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
import coil.key.Keyer
import coil.memory.MemoryCache
import coil.request.Options
import coil.size.isOriginal
import io.silv.coil_disk_fetcher.DiskBackedFetcherImpl.Utils.toImageSource
import io.silv.coil_disk_fetcher.DiskBackedFetcherImpl.Utils.writeResponseToCoverCache
import io.silv.coil_disk_fetcher.DiskBackedFetcherImpl.Utils.writeToMemCache
import okio.Path.Companion.toOkioPath
import okio.Source
import okio.buffer
import okio.sink
import okio.source
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File


internal class DiskBackedFetcherImpl<T: Any>(
    val keyer: Keyer<T>,
    val diskStore: FetcherDiskStore<T>,
    val context: Context,
    val overrideCall: suspend (options: Options, data: T) -> FetchResult? = { _, _ -> null },
    val fetch: suspend (options: Options, data: T) -> ByteArray,
    val memoryCacheInit: () -> MemoryCache?,
    val diskCacheInit: () -> DiskCache?
): Fetcher.Factory<T>{

    internal val memCache by lazy { memoryCacheInit() ?: CoilMemoryCache.get(context) }
    internal val diskCache by lazy { diskCacheInit()  ?: CoilDiskCache.get(context) }

    @OptIn(ExperimentalCoilApi::class)
    override fun create(data: T, options: Options, imageLoader: ImageLoader): Fetcher {

        val diskCacheKey = keyer.key(data, options) ?: error("null disk cache key provided")
        val memCacheKey = MemoryCache.Key(keyer.key(data, options) ?: error("null mem cache key provided"))

        fun fileLoader(file: File): FetchResult {
            return SourceResult(
                source = ImageSource(
                    file = file.toOkioPath(),
                    diskCacheKey = diskCacheKey
                ),
                mimeType = "image/*",
                dataSource = DataSource.DISK,
            ).also {
                writeToMemCache(options, file.readBytes(), memCacheKey, memCache)
            }
        }

        return Fetcher {
            if (options.memoryCachePolicy.readEnabled) {
                memCache[memCacheKey]?.let {
                    return@Fetcher DrawableResult(
                        drawable = it.bitmap.toDrawable(context.resources),
                        isSampled = options.size.isOriginal,
                        dataSource = DataSource.MEMORY_CACHE
                    )
                }
            }

            overrideCall(options, data)?.let { fetchResult ->
                return@Fetcher fetchResult
                    .also {
                        writeToMemCache(
                            options,
                            bytes = when (fetchResult) {
                                is DrawableResult -> {
                                    val bitmap = (fetchResult.drawable as BitmapDrawable).bitmap
                                    val stream = ByteArrayOutputStream()
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                                    stream.toByteArray()
                                }
                                is SourceResult -> fetchResult.source.source().buffer.readByteArray()
                            },
                            memCacheKey,
                            memCache
                        )
                    }
            }

            val imageCacheFile = diskStore.getImageFile(data, options)

            // Check if the file path already has an existing file meaning the image exists
            if (imageCacheFile?.exists() == true && options.diskCachePolicy.readEnabled) {
                return@Fetcher fileLoader(imageCacheFile)
            }

            var snapshot = Utils.readFromDiskCache(options, diskCache, diskCacheKey)

            try {
                // Fetch from disk cache
                if (snapshot != null) {

                    val snapshotCoverCache = Utils.moveSnapshotToCoverCache(
                        diskCache,
                        diskCacheKey,
                        snapshot,
                        imageCacheFile
                    )

                    if (snapshotCoverCache != null) {
                        // Read from cover cache after added to library
                        return@Fetcher fileLoader(snapshotCoverCache)
                    }

                    // Read from snapshot
                    return@Fetcher SourceResult(
                        source = snapshot.toImageSource(diskCacheKey),
                        mimeType = "image/*",
                        dataSource = DataSource.DISK,
                    )
                }
                // Fetch from network
                val response = fetch(options, data)

                try {
                    // Read from cover cache after library manga cover updated
                    val responseCoverCache = writeResponseToCoverCache(
                        ByteArrayInputStream(response),
                        imageCacheFile,
                        options
                    )
                    if (responseCoverCache != null) {
                        return@Fetcher fileLoader(responseCoverCache)
                    }

                    // Read from disk cache
                    snapshot = Utils.writeToDiskCache(response, diskCache, diskCacheKey)
                    if (snapshot != null) {
                        return@Fetcher SourceResult(
                            source = snapshot.toImageSource(diskCacheKey),
                            mimeType = "image/*",
                            dataSource = DataSource.NETWORK,
                        )
                    }

                    // Read from response if cache is unused or unusable
                    return@Fetcher SourceResult(
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
                    writeToMemCache(options, response, memCacheKey, memCache)
                }
            } catch (e: Exception) {
                snapshot?.close()
                throw e
            }
        }
    }

    private object Utils {

        fun writeToMemCache(
            options: Options,
            bytes: ByteArray,
            memCacheKey: MemoryCache.Key,
            memCache: MemoryCache
        ) {
            if (options.memoryCachePolicy.writeEnabled) {
                val bmp = with(
                    BitmapFactory.Options().apply { inMutable = true }
                ) {
                    BitmapFactory.decodeByteArray(
                        bytes, 0,
                        bytes.size,
                        this
                    )
                }
                memCache[memCacheKey] = MemoryCache.Value(bitmap = bmp)
            }
        }

        fun writeResponseToCoverCache(
            response: ByteArrayInputStream,
            cacheFile: File?,
            options: Options
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

        fun writeSourceToCoverCache(
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
        fun readFromDiskCache(
            options: Options,
            diskCache: DiskCache,
            diskCacheKey: String
        ): DiskCache.Snapshot? {
            return if (options.diskCachePolicy.readEnabled) {
                diskCache.openSnapshot(diskCacheKey)
            } else {
                null
            }
        }

        @OptIn(ExperimentalCoilApi::class)
        fun moveSnapshotToCoverCache(
            diskCache: DiskCache,
            diskCacheKey: String,
            snapshot: DiskCache.Snapshot,
            cacheFile: File?,
        ): File? {
            if (cacheFile == null) return null
            return try {
                diskCache.run {
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

        @OptIn(ExperimentalCoilApi::class)
        fun DiskCache.Snapshot.toImageSource(key: String): ImageSource {
            return ImageSource(
                file = data,
                diskCacheKey = key,
                closeable = this
            )
        }

        @OptIn(ExperimentalCoilApi::class)
        fun writeToDiskCache(
            response: ByteArray,
            diskCache: DiskCache,
            diskCacheKey: String
        ): DiskCache.Snapshot? {
            val editor = diskCache.openEditor(diskCacheKey) ?: return null
            try {
                diskCache.fileSystem.write(editor.data) {
                    write(response)
                }
                return editor.commitAndOpenSnapshot()
            } catch (e: Exception) {
                runCatching { editor.abort() }
                throw e
            }
        }
    }
}