package io.silv.hsrdmgcalc.ui.screens.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.ExpandedBottomBarContent
import io.silv.hsrdmgcalc.HonkaiConstants
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.LocalPaddingValues
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.CompactCharacterCard
import io.silv.hsrdmgcalc.ui.composables.DisplayOptionsBottomSheet
import io.silv.hsrdmgcalc.ui.composables.ExtraCompactCharacterCard
import io.silv.hsrdmgcalc.ui.composables.Type
import io.silv.hsrdmgcalc.ui.composables.UpdateAppBar
import io.silv.hsrdmgcalc.ui.composables.pathFilterRow
import io.silv.hsrdmgcalc.ui.composables.typeFilterRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = koinViewModel(),
    appState: AppState,
) {

    CharacterScreenContent(
        paddingValues = LocalPaddingValues.current,
        navBarHeight = LocalNavBarHeight.current,
        appState = appState,
        onGridSizeSelected = viewModel::updateGridCellCount,
        onCardTypeSelected = viewModel::updateCardType
    )
}

@Composable
fun CharacterScreenContent(
    paddingValues: PaddingValues,
    navBarHeight: Dp,
    appState: AppState,

    onGridSizeSelected: (size: Int) -> Unit,
    onCardTypeSelected: (cardType: CardType) -> Unit
) {

    var displayOptionsBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    UpdateAppBar(
        appState,
        peekContent = {
            Column {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                ) {
                    typeFilterRow { type ->

                    }
                }
                LazyRow(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)) {
                    pathFilterRow { path ->

                    }
                }
            }
        },
        content = {
            ExpandedBottomBarContent {
                displayOptionsBottomSheetVisible = true
            }
        }
    )

    DisplayOptionsBottomSheet(
        visible = displayOptionsBottomSheetVisible,
        onDismissRequest = { displayOptionsBottomSheetVisible = false },
        onGridSizeSelected = onGridSizeSelected,
        gridCells = appState.displayPrefs.gridCells,
        onCardTypeSelected = onCardTypeSelected,
        cardType = appState.displayPrefs.cardType
    )

    Surface(
        Modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                bottom = navBarHeight
            )
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(appState.displayPrefs.gridCells),
        ) {
            itemsIndexed(HonkaiConstants.characters) { i, character ->
                when (appState.displayPrefs.cardType) {
                    CardType.Full,
                    CardType.List,
                    CardType.SemiCompact,
                    CardType.Compact -> CompactCharacterCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        character = character,
                        type = Type.values().random(),
                        fiveStar = i < 17
                    ){}
                    CardType.ExtraCompact -> ExtraCompactCharacterCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        character = character,
                        type = Type.values().random(),
                        fiveStar = i < 17
                    ){}
                }
            }
        }
    }
}