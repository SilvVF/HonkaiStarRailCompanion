package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.data.CharacterStats
import io.silv.hsrdmgcalc.data.GetCharacterWithItems
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import io.silv.hsrdmgcalc.ui.UiCharacter
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.UiRelic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val honkaiDataRepository: HonkaiDataRepository,
    private val getCharacterWithItems: GetCharacterWithItems
): ViewModel() {

    private val characterDetailsArgs = CharacterDetailsArgs(savedStateHandle)

    val characterDetailsState = getCharacterWithItems.invoke(characterDetailsArgs.name)
        .map { characterWithItems ->
            val uic = characterWithItems.character
            val cBaseStats = CharacterStats.calcBaseStatsOrNull(uic.name, uic.level, uic.maxLevel)
            CharacterDetailsState.Success(
                character = uic,
                baseStats = cBaseStats,
                relics = characterWithItems.relics,
                lightCone = characterWithItems.lightCone,
                baseStatsWithRelics = cBaseStats?.let {
                    CharacterStats.calcBaseStatsWithRelics(
                        baseStats = it,
                        addStats = characterWithItems.relics.flatMap { relic -> relic.stats }
                    )
                }
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CharacterDetailsState.Loading
        )

    fun updateLevel(maxLevel: Int, level: Int) {
        val range = maxLevel.takeIf { max -> max in listOf(20, 40, 50, 60, 70, 80, 90) }
            ?.let { max -> CharacterStats.levelRanges.find { r -> r.last == max } }
            ?: return

        if (level !in range)
            return

        viewModelScope.launch {
            honkaiDataRepository.updateCharacter(characterDetailsArgs.name) { character ->
                character?.copy(
                    maxLevel = maxLevel.toLong(),
                    level = level.toLong()
                )
            }
        }
    }

    fun updateCharacterOwned(owned: Boolean) {
        viewModelScope.launch {
            honkaiDataRepository.updateCharacter(characterDetailsArgs.name) { character ->
                character?.copy(
                    owned = owned
                )
            }
        }
    }
}

sealed interface CharacterDetailsState {

    data object Loading: CharacterDetailsState

    data class Success(
        val character: UiCharacter,
        val relics: ImmutableList<UiRelic>,
        val lightCone: UiLightCone?,
        val baseStats: CharacterStats.BaseStats?,
        val baseStatsWithRelics: CharacterStats.CalcInfo?,
    ): CharacterDetailsState
}