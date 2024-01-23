package io.silv.data

import androidx.work.WorkManager
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.silv.data.constants.Adapters
import io.silv.honkai.Relic
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {

    single {
        Database(
            RelicAdapter = Relic.Adapter(Adapters.listOfPairStringFloatAdapter),
            driver = AndroidSqliteDriver(
                Database.Schema,
                androidContext(),
                "honkai.db"
            )
        )
    }

    single { WorkManager.getInstance(androidContext()) }

    singleOf(::HonkaiDataRepository)

    workerOf(::PrepopulateWorker)
}