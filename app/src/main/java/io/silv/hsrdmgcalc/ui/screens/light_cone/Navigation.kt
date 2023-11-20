package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.navigation.NavResultCallback
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.LightConeInfo

const val LightConeGraph = "light_cone_graph"

fun NavController.navigateToLightConeGraph(navOptions: NavOptions? = null) {
    this.navigate(LightConeGraph, navOptions)
}

fun NavGraphBuilder.lightConeGraph(
    appState: AppState,
    onLightConeClick: (id: String) -> Unit,
    onAddLightConeClick: (NavResultCallback<LightConeInfo?>) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = LightConeGraph,
        startDestination = HsrDestination.LightCone.route
    ) {
        composable(HsrDestination.LightCone.route) {
            LightConeScreen(
                appState = appState,
                navigateToAddLightConeForResult = onAddLightConeClick,
            ) {
                onLightConeClick(it)
            }
        }
        nestedGraphs()
    }
}