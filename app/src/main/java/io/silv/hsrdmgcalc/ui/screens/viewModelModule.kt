package io.silv.hsrdmgcalc.ui.screens

import io.silv.hsrdmgcalc.data.GetCharacterWithItems
import io.silv.hsrdmgcalc.ui.screens.character.CharacterViewModel
import io.silv.hsrdmgcalc.ui.screens.character_details.CharacterDetailsViewModel
import io.silv.hsrdmgcalc.ui.screens.light_cone.LightConeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {

    factoryOf(::GetCharacterWithItems)

    viewModelOf(::CharacterViewModel)

    viewModelOf(::LightConeViewModel)

    viewModelOf(::CharacterDetailsViewModel)
}