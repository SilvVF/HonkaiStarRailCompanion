package io.silv.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val preferencesModule = module {

    singleOf(::DisplayPreferencesImpl) {
        bind<DisplayPreferences>()
    }

    singleOf(::DataPreferencesImpl) {
        bind<DataPreferences>()
    }

    single {
        androidContext().dataStore
    }
}