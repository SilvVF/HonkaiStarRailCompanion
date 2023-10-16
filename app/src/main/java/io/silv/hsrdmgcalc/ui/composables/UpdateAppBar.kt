package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.silv.hsrdmgcalc.ui.AppState

@Composable
fun UpdateAppBar(
    appState: AppState,
    peekContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    
    DisposableEffect(Unit) {
        appState.changeDraggableBottomBarContent(
            peekContent = { peekModifier ->
                Box(modifier = peekModifier) {
                    peekContent()
                }
            },
            content = {
                content()
            },
        )
        onDispose { appState.clearDraggableBottomBarContent() }
    }
}