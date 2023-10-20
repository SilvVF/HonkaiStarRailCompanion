package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val characterDetailsArgs = CharacterDetailsArgs(savedStateHandle)


}