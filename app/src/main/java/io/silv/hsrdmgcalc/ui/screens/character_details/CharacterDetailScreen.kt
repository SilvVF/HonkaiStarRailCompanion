package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.ui.AppState
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetails(
    appState: AppState,
    viewModel: CharacterDetailsViewModel = koinViewModel()
) {
    SideEffect { appState.clearDraggableBottomBarContent() }

    when (val state = viewModel.characterDetailsState.collectAsStateWithLifecycle().value) {
        CharacterDetailsState.Loading -> Unit
        is CharacterDetailsState.Success -> CharacterDetailSuccessScreen(characterDetailsState = state)
    }
}

@Composable
fun CharacterDetailSuccessScreen(
    characterDetailsState: CharacterDetailsState.Success
) {
    val character = characterDetailsState.character

    CollapsingToolbarLayout(
        character = character,
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        repeat(40) {
            Text(text = "sdfaf", Modifier.fillMaxWidth())
        }
    }
}

