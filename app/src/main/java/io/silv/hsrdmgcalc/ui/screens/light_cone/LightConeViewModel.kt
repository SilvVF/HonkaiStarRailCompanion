package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.lifecycle.ViewModel
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.ui.composables.CardType
import kotlinx.coroutines.launch

class LightConeViewModel(
    private val displayPreferences: DisplayPreferences,
    private val applicationScope: ApplicationScope,
): ViewModel() {



    fun updateGridCellCount(count: Int) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(
                    characterPrefs = prev.lightConePrefs.copy(
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
                    characterPrefs = prev.lightConePrefs.copy(
                        cardType = cardType
                    )
                )
            }
        }
    }

    fun updateAnimateCardPlacement(animate: Boolean) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(
                    characterPrefs = prev.lightConePrefs.copy(
                        animateCardPlacement = animate
                    )
                )
            }
        }
    }

}