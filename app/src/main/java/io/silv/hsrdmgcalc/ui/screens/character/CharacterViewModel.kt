package io.silv.hsrdmgcalc.ui.screens.character

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.data.GetCharacterWithItems
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.Type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val applicationScope: ApplicationScope,
    private val displayPreferences: DisplayPreferences,
    getCharacterWithItems: GetCharacterWithItems,
): ViewModel() {

    private val typeFilter = MutableStateFlow<Type?>(null)
    private val pathFilter = MutableStateFlow<Path?>(null)

    var searchText = mutableStateOf("")

    val charactersWithItems = combine(
        getCharacterWithItems(),
        typeFilter,
        pathFilter,
        snapshotFlow { searchText.value }
    ) { items, type, path, search ->
        items
            .filter { item -> item.character.type == type || type == null }
            .filter { item -> item.character.path == path || path == null }
            .filter { item -> search.lowercase() in item.character.name.lowercase() }
    }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val displayPrefs = displayPreferences.observePrefs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DisplayPrefs()
        )

    fun updateGridCellCount(count: Int) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(gridCells = count)
            }
        }
    }

    fun updateAnimateCardPlacement(animate: Boolean) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(animateCardPlacement = animate)
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

    fun updateCardType(cardType: CardType) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(cardType = cardType)
            }
        }
    }
}