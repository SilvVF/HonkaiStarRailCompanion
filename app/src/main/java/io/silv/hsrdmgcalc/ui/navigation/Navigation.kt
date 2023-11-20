package io.silv.hsrdmgcalc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.addLightConeScreen
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.navigateToAddLightCone
import io.silv.hsrdmgcalc.ui.screens.add_relic.addRelicScreen
import io.silv.hsrdmgcalc.ui.screens.add_relic.navigateToAddRelicScreen
import io.silv.hsrdmgcalc.ui.screens.character.CharacterGraphRoute
import io.silv.hsrdmgcalc.ui.screens.character.characterGraph
import io.silv.hsrdmgcalc.ui.screens.character_details.characterDetailsScreen
import io.silv.hsrdmgcalc.ui.screens.character_details.navigateToCharacterDetails
import io.silv.hsrdmgcalc.ui.screens.light_cone.lightConeGraph
import io.silv.hsrdmgcalc.ui.screens.relic.relicGraph

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

            },
            onAddLightConeClick = { navResultCallback ->
                navController.navigateToAddLightCone(navResultCallback)
            }
        ) {
            addLightConeScreen(appState) { result ->
                navController.popBackStackWithResult(result)
            }
        }

        relicGraph(
            appState,
            onRelicClick = {},
            navigateToAddRelicForResult = { navResultCallback ->
                navController.navigateToAddRelicScreen(navResultCallback)
            }
        ) {
            addRelicScreen(appState) { result ->
                navController.popBackStackWithResult(result)
            }
        }
    }
}