package io.silv.hsrdmgcalc.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastFirstOrNull
import androidx.navigation.NavHostController
import io.silv.hsrdmgcalc.preferences.DisplayPreferences
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.composables.ExpandableState
import io.silv.hsrdmgcalc.ui.composables.rememberExpandableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.compose.rememberKoinInject
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    navController: NavHostController,
    bottomBarState: ExpandableState = rememberExpandableState(startProgress = SheetValue.Hidden),
    scope: CoroutineScope = rememberCoroutineScope(),
    initialTopAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) = remember {
    AppState(navController, bottomBarState, scope, initialTopAppBarScrollBehavior)
}

class AppState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val navController: NavHostController,
    val bottomBarState: ExpandableState,
    private val scope: CoroutineScope,
    private val initialTopAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    var draggablePeekContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var draggableContent by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    @OptIn(ExperimentalMaterial3Api::class)
    var topAppBar by mutableStateOf<Pair<TopAppBarScrollBehavior, @Composable () -> Unit>>(
        initialTopAppBarScrollBehavior to {}
    )

    val destinationTappedActions =
        mutableStateMapOf<HsrDestination, List<Pair<UUID, suspend () -> Unit>>>()

    val destinations = listOf<HsrDestination>(
        HsrDestination.Character,
        HsrDestination.LightCone,
        HsrDestination.Relic
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

    private var prevId: UUID? = null

    @OptIn(ExperimentalMaterial3Api::class)
    fun changeTopAppBar(scrollBehavior: TopAppBarScrollBehavior, topBar: @Composable () -> Unit) {
        topAppBar = scrollBehavior to topBar
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun clearTopAppBar() {
        topAppBar = initialTopAppBarScrollBehavior to {}
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun changeDraggableBottomBarContent(
        peekContent: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ): UUID {
        prevId?.let { clearDraggableBottomBarContent(it) }
        draggablePeekContent = peekContent
        draggableContent = content
        return UUID.randomUUID().also { prevId = it }
    }

    fun onDestinationClick(
        destination: HsrDestination,
        action: suspend () -> Unit
    ): UUID {
        val id = UUID.randomUUID()
        destinationTappedActions[destination] =
            (destinationTappedActions[destination] ?: emptyList()) + (id to action )
        return id
    }

    fun removeOnDestinationClick(destination: HsrDestination,id: UUID) {
        destinationTappedActions[destination] =
            destinationTappedActions
                .getOrDefault(destination, emptyList())
                .filter { (actionId, _) -> actionId != id }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun clearDraggableBottomBarContent(id : UUID) {
        if (prevId == id) {
            prevId = null
            draggableContent = null
            draggablePeekContent = null
        }
    }

    fun navigate(destination: HsrDestination) {
        navController.navigate(destination.route)
    }
}