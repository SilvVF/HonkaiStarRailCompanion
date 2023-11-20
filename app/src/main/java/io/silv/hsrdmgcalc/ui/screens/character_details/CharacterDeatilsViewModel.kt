package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.data.CalculateCharacterStats
import io.silv.hsrdmgcalc.data.CharacterStats
import io.silv.hsrdmgcalc.data.GetCharacterWithItems
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import io.silv.hsrdmgcalc.data.ValidateCharacterLevel
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
    getCharacterWithItems: GetCharacterWithItems,
    calculateCharacterStats: CalculateCharacterStats,
    private val honkaiDataRepository: HonkaiDataRepository,
    private val validateCharacterLevel: ValidateCharacterLevel
): ViewModel() {

    private val characterDetailsArgs = CharacterDetailsArgs(savedStateHandle)

    val characterDetailsState = getCharacterWithItems(characterDetailsArgs.name)
        .map { characterWithItems ->

            val stats = calculateCharacterStats(
                name = characterWithItems.character.name,
                level = characterWithItems.character.level,
                maxLevel = characterWithItems.character.maxLevel,
                relicsStats = characterWithItems.relics.flatMap { relic -> relic.stats },
                lightConeName = characterWithItems.lightCone?.name
            )
                .getOrNull()

            CharacterDetailsState.Success(
                character = characterWithItems.character,
                baseStats = stats?.stats,
                relics = characterWithItems.relics,
                lightCone = characterWithItems.lightCone,
                relicCalcInfo = stats?.relResult,
                lightConeCalcInfo = stats?.lcResult
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CharacterDetailsState.Loading
        )

    fun updateLevel(maxLevel: Int, level: Int) {
        viewModelScope.launch {
            if (validateCharacterLevel(level, maxLevel)) {
                honkaiDataRepository.updateCharacter(characterDetailsArgs.name) { character ->
                    character?.copy(
                        maxLevel = maxLevel.toLong(),
                        level = level.toLong()
                    )
                }
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
        val relicCalcInfo: CharacterStats.CalcInfo?,
        val lightConeCalcInfo: CharacterStats.CalcInfo?
    ): CharacterDetailsState
}