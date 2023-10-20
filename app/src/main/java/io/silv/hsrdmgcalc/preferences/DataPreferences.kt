package io.silv.hsrdmgcalc.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface DataPreferences {

    suspend fun characterListVersion(): Int

    suspend fun updateCharacterListVersion(version: Int)
}

class DataPreferencesImpl(
    private val dataStore: DataStore<Preferences>
): DataPreferences {

    override suspend fun characterListVersion(): Int {
        return dataStore.data.map { prefs ->
            prefs[characterListVersionKey]
        }
            .firstOrNull() ?: -1
    }

    override suspend fun updateCharacterListVersion(version: Int) {
        dataStore.edit { prefs ->
            prefs[characterListVersionKey] = version
        }
    }

    companion object {
        val characterListVersionKey = intPreferencesKey("CHARACTER_LIST_VERSION_KEY")

    }
}