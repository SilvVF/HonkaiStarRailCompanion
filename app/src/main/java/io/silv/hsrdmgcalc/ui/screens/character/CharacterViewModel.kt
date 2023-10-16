package io.silv.hsrdmgcalc.ui.screens.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.prefrences.DisplayPreferences
import io.silv.hsrdmgcalc.prefrences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.CardType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val applicationScope: ApplicationScope,
    private val displayPreferences: DisplayPreferences,
): ViewModel() {

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

    fun updateCardType(cardType: CardType) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(cardType = cardType)
            }
        }
    }
}