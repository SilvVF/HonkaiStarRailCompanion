package io.silv.hsrdmgcalc.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastFirstOrNull
import androidx.navigation.NavHostController
import io.silv.hsrdmgcalc.prefrences.DisplayPreferences
import io.silv.hsrdmgcalc.prefrences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.DraggableBottomBarState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.compose.rememberKoinInject


@Composable
fun rememberAppState(
    navController: NavHostController,
    bottomBarState: DraggableBottomBarState,
) = remember {
    AppState(navController, bottomBarState)
}

class AppState(
    val navController: NavHostController,
    val bottomBarState: DraggableBottomBarState,
) {
    var draggablePeekContent by mutableStateOf<(@Composable (peekModifier: Modifier) -> Unit)?>(null)
        private set

    var draggableContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var topAppBar by mutableStateOf<@Composable () -> Unit>({})

    val destinations = persistentListOf<HsrDestination>(
        HsrDestination.Character
    )

    val displayPrefs: DisplayPrefs
        @Composable get() {
            val preferences = rememberKoinInject<DisplayPreferences>()
            val prefs by preferences.observePrefs().collectAsState(initial = DisplayPrefs())

            return prefs
        }

    val currentDestination: Flow<HsrDestination>
        get() = navController.currentBackStackEntryFlow.map { backStackEntry ->
                destinations.fastFirstOrNull { hsrDest ->
                    backStackEntry.destination.route == hsrDest.route
                }
                    ?: HsrDestination.Character
            }

    fun changeDraggableBottomBarContent(
        peekContent: @Composable (peekModifier: Modifier) -> Unit,
        content: @Composable () -> Unit,
    ) {
        draggablePeekContent = peekContent
        draggableContent = content
    }

    fun clearDraggableBottomBarContent() {
        draggableContent = null
        draggablePeekContent = null
    }

    fun navigate(destination: HsrDestination) {
        navController.navigate(destination.route)
    }
}