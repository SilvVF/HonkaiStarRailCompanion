package io.silv.hsrdmgcalc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.silv.hsrdmgcalc.ExpandedBottomBarContent
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.UpdateAppBar
import io.silv.hsrdmgcalc.ui.composables.typeFilterRow
import io.silv.hsrdmgcalc.ui.screens.character.CharacterScreen

@OptIn(ExperimentalMaterial3Api::class)
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
        composable(
            route = HsrDestination.LightCone.route
        ) {
           Box(modifier = Modifier.fillMaxSize()) {

               LaunchedOnSelectedDestinationClick(appState = appState, destination = HsrDestination.LightCone) {
                   appState.bottomBarState.toggleProgress()
               }

               UpdateAppBar(
                   appState,
                   peekContent = {
                       Column {
                           LazyRow(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(bottom = 6.dp)
                           ) {
                               typeFilterRow { type ->

                               }
                           }
                       }
                   },
                   content = {
                       ExpandedBottomBarContent {

                       }
                   },
               )

               Text("Light Cone", Modifier.align(Alignment.Center))
           }
        }
        composable(
            route = HsrDestination.Relic.route
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Relic", Modifier.align(Alignment.Center))
            }
        }
    }
}