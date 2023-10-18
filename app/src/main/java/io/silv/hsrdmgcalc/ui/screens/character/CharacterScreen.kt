package io.silv.hsrdmgcalc.ui.screens.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.ExpandedBottomBarContent
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.LocalPaddingValues
import io.silv.hsrdmgcalc.data.CharacterWithItems
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.CompactCharacterCard
import io.silv.hsrdmgcalc.ui.composables.DisplayOptionsBottomSheet
import io.silv.hsrdmgcalc.ui.composables.ExtraCompactCharacterCard
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.SearchTopAppBar
import io.silv.hsrdmgcalc.ui.composables.SemiCompactCharacterCard
import io.silv.hsrdmgcalc.ui.composables.Type
import io.silv.hsrdmgcalc.ui.composables.UpdateBottomAppBar
import io.silv.hsrdmgcalc.ui.composables.UpdateTopBar
import io.silv.hsrdmgcalc.ui.composables.pathFilterRow
import io.silv.hsrdmgcalc.ui.composables.typeFilterRow
import io.silv.hsrdmgcalc.ui.conditional
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = koinViewModel(),
    appState: AppState,
) {
    val charactersWithItems by viewModel.charactersWithItems.collectAsState()

    CharacterScreenContent(
        paddingValues = LocalPaddingValues.current,
        navBarHeight = LocalNavBarHeight.current,
        appState = appState,
        onGridSizeSelected = viewModel::updateGridCellCount,
        onCardTypeSelected = viewModel::updateCardType,
        onAnimateCardPlacementChanged = viewModel::updateAnimateCardPlacement,
        charactersWithItems = charactersWithItems,
        onPathSelected = viewModel::updatePathFilter,
        onTypeSelected = viewModel::updateTypeFilter,
        searchText = viewModel.searchText
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CharacterScreenContent(
    paddingValues: PaddingValues,
    navBarHeight: Dp,
    appState: AppState,

    onGridSizeSelected: (size: Int) -> Unit,
    onCardTypeSelected: (cardType: CardType) -> Unit,
    onAnimateCardPlacementChanged: (animate: Boolean) -> Unit,
    onTypeSelected: (Type?) -> Unit,
    onPathSelected: (Path?) -> Unit,

    charactersWithItems: List<CharacterWithItems>,
    searchText: MutableState<String>,
) {

    var displayOptionsBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedOnSelectedDestinationClick(appState = appState, destination = HsrDestination.Character) {
        appState.bottomBarState.toggleProgress()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searching by rememberSaveable {
        mutableStateOf(false)
    }

    var text by searchText

    UpdateTopBar(appState = appState, scrollBehavior = scrollBehavior) {
        SearchTopAppBar(
            onSearchText = {
                text = it
            },
            showTextField = searching,
            actions = {
                IconButton(
                    onClick = {
                        appState.bottomBarState.progress = SheetValue.Expanded
                    }
                ) {
                    Icon(imageVector = Icons.Filled.FilterList, null)
                }
            },
            scrollBehavior = scrollBehavior,
            searchText = text,
            onSearchChanged = { searchState -> searching = searchState }
        )
    }

    UpdateBottomAppBar(
        appState,
        peekContent = {
            Column {
                val modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(38.dp)
                    .padding(bottom = 6.dp)
                LazyRow(
                    modifier = modifier
                ) {
                    typeFilterRow { type ->
                        onTypeSelected(type)
                    }
                }
                LazyRow(
                    modifier = modifier
                ) {
                    pathFilterRow { path ->
                        onPathSelected(path)
                    }
                }
            }
        },
        content = {
            ExpandedBottomBarContent {
                displayOptionsBottomSheetVisible = true
            }
        },
    )

    DisplayOptionsBottomSheet(
        visible = displayOptionsBottomSheetVisible,
        onDismissRequest = { displayOptionsBottomSheetVisible = false },
        onGridSizeSelected = onGridSizeSelected,
        gridCells = appState.displayPrefs.gridCells,
        onCardTypeSelected = onCardTypeSelected,
        cardType = appState.displayPrefs.cardType,
        animatePlacement = appState.displayPrefs.animateCardPlacement,
        onAnimatePlacementChanged = onAnimateCardPlacementChanged
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
            items(
                items = charactersWithItems,
                key = { item: CharacterWithItems -> item.character.name }
            ) {item ->
                when (appState.displayPrefs.cardType) {
                    CardType.Full,
                    CardType.List,
                    CardType.SemiCompact -> SemiCompactCharacterCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .conditional(appState.displayPrefs.animateCardPlacement) {
                                animateItemPlacement()
                            },
                        character = item.character.name,
                        type = item.character.type,
                        path = item.character.path,
                        fiveStar = item.character.is5star
                    ) {}
                    CardType.Compact -> CompactCharacterCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .conditional(appState.displayPrefs.animateCardPlacement) {
                                animateItemPlacement()
                            },
                        character = item.character.name,
                        type = item.character.type,
                        fiveStar = item.character.is5star
                    ){}
                    CardType.ExtraCompact -> ExtraCompactCharacterCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .conditional(appState.displayPrefs.animateCardPlacement) {
                                animateItemPlacement()
                            },
                        character = item.character.name,
                        type = item.character.type,
                        fiveStar = item.character.is5star
                    ){}
                }
            }
        }
    }
}