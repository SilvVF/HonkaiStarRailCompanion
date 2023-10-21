package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import io.silv.hsrdmgcalc.data.toUi
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.composables.CardType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LightConeViewModel(
    private val displayPreferences: DisplayPreferences,
    private val applicationScope: ApplicationScope,
    honkaiDataRepository: HonkaiDataRepository,
): ViewModel() {

    val lightCones: StateFlow<ImmutableList<UiLightCone>> = honkaiDataRepository.observeAllLightCones()
        .catch { it.printStackTrace() }
        .map { lightCones ->
            lightCones
                .map { lightCone -> lightCone.toUi() }
                .toImmutableList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            persistentListOf()
        )

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