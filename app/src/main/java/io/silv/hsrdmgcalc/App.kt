package io.silv.hsrdmgcalc

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.core.content.getSystemService
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import io.silv.hsrdmgcalc.data.coil.StorageItemFetcher
import io.silv.hsrdmgcalc.data.coil.StorageItemKeyer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

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
            val memCacheInit = { CoilMemoryCache.get(this@App) }

            components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(
                    StorageItemFetcher.Factory(
                        lazy(diskCacheInit),
                        lazy(memCacheInit),
                        this@App
                    )
                )
                add(StorageItemKeyer)
            }
            memoryCache(memCacheInit)
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

    companion object {

        object CoilMemoryCache {

            private var instance: MemoryCache? = null

            @Synchronized
            fun get(context: Context): MemoryCache {
                return instance ?: run {
                    // Create the singleton mem cache instance.
                    MemoryCache.Builder(context).build()
                }
            }
        }
        /**
         * Direct copy of Coil's internal SingletonDiskCache so that [StorageItemFetcher] can access it.
         */
        object CoilDiskCache {

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
    }
}



