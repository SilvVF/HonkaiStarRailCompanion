package io.silv.hsrdmgcalc.prefrences

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.silv.hsrdmgcalc.ui.composables.CardType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Stable
@Immutable
data class DisplayPrefs(
    val gridCells: Int = 3,
    val cardType: CardType = CardType.SemiCompact
)

interface DisplayPreferences {

    fun observePrefs(): Flow<DisplayPrefs>

    suspend fun updatePrefs(update: (prev: DisplayPrefs) -> DisplayPrefs)
}

class DisplayPreferencesImpl(
    private val dataStore: DataStore<Preferences>
): DisplayPreferences {

    private fun Preferences.toDisplayPrefs(): DisplayPrefs {
        return DisplayPrefs(
            gridCells = this[gridCellsKey] ?: 3,
            cardType = CardType.values().getOrNull(this[cardTypeKey] ?: -1)
                ?: CardType.SemiCompact
        )
    }

    override fun observePrefs(): Flow<DisplayPrefs> {
        return dataStore.data.map { prefs ->
            prefs.toDisplayPrefs()
        }
    }

    override suspend fun updatePrefs(update: (prev: DisplayPrefs) -> DisplayPrefs) {
       dataStore.edit { prefs ->

           val updated = update(prefs.toDisplayPrefs())

           prefs[gridCellsKey] = updated.gridCells
           prefs[cardTypeKey] = updated.cardType.ordinal
       }
    }

    companion object {
        private val gridCellsKey = intPreferencesKey("GRID_CELLS_KEY")
        private val cardTypeKey = intPreferencesKey("CARD_TYPE_KEY")
    }
}