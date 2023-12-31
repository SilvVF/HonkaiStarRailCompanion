package io.silv.hsrdmgcalc.ui.screens

import io.silv.hsrdmgcalc.data.CalculateCharacterStats
import io.silv.hsrdmgcalc.data.GetCharacterWithItems
import io.silv.hsrdmgcalc.data.GetCharactersWithItems
import io.silv.hsrdmgcalc.data.ValidateCharacterLevel
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.AddLightConeViewModel
import io.silv.hsrdmgcalc.ui.screens.character.CharacterViewModel
import io.silv.hsrdmgcalc.ui.screens.character_details.CharacterDetailsViewModel
import io.silv.hsrdmgcalc.ui.screens.light_cone.LightConeViewModel
import io.silv.hsrdmgcalc.ui.screens.relic.RelicViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {

    factoryOf(::GetCharactersWithItems)

    factoryOf(::GetCharacterWithItems)

    factoryOf(::ValidateCharacterLevel)

    factoryOf(::CalculateCharacterStats)

    viewModelOf(::CharacterViewModel)

    viewModelOf(::LightConeViewModel)

    viewModelOf(::CharacterDetailsViewModel)

    viewModelOf(::AddLightConeViewModel)

    viewModelOf(::RelicViewModel)
}