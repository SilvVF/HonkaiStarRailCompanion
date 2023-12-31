package io.silv.hsrdmgcalc

import io.silv.hsrdmgcalc.data.dataModule
import io.silv.hsrdmgcalc.preferences.preferencesModule
import io.silv.hsrdmgcalc.ui.screens.viewModelModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module


val appModule = module {

    includes(
        preferencesModule,
        viewModelModule,
        dataModule
    )

    single<AppDispatchers> {
        object: AppDispatchers {
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
            override val default: CoroutineDispatcher = Dispatchers.Default
        }
    }

    single { ApplicationScope() }
}