package io.silv.hsrdmgcalc.ui.screens.add_light_cone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.hsrdmgcalc.ApplicationScope
import io.silv.hsrdmgcalc.data.HonkaiConstants
import io.silv.hsrdmgcalc.data.HonkaiDataRepository
import kotlinx.collections.immutable.toImmutableList


class AddLightConeViewModel(
    private val dataRepository: HonkaiDataRepository,
    private val applicationScope: ApplicationScope,
): ViewModel() {

    val state = LightConeSearchStateHolder(
        initialFilters = LightConeFilters(),
        initialQuery = "",
        lightConesList = HonkaiConstants.lightCones.toImmutableList(),
        scope = viewModelScope
    )
}