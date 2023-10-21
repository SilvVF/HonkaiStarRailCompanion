package io.silv.hsrdmgcalc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.silv.hsrdmgcalc.ui.composables.UpdateBlankBottomAppBar
import io.silv.hsrdmgcalc.ui.screens.character.CharacterGraphRoute
import io.silv.hsrdmgcalc.ui.screens.character.characterGraph
import io.silv.hsrdmgcalc.ui.screens.character_details.characterDetailsScreen
import io.silv.hsrdmgcalc.ui.screens.character_details.navigateToCharacterDetails
import io.silv.hsrdmgcalc.ui.screens.light_cone.lightConeGraph

@Composable
fun Navigation(
    appState: AppState,
) {
    val navController = appState.navController
    NavHost(
        navController = appState.navController,
        startDestination = CharacterGraphRoute,
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

        navigation(
            startDestination = HsrDestination.Relic.route,
            route = "relic_graph",
        ) {
            composable(HsrDestination.Relic.route) {
                UpdateBlankBottomAppBar(appState = appState)

                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Relic", Modifier.align(Alignment.Center))
                }
            }
        }
    }
}