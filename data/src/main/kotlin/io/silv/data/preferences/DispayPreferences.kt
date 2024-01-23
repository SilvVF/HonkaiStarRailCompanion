package io.silv.data.preferences

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class Grouping {
    ASC, DSC, NONE
}

enum class CardType(val string: String) {
    Full("Full Card"),
    List("List"),
    SemiCompact("Semi-Compact Card"),
    Compact("Compact Card"),
    ExtraCompact("Extra-Compact Card")
}

@Stable
@Immutable
data class DisplayPrefs(
    val characterPrefs: Prefs = Prefs(),
    val characterGrouping: CharacterGrouping = CharacterGrouping(),
    val relicPrefs: Prefs = Prefs(),
    val lightConePrefs: Prefs = Prefs(),
) {
    @Stable
    @Immutable
    data class Prefs(
        val gridCells: Int = 3,
        val cardType: CardType = CardType.SemiCompact,
        val animateCardPlacement: Boolean = true,
    )

    @Stable
    @Immutable
    data class CharacterGrouping(
        val ownedOnly: Boolean = false,
        val fiveStarOnly: Boolean = false,
        val level: Grouping = Grouping.NONE,
    )
}

interface DisplayPreferences {

    fun observePrefs(): Flow<DisplayPrefs>

    suspend fun updatePrefs(update: (prev: DisplayPrefs) -> DisplayPrefs)
}

class DisplayPreferencesImpl(
    private val dataStore: DataStore<Preferences>
): DisplayPreferences {

    private fun Preferences.displayPref(
        gridCells: Preferences.Key<Int>,
        cardType: Preferences.Key<Int>,
        animate: Preferences.Key<Boolean>
    ): DisplayPrefs.Prefs {
        return DisplayPrefs.Prefs(
            gridCells = this[gridCells] ?: 3,
            cardType = CardType.values().getOrNull(this[cardType] ?: -1) ?: CardType.SemiCompact,
            animateCardPlacement = this[animate] ?: true
        )
    }

    private fun Preferences.toDisplayPrefs(): DisplayPrefs {
        return DisplayPrefs(
            characterPrefs = displayPref(
                gridCells = characterGridCellsKey,
                cardType = characterCardTypeKey,
                animate = characterAnimatePlacementKey,
            ),
            lightConePrefs = displayPref(
                gridCells = lightConeGridCellsKey,
                cardType = lightConeCardTypeKey,
                animate = lightConeAnimatePlacementKey
            ),
            relicPrefs = displayPref(
                gridCells = relicGridCellsKey,
                cardType = relicCardTypeKey,
                animate = relicAnimatePlacementKey
            ),
            characterGrouping = DisplayPrefs.CharacterGrouping(
                ownedOnly = this[characterGroupOwnedOnlyKey] ?: false,
                fiveStarOnly = this[characterGroupFiveStarOnlyKey] ?: false,
                level = Grouping.values().getOrNull(this[characterGroupLevelKey] ?: -1) ?: Grouping.NONE
            )
        )
    }

    override fun observePrefs(): Flow<DisplayPrefs> {
        return dataStore.data.map { prefs ->
            prefs.toDisplayPrefs()
        }
    }

    override suspend fun updatePrefs(update: (prev: DisplayPrefs) -> DisplayPrefs) {
       dataStore.edit { prefs ->

           val updated = update(
               prefs.toDisplayPrefs()
           )

           prefs[characterGridCellsKey] = updated.characterPrefs.gridCells
           prefs[characterCardTypeKey] = updated.characterPrefs.cardType.ordinal
           prefs[characterAnimatePlacementKey] = updated.characterPrefs.animateCardPlacement
           prefs[characterGroupLevelKey] = updated.characterGrouping.level.ordinal
           prefs[characterGroupOwnedOnlyKey] = updated.characterGrouping.ownedOnly
           prefs[characterGroupFiveStarOnlyKey] = updated.characterGrouping.fiveStarOnly

           prefs[lightConeGridCellsKey] = updated.lightConePrefs.gridCells
           prefs[lightConeCardTypeKey] = updated.lightConePrefs.cardType.ordinal
           prefs[lightConeAnimatePlacementKey] = updated.lightConePrefs.animateCardPlacement

           prefs[relicGridCellsKey] = updated.relicPrefs.gridCells
           prefs[relicCardTypeKey] = updated.relicPrefs.cardType.ordinal
           prefs[relicAnimatePlacementKey] = updated.relicPrefs.animateCardPlacement
       }
    }

    companion object {
        private val characterGridCellsKey = intPreferencesKey("CHARACTER_GRID_CELLS_KEY")
        private val characterCardTypeKey = intPreferencesKey("CHARACTER_CARD_TYPE_KEY")
        private val characterAnimatePlacementKey = booleanPreferencesKey("CHARACTER_ANIMATE_PLACEMENT_KEY")
        private val characterGroupOwnedOnlyKey = booleanPreferencesKey("CHARACTER_GROUP_OWNED_ONLY_KEY")
        private val characterGroupFiveStarOnlyKey = booleanPreferencesKey("CHARACTER_GROUP_FIVE_STAR_ONLY_KEY")
        private val characterGroupLevelKey = intPreferencesKey("CHARACTER_GROUP_LEVEL_KEY")

        private val lightConeGridCellsKey = intPreferencesKey("LIGHT_CONE_GRID_CELLS_KEY")
        private val lightConeCardTypeKey = intPreferencesKey("LIGHT_CONE_CARD_TYPE_KEY")
        private val lightConeAnimatePlacementKey = booleanPreferencesKey("LIGHT_CONE_ANIMATE_PLACEMENT_KEY")

        private val relicGridCellsKey = intPreferencesKey("RELIC_GRID_CELLS_KEY")
        private val relicCardTypeKey = intPreferencesKey("RELIC_CARD_TYPE_KEY")
        private val relicAnimatePlacementKey = booleanPreferencesKey("RELIC_ANIMATE_PLACEMENT_KEY")
    }
}