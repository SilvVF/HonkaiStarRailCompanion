package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination
import kotlinx.coroutines.launch

@Composable
fun BottomBarWithDraggableContent(
    appState: AppState,
    navBarHeight: Dp,
) {

    val selectedDest by appState.currentDestination.collectAsState(initial = HsrDestination.Character)
    val scope = rememberCoroutineScope()

    Column {
        AnimatedContent(
            targetState = appState.draggableContent to appState.draggablePeekContent,
            label = "bottom-bar-content",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { (content, peek) ->
            ExpandableInfoLayout(
                appState.bottomBarState,
                peekContent = {
                    Box { peek?.invoke() }
                },
                content = {
                    Box { content?.invoke() }
                }
            )
        }
        BottomAppBar(
            Modifier.height(navBarHeight)
        ) {
            appState.destinations.forEach { dest ->
                NavigationBarItem(
                    selected = selectedDest == dest,
                    onClick = {
                        if (selectedDest == dest) {
                            for ((_, action) in appState.destinationTappedActions[dest] ?: emptyList()) {
                                scope.launch {
                                    action.invoke()
                                }
                            }
                        } else {
                            appState.navigate(dest)
                        }
                    },
                    icon = { dest.icon(selectedDest == dest) }
                )
            }
        }
    }
}
