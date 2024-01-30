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
import coil.util.DebugLogger
import io.github.jan.supabase.storage.Storage
import io.silv.coil_disk_fetcher.addDiskFetcher
import io.silv.hsrdmgcalc.data.coil.StorageItemCache
import io.silv.hsrdmgcalc.data.coil.StorageItemFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.get
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
            components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            addDiskFetcher(
                StorageItemFetcher(
                    this@App,
                    get<StorageItemCache>(),
                    get<Storage>()
                )
            )
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



