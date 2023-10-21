package io.silv.hsrdmgcalc.ui

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.ExpandableState
import io.silv.hsrdmgcalc.ui.composables.rememberExpandableState
import io.silv.hsrdmgcalc.ui.screens.character.navigateToCharacterGraph
import io.silv.hsrdmgcalc.ui.screens.light_cone.navigateToLightConeGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.rememberKoinInject
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    navController: NavHostController,
    bottomBarState: ExpandableState = rememberExpandableState(startProgress = SheetValue.Hidden),
    scope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass,
    initialTopAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) = remember {
    AppState(
        navController,
        bottomBarState,
        windowSizeClass,
        scope,
        initialTopAppBarScrollBehavior
    )
}

class AppState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val navController: NavHostController,
    val bottomBarState: ExpandableState,
    val windowSizeClass: WindowSizeClass,
    private val scope: CoroutineScope,
    private val initialTopAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    var draggablePeekContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var draggableContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    @OptIn(ExperimentalMaterial3Api::class)
    var topAppBar by mutableStateOf<Pair<TopAppBarScrollBehavior?, @Composable () -> Unit>>(
        initialTopAppBarScrollBehavior to {}
    )


    val destinationTappedActions =
        mutableStateMapOf<HsrDestination, suspend () -> Unit>()

    val destinations = listOf<HsrDestination>(
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

    val currentDestination: Flow<HsrDestination>
        get() = navController.currentBackStackEntryFlow.map { backStackEntry ->
                destinations.fastFirstOrNull { hsrDest ->
                    backStackEntry.destination.route == hsrDest.route
                }
                    ?: HsrDestination.Character
            }

    private var prevId: UUID? = null

    @OptIn(ExperimentalMaterial3Api::class)
    fun changeTopAppBar(scrollBehavior: TopAppBarScrollBehavior, topBar: @Composable () -> Unit) {
        topAppBar = scrollBehavior to topBar
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun clearTopAppBar() {
        topAppBar = null to {}
    }

    @OptIn(ExperimentalMaterial3Api::class)
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

    @OptIn(ExperimentalMaterial3Api::class)
    fun clearDraggableBottomBarContent() {
        prevId = null
        draggableContent = null
        draggablePeekContent = null
    }

    fun handleDestinationClick(destination: HsrDestination) {
        Log.d("NAVBAR", "current = ${navController.currentDestination}, going to =$destination")
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
            HsrDestination.Relic -> navController.navigate("relic_graph", topLevelNavOptions)
            HsrDestination.LightCone -> navController.navigateToLightConeGraph(topLevelNavOptions)
        }
    }
}