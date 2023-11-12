package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.navigation.SubDestination
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

class CharacterDetailsArgs(val name: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[SubDestination.CharacterDetails.nameArg]), UTF_8.name()))
}

fun NavController.navigateToCharacterDetails(name: String) {
    val encodedId = URLEncoder.encode(name, UTF_8.name())
    this.navigate(SubDestination.CharacterDetails.buildRoute(encodedId)) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.characterDetailsScreen(
    appState: AppState,
) {

    composable(
        route = SubDestination.CharacterDetails.route,
        arguments = listOf(
            navArgument(SubDestination.CharacterDetails.nameArg) { type = NavType.StringType },
        )
    ) {
       CharacterDetails(appState)
    }
}