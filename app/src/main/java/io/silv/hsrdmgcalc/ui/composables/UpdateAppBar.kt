package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import io.silv.hsrdmgcalc.ui.AppState

@Composable
fun UpdateBottomAppBar(
    appState: AppState,
    peekContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    SideEffect {
       appState.changeDraggableBottomBarContent(
            peekContent = peekContent,
            content =  content,
        )
    }
}

@Composable
fun ClearBottomAppBarExpandableContent(appState: AppState) {
    SideEffect {
        appState.clearDraggableBottomBarContent()
    }
}