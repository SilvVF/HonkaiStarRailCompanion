package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import io.silv.hsrdmgcalc.ui.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTopBar(
    appState: AppState,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    content: @Composable () -> Unit
) {

    SideEffect {
        appState.changeTopAppBar(
            scrollBehavior = scrollBehavior,
            topBar = content
        )
    }
}