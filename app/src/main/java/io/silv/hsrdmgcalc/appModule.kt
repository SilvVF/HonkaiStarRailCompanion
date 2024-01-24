package io.silv.hsrdmgcalc

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.silv.data.dataModule
import io.silv.hsrdmgcalc.util.coroutine.AppDispatchers
import io.silv.hsrdmgcalc.util.coroutine.ApplicationScope
import io.silv.hsrdmgcalc.util.coroutine.DefaultDispatcher
import io.silv.hsrdmgcalc.util.coroutine.IODispatcher
import io.silv.hsrdmgcalc.util.coroutine.MainDispatcher
import io.silv.hsrdmgcalc.util.coroutine.UnconfinedDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {

    includes(
        dataModule
    )

    single<Storage> { get<SupabaseClient>().storage }

    single { StorageItemCache(androidContext()) }

    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_API_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Storage)
        }
    }

    single<AppDispatchers> {
        object: AppDispatchers {
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
            override val default: CoroutineDispatcher = Dispatchers.Default
        }
    }

    single<IODispatcher> { get<AppDispatchers>().io }
    single<MainDispatcher> { get<AppDispatchers>().main }
    single<UnconfinedDispatcher> { get<AppDispatchers>().unconfined }
    single<DefaultDispatcher> { get<AppDispatchers>().default }

    single { ApplicationScope() }
}