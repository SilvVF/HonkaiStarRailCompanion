package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import io.silv.hsrdmgcalc.data.toUi
import io.silv.hsrdmgcalc.ui.UiCharacter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val honkaiDataRepository: HonkaiDataRepository
): ViewModel() {

    val characterDetailsArgs = CharacterDetailsArgs(savedStateHandle)


    val characterDetailsState = honkaiDataRepository.observeCharacterByName(characterDetailsArgs.name)
        .map {
            CharacterDetailsState.Success(it.toUi())
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CharacterDetailsState.Loading
        )

    fun updateLevel(maxLevel: Int, level: Int) {

        val levelRanges = listOf(80..90, 70..80, 60..70, 50..60, 40..50, 20..40, 1..20)

        val range = maxLevel.takeIf { max -> max in listOf(20, 40, 50, 60, 70, 80, 90) }
            ?.let { max -> levelRanges.find { r -> r.last == max } }
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
}

sealed interface CharacterDetailsState {

    data object Loading: CharacterDetailsState

    data class Success(val character: UiCharacter): CharacterDetailsState
}