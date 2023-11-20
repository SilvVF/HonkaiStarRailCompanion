package io.silv.hsrdmgcalc.ui.screens.add_light_cone

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.navigation.NavResultCallback
import io.silv.hsrdmgcalc.ui.navigation.SubDestination
import io.silv.hsrdmgcalc.ui.navigation.navigateForResult


data class LightConeInfo(
    val key: String,
    val superimpose: Int,
    val level: Int,
    val maxLevel: Int,
)

fun NavController.navigateToAddLightCone(navResultCallback: NavResultCallback<LightConeInfo?>) {
    return this.navigateForResult(
        route = SubDestination.LightConeAdd.route,
        navResultCallback = navResultCallback,
        builder = { launchSingleTop = true }
    )
}

fun NavGraphBuilder.addLightConeScreen(
    appState: AppState,
    navigateBack: (LightConeInfo?) -> Unit,
) {
    composable(
        route = SubDestination.LightConeAdd.route,
    ) {
        AddLightConeScreen(
            appState = appState,
            navigateBack = navigateBack
        )
    }
}