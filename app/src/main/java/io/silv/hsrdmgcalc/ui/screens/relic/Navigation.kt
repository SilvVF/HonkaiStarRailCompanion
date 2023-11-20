package io.silv.hsrdmgcalc.ui.screens.relic

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.navigation.NavResultCallback
import io.silv.hsrdmgcalc.ui.screens.add_relic.RelicInfo

const val RelicGraph = "relic_graph"

fun NavController.navigateToRelicGraph(navOptions: NavOptions? = null) {
    this.navigate(RelicGraph, navOptions)
}

fun NavGraphBuilder.relicGraph(
    appState: AppState,
    onRelicClick: (id: Long) -> Unit,
    navigateToAddRelicForResult: (NavResultCallback<RelicInfo?>) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = RelicGraph,
        startDestination = HsrDestination.Relic.route
    ) {
        composable(HsrDestination.Relic.route) {
            RelicScreen(
                appState = appState,
                navigateToAddRelicForResult = { callback ->
                    navigateToAddRelicForResult(callback)
                }
            )
        }
        nestedGraphs()
    }
}