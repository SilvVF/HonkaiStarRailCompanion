package io.silv.hsrdmgcalc

import android.app.Application
import io.silv.hsrdmgcalc.data.PrepopulateWorker
import io.silv.hsrdmgcalc.data.dataModule
import io.silv.hsrdmgcalc.preferences.preferencesModule
import io.silv.hsrdmgcalc.ui.screens.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()
            workManagerFactory()
            modules(appModule, viewModelModule, dataModule, preferencesModule)
        }

        PrepopulateWorker.prepopulate(this)
    }
}