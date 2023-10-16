package io.silv.hsrdmgcalc

import io.silv.hsrdmgcalc.prefrences.preferencesModule
import io.silv.hsrdmgcalc.ui.screens.viewModelModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val appModule = module {

    includes(
        preferencesModule,
        viewModelModule
    )

    single {
        object: AppDispatchers {
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
            override val default: CoroutineDispatcher = Dispatchers.Default
        }
    }

    single<ApplicationScope> {
        object : ApplicationScope {
            override val scope = GlobalScope
        }
    }
}