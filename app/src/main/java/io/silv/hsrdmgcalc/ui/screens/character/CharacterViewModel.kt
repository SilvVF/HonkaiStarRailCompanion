package io.silv.hsrdmgcalc.ui.screens.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.data.GetCharacterWithItems
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.preferences.Grouping
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.Type
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val applicationScope: ApplicationScope,
    private val displayPreferences: DisplayPreferences,
    getCharacterWithItems: GetCharacterWithItems,
): ViewModel() {

    private val typeFilter = MutableStateFlow<Type?>(null)
    val selectedTypeFilter = typeFilter.asStateFlow()
    private val pathFilter = MutableStateFlow<Path?>(null)
    val selectedPathFilter = pathFilter.asStateFlow()

    val charactersWithItems = combine(
        getCharacterWithItems(),
        typeFilter,
        pathFilter,
        displayPreferences.observePrefs().map { prefs -> prefs.characterGrouping },
    ) { items, type, path, grouping ->

        var filteredItems = items
            .filter { item -> item.character.type == type || type == null }
            .filter { item -> item.character.path == path || path == null }

        if (grouping.fiveStarOnly)
            filteredItems = filteredItems.filter { item -> item.character.is5star }
        if (grouping.ownedOnly)
            filteredItems = filteredItems.filter { item -> item.character.owned }

        filteredItems = when(grouping.level) {
            Grouping.ASC -> filteredItems.sortedBy { item -> item.character.level }
            Grouping.DSC -> filteredItems.sortedByDescending { item -> item.character.level }
            Grouping.NONE -> filteredItems
        }

        filteredItems.toImmutableList()
    }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            persistentListOf()
        )


    fun updateGridCellCount(count: Int) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(
                    characterPrefs = prev.characterPrefs.copy(
                        gridCells = count
                    )
                )
            }
        }
    }

    fun updateCardType(cardType: CardType) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(
                    characterPrefs = prev.characterPrefs.copy(
                        cardType = cardType
                    )
                )
            }
        }
    }

    fun updateGroupByLevel(grouping: Grouping) {
        applicationScope.launch {
            displayPreferences.updatePrefs {prev ->
                prev.copy(
                    characterGrouping = prev.characterGrouping.copy(
                        level = grouping
                    )
                )
            }
        }
    }

    fun updateOwnedOnly(ownedOnly: Boolean) {
        applicationScope.launch {
            displayPreferences.updatePrefs {prev ->
                prev.copy(
                    characterGrouping = prev.characterGrouping.copy(
                        ownedOnly = ownedOnly
                    )
                )
            }
        }
    }

    fun updateFiveStarOnly(fiveStarOnly: Boolean) {
        applicationScope.launch {
            displayPreferences.updatePrefs {prev ->
                prev.copy(
                    characterGrouping = prev.characterGrouping.copy(
                        fiveStarOnly = fiveStarOnly
                    )
                )
            }
        }
    }

    fun updateAnimateCardPlacement(animate: Boolean) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(
                    characterPrefs = prev.characterPrefs.copy(
                        animateCardPlacement = animate
                    )
                )
            }
        }
    }

    fun updateTypeFilter(type: Type?) {
        viewModelScope.launch {
            typeFilter.emit(type)
        }
    }

    fun updatePathFilter(path: Path?) {
        viewModelScope.launch {
            pathFilter.emit(path)
        }
    }

}