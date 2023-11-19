package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import io.silv.hsrdmgcalc.data.toUi
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.LightConeInfo
import io.silv.hsrdmgcalc.ui.screens.light_cone.LightConeEvent.LightConeAdded
import io.silv.hsrdmgcalc.ui.screens.light_cone.LightConeEvent.LightConeDeleted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LightConeViewModel(
    private val displayPreferences: DisplayPreferences,
    private val applicationScope: ApplicationScope,
    private val honkaiDataRepository: HonkaiDataRepository,
): ViewModel() {

    private val mutableEvents = Channel<LightConeEvent>()
    val events = mutableEvents.receiveAsFlow()

    private val mutablePathFilter = MutableStateFlow<Path?>(null)
    val pathFilter = mutablePathFilter.asStateFlow()

    val lightCones: StateFlow<ImmutableList<UiLightCone>> = honkaiDataRepository.observeAllLightCones()
        .combine(mutablePathFilter) { lightCones, pathFilter -> lightCones to pathFilter }
        .catch { it.printStackTrace() }
        .map { (lightCones, pathFilter) ->
            lightCones
                .map { lightCone -> lightCone.toUi() }
                .filter { lightCone -> pathFilter == null || lightCone.path == pathFilter }
                .toImmutableList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            persistentListOf()
        )

    fun updatePathFilter(path: Path?) {
        viewModelScope.launch {
            mutablePathFilter.update { prev ->
                if (path == prev)
                    null
                else
                    path
            }
        }
    }

    fun updateGridCellCount(count: Int) {
        applicationScope.launch {
            displayPreferences.updatePrefs { prev ->
                prev.copy(
                    lightConePrefs = prev.lightConePrefs.copy(
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
                    lightConePrefs = prev.lightConePrefs.copy(
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
                    lightConePrefs = prev.lightConePrefs.copy(
                        animateCardPlacement = animate
                    )
                )
            }
        }
    }

    fun addLightCone(info: LightConeInfo) {
        viewModelScope.launch {
            val id = honkaiDataRepository.addLightCone(
                name = info.key,
                level = info.level,
                superimpose = info.superimpose,
                maxLevel = info.maxLevel
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
                        LightConeInfo(
                            deleted.name,
                            deleted.superimpose.toInt(),
                            deleted.level.toInt(),
                            deleted.maxLevel.toInt()
                        )
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