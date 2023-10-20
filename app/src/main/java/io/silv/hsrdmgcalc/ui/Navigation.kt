package io.silv.hsrdmgcalc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.silv.hsrdmgcalc.ui.screens.character.CharacterGraphRoute
import io.silv.hsrdmgcalc.ui.screens.character.characterGraph
import io.silv.hsrdmgcalc.ui.screens.character_details.characterDetailsScreen
import io.silv.hsrdmgcalc.ui.screens.character_details.navigateToCharacterDetails
import io.silv.hsrdmgcalc.ui.screens.light_cone.lightConeGraph

@Composable
fun Navigation(
    appState: AppState
) {
    val navController = appState.navController
    NavHost(
        navController = appState.navController,
        startDestination = CharacterGraphRoute
    ) {

        characterGraph(
            appState = appState,
            onCharacterClick = { name ->
                navController.navigateToCharacterDetails(name)
            }
        ) {
            characterDetailsScreen(appState)
        }

        lightConeGraph(
            appState,
            onLightConeClick = { id ->

            }
        ) {

        }

        composable(
            route = HsrDestination.Relic.route
        ) {
            SideEffect {
                appState.clearTopAppBar()
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Text("Relic", Modifier.align(Alignment.Center))
            }
        }
    }
}