package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.silv.hsrdmgcalc.ui.AppState
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetails(
    appState: AppState,
    viewModel: CharacterDetailsViewModel = koinViewModel()
) {
    SideEffect {
        appState.clearTopAppBar()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = viewModel.characterDetailsArgs.name, Modifier.align(Alignment.Center))
    }
}