package io.silv.hsrdmgcalc.ui.screens

import io.silv.hsrdmgcalc.ui.screens.character.CharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::CharacterViewModel)
}