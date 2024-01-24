package io.silv.hsrdmgcalc

import android.content.Context
import io.github.jan.supabase.storage.StorageItem
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Class used to create cover cache.
 * It is used to store the covers of the library.
 * Names of files are created with the md5 of the thumbnail URL.
 *
 * @param context the application context.
 * @constructor creates an instance of the cover cache.
 */
class StorageItemCache(
    private val context: Context
) {

    companion object {
        private const val COVERS_DIR = "storage"
        private const val CUSTOM_COVERS_DIR = "storage/custom"
    }

    /**
     * Cache directory used for cache management.
     */
    private val cacheDir = getCacheDir(COVERS_DIR)

    private val customItemCacheDir = getCacheDir(CUSTOM_COVERS_DIR)

    /**
     * Returns the cover from cache.
     *
     * @param storageItem item to store.
     * @return item image.
     */
    fun getItemFile(storageItem: StorageItem): File {
        return File(cacheDir, DiskUtil.hashKeyForDisk(storageItem.path))
    }

    /**
     * Returns the custom cover from cache.
     *
     * @param storageItem
     * @return item image.
     */
    fun getCustomItemFile(storageItem: StorageItem): File {
        return File(customItemCacheDir, DiskUtil.hashKeyForDisk(storageItem.path))
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
        getCustomItemFile(item).outputStream().use {
            inputStream.copyTo(it)
        }
    }

    /**
     * Delete the cover files of the manga from the cache.
     *
     * @param manga the manga.
     * @param deleteCustomItem whether the custom cover should be deleted.
     * @return number of files that were deleted.
     */
    fun deleteFromCache(
        item: StorageItem,
        deleteCustomItem: Boolean = false,
    ): Int {
        var deleted = 0

        getItemFile(item).let {
            if (it.exists() && it.delete()) ++deleted
        }

        if (deleteCustomItem) {
            if (deleteCustomItem(item)) ++deleted
        }

        return deleted
    }

    /**
     * Delete custom cover of the manga from the cache
     *
     * @param mangaId the manga id.
     * @return whether the cover was deleted.
     */
    fun deleteCustomItem(item: StorageItem): Boolean {
        return getCustomItemFile(item).let {
            it.exists() && it.delete()
        }
    }

    private fun getCacheDir(dir: String): File {
        return context.getExternalFilesDir(dir)
            ?: File(context.filesDir, dir).also { it.mkdirs() }
    }
}