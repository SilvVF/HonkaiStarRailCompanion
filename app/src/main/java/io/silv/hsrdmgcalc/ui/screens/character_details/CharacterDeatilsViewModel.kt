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

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    honkaiDataRepository: HonkaiDataRepository
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


}

sealed interface CharacterDetailsState {

    data object Loading: CharacterDetailsState

    data class Success(val character: UiCharacter): CharacterDetailsState
}