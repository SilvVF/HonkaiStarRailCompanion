package io.silv.hsrdmgcalc.ui.screens.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.data.constants.Path
import io.silv.data.constants.Type
import io.silv.data.preferences.CardType
import io.silv.data.preferences.DisplayPreferences
import io.silv.data.preferences.Grouping
import io.silv.hsrdmgcalc.ApplicationScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val applicationScope: ApplicationScope,
    private val displayPreferences: DisplayPreferences,
): ViewModel() {

    private val typeFilter = MutableStateFlow<Type?>(null)
    val selectedTypeFilter = typeFilter.asStateFlow()
    private val pathFilter = MutableStateFlow<Path?>(null)
    val selectedPathFilter = pathFilter.asStateFlow()


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