package io.silv.hsrdmgcalc.data.coil

import android.content.Context
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.SourceResult
import coil.key.Keyer
import coil.request.Options
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.StorageItem
import io.silv.coil_disk_fetcher.DiskBackedFetcher
import okio.Path.Companion.toOkioPath


class StorageItemFetcher(
    override val context: Context,
    override val diskStore: StorageItemCache,
    private val storage: Storage
): DiskBackedFetcher<StorageItem> {

    override val keyer: Keyer<StorageItem> = StorageItemKeyer

    override suspend fun overrideFetch(options: Options, data: StorageItem): FetchResult? {
        // Use custom cover if exists
        val useCustomCover = options.parameters.value("USE_CUSTOM_IMAGE") ?: false
        if (useCustomCover) {
            val customCoverFile = diskStore.getCustomImageFile(data.path, data.bucketId)
            if (customCoverFile.exists()) {
                return SourceResult(
                    source = ImageSource(
                        file = customCoverFile.toOkioPath(),
                        diskCacheKey = data.path
                    ),
                    mimeType = "image/*",
                    dataSource = DataSource.DISK,
                )
            }
        }
        return null
    }
    override suspend fun fetch(options: Options, data: StorageItem): ByteArray {

        val bucket = storage[data.bucketId]

        return if(data.authenticated) {
            bucket.downloadAuthenticated(data.path)
        } else {
            bucket.downloadPublic(data.path)
        }
    }
}