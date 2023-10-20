package io.silv.hsrdmgcalc.ui.screens.character

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination

const val CharacterGraphRoute = "character_graph"

fun NavController.navigateToCharacterGraph(navOptions: NavOptions? = null) {
    this.navigate(CharacterGraphRoute, navOptions)
}

fun NavGraphBuilder.characterGraph(
    appState: AppState,
    onCharacterClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {

    navigation(
        route = CharacterGraphRoute,
        startDestination = HsrDestination.Character.route,
    ) {
        composable(route = HsrDestination.Character.route) {
            CharacterScreen(
                appState = appState,
                onCharacterClick = onCharacterClick
            )
        }
        nestedGraphs()
    }
}