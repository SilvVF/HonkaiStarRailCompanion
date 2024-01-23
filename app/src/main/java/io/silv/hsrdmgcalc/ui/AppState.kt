@file:OptIn(ExperimentalMaterial3Api::class)

package io.silv.hsrdmgcalc.ui

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastFirstOrNull
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import io.silv.data.preferences.DisplayPreferences
import io.silv.data.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.ExpandableState
import io.silv.hsrdmgcalc.ui.composables.rememberExpandableState
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.screens.character.navigateToCharacterGraph
import io.silv.hsrdmgcalc.ui.screens.light_cone.navigateToLightConeGraph
import io.silv.hsrdmgcalc.ui.screens.relic.navigateToRelicGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.rememberKoinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    navController: NavHostController,
    bottomBarState: ExpandableState = rememberExpandableState(startProgress = SheetValue.Hidden),
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    windowSizeClass: WindowSizeClass,
) = remember {
    AppState(
        navController,
        bottomBarState,
        snackbarHostState,
        windowSizeClass,
        scope,
    )
}

class AppState(
    val navController: NavHostController,
    val bottomBarState: ExpandableState,
    private val snackbarHostState: SnackbarHostState,
    private val windowSizeClass: WindowSizeClass,
    private val scope: CoroutineScope,
) {
    var draggablePeekContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var draggableContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
        onActionPerformed: () -> Unit = {}
    ) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration
            )
            if (result == SnackbarResult.ActionPerformed) {
                onActionPerformed()
            }
        }
    }

    private val destinationTappedActions =
        mutableStateMapOf<HsrDestination, suspend () -> Unit>()

    val destinations = listOf(
        HsrDestination.Character,
        HsrDestination.LightCone,
        HsrDestination.Relic
    )

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val displayPrefs: DisplayPrefs
        @Composable get() {
            val preferences = rememberKoinInject<DisplayPreferences>()
            val prefs by preferences
                .observePrefs()
                .collectAsStateWithLifecycle(initialValue = DisplayPrefs())

            return prefs
        }

    val currentTopLevelDest: Flow<HsrDestination>
        get() = navController.currentBackStack.map { backStack ->
            destinations.fastFirstOrNull { dest ->
                dest.route in backStack.map { it.destination.route }
            }
                ?: HsrDestination.Character
        }

    fun changeDraggableBottomBarContent(
        peekContent: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ) {
        draggablePeekContent = peekContent
        draggableContent = content
    }

    fun onDestinationClick(
        destination: HsrDestination,
        action: suspend () -> Unit
    ) {
        destinationTappedActions[destination] = action
    }

    fun clearDraggableBottomBarContent() {
        draggableContent = null
        draggablePeekContent = null
    }

    fun handleDestinationClick(destination: HsrDestination) {
        Log.d("NAVBAR", "current = ${navController.currentDestination}, going to = $destination")
        if (navController.currentDestination?.route == destination.route) {
            scope.launch {
                destinationTappedActions[destination]?.invoke()
            }
        } else {
            navigateToHsrDestination(destination)
        }
    }

    private fun navigateToHsrDestination(destination: HsrDestination) {
        Log.d("Navigation", "trying to navigate to $destination")
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            navController.currentBackStack.value.firstOrNull()?.destination?.id?.let {
                popUpTo(it) {
                    saveState = true
                }
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
        when (destination) {
            HsrDestination.Character -> navController.navigateToCharacterGraph(topLevelNavOptions)
            HsrDestination.Relic -> navController.navigateToRelicGraph(topLevelNavOptions)
            HsrDestination.LightCone -> navController.navigateToLightConeGraph(topLevelNavOptions)
        }
    }
}