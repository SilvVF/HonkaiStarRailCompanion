package io.silv.hsrdmgcalc.data.coil

import android.content.Context
import io.github.jan.supabase.storage.StorageItem
import io.silv.hsrdmgcalc.DiskUtil
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Class used to create storage item image cache.
 * It is used to store the images from Supabase buckets.
 * Names of files are created with the md5 of the storage items path.
 *
 * @param context the application context.
 * @constructor creates an instance of the cache.
 */
class StorageItemCache(
    private val context: Context
) {

    companion object {
        private const val IMAGE_DIR = "storage"
        private const val CUSTOM_IMAGE_DIR = "storage/custom"
    }

    /**
     * Cache directory used for cache management.
     */
    private val cacheDir = getCacheDir(IMAGE_DIR)

    private val customImageCacheDir = getCacheDir(CUSTOM_IMAGE_DIR)

    /**
     * Returns the image from cache.
     *
     * @param path path of the [StorageItem].
     * @param bucketId bucketId of the [StorageItem]
     * @return item image.
     */
    fun getImageFile(path: String, bucketId: String = "images"): File {
        return File(cacheDir, getKey(bucketId, path))
    }

    /**
     * Returns the custom cover from cache.
     *
     * @param path path of the [StorageItem].
     * @param bucketId bucketId of the [StorageItem]
     * @return item image.
     */
    fun getCustomImageFile(path: String, bucketId: String = "images"): File {
        return File(customImageCacheDir, getKey(bucketId, path))
    }

    /**
     * Saves the given stream as the manga's custom cover to cache.
     *
     * @param item Storage item.
     * @param inputStream the stream to copy.
     * @throws IOException if there's any error.
     */
    @Throws(IOException::class)
    fun setCustomItemToCache(
        item: StorageItem,
        inputStream: InputStream,
    ) {
        getCustomImageFile(item.path, item.bucketId).outputStream().use {
            inputStream.copyTo(it)
        }
    }

    /**
     * Delete the cover files of the item from the cache.
     *
     * @param path path of the [StorageItem].
     * @param bucketId bucketId of the [StorageItem].
     * @param deleteCustomImages whether the images cover should be deleted.
     * @return number of files that were deleted.
     */
    fun deleteFromCache(
        path: String,
        bucketId: String,
        deleteCustomImages: Boolean = false,
    ): Int {
        var deleted = 0

        getImageFile(path, bucketId).let {
            if (it.exists() && it.delete()) ++deleted
        }

        if (deleteCustomImages) {
            if (deleteCusomtImage(path, bucketId)) ++deleted
        }

        return deleted
    }

    /**
     * Delete custom cover of the manga from the cache
     *
     * @param path path of the [StorageItem].
     * @param bucketId bucketId of the [StorageItem]
     * @return whether the cover was deleted.
     */
    fun deleteCusomtImage(path: String, bucketId: String): Boolean {
        return getCustomImageFile(path, bucketId).let {
            it.exists() && it.delete()
        }
    }

    private fun getKey(bucketId: String, path: String): String {
        return DiskUtil.hashKeyForDisk("$bucketId$path")
    }

    private fun getCacheDir(dir: String): File {
        return context.getExternalFilesDir(dir)
            ?: File(context.filesDir, dir).also { it.mkdirs() }
    }
}