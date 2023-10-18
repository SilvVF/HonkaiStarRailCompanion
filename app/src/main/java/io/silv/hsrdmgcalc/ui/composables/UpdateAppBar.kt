package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.silv.hsrdmgcalc.ui.AppState

@Composable
fun UpdateBottomAppBar(
    appState: AppState,
    peekContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    DisposableEffect(Unit) {

       val id = appState.changeDraggableBottomBarContent(
            peekContent = peekContent,
            content =  content,
        )

        onDispose {
            appState.clearDraggableBottomBarContent(id)
        }
    }
}