package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import io.silv.hsrdmgcalc.data.toUi
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.screens.light_cone.LightConeEvent.LightConeAdded
import io.silv.hsrdmgcalc.ui.screens.light_cone.LightConeEvent.LightConeDeleted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LightConeViewModel(
    private val displayPreferences: DisplayPreferences,
    private val applicationScope: ApplicationScope,
    private val honkaiDataRepository: HonkaiDataRepository,
): ViewModel() {

    private val mutableEvents = Channel<LightConeEvent>()
    val events = mutableEvents.receiveAsFlow()

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

    fun addLightCone(info: LightConeInfo) {
        viewModelScope.launch {
            val id = honkaiDataRepository.addLightCone(
                name = info.first,
                level = info.second,
                superimpose = info.third
            )
            mutableEvents.send(
                LightConeAdded(id, info)
            )
        }
    }

    fun deleteLightCone(id: Long) {
        viewModelScope.launch {
            honkaiDataRepository.removeLightCone(id)?.let { deleted ->
                mutableEvents.send(
                    LightConeDeleted(
                        id,
                        Triple(deleted.name, deleted.level.toInt(), deleted.superimpose.toInt())
                    )
                )
            }
        }
    }
}

sealed interface LightConeEvent {

    data class LightConeAdded(val id: Long, val info: LightConeInfo): LightConeEvent

    data class LightConeDeleted(val id: Long, val info: LightConeInfo): LightConeEvent
}