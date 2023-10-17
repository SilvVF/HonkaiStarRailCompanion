package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination

@Composable
fun LaunchedOnSelectedDestinationClick(
    appState: AppState,
    destination: HsrDestination,
    action: suspend () -> Unit
) {

    val onClick by rememberUpdatedState(action)

    DisposableEffect(key1 = Unit) {
        appState.onDestinationClick(
            destination,
            onClick
        )
        onDispose { appState.removeOnDestinationClick(destination, onClick) }
    }
}