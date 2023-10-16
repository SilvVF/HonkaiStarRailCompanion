package io.silv.hsrdmgcalc.ui.screens.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.prefrences.DisplayPreferences
import io.silv.hsrdmgcalc.prefrences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.CardType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(
    appScope: ApplicationScope,
    private val displayPreferences: DisplayPreferences,
): ViewModel() {

    private val applicationScope: CoroutineScope = appScope.scope

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