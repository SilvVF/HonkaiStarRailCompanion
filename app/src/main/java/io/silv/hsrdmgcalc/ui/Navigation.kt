package io.silv.hsrdmgcalc.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.silv.hsrdmgcalc.ui.screens.character.CharacterScreen

@Composable
fun Navigation(
    appState: AppState
) {

    NavHost(
        navController = appState.navController,
        startDestination = HsrDestination.Character.route
    ) {

        composable(
            route = HsrDestination.Character.route
        ) {
            CharacterScreen(
                appState = appState
            )
        }
    }
}