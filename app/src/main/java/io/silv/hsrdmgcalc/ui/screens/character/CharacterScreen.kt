package io.silv.hsrdmgcalc.ui.screens.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.LocalPaddingValues
import io.silv.hsrdmgcalc.data.CharacterWithItems
import io.silv.hsrdmgcalc.preferences.Grouping
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.DisplayOptionsBottomSheet
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.ScrollbarLazyColumn
import io.silv.hsrdmgcalc.ui.composables.ScrollbarLazyGrid
import io.silv.hsrdmgcalc.ui.composables.SearchTopAppBar
import io.silv.hsrdmgcalc.ui.composables.Type
import io.silv.hsrdmgcalc.ui.composables.UpdateBottomAppBar
import io.silv.hsrdmgcalc.ui.composables.UpdateTopBar
import io.silv.hsrdmgcalc.ui.conditional
import io.silv.hsrdmgcalc.ui.screens.character.composables.CharacterExpandedBottomBarContent
import io.silv.hsrdmgcalc.ui.screens.character.composables.CharacterListItem
import io.silv.hsrdmgcalc.ui.screens.character.composables.CharacterPeekContent
import io.silv.hsrdmgcalc.ui.screens.character.composables.CompactCharacterCard
import io.silv.hsrdmgcalc.ui.screens.character.composables.ExtraCompactCharacterCard
import io.silv.hsrdmgcalc.ui.screens.character.composables.GroupCharactersBottomSheet
import io.silv.hsrdmgcalc.ui.screens.character.composables.SemiCompactCharacterCard
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = koinViewModel(),
    appState: AppState,
    onCharacterClick: (String) -> Unit
) {
    val charactersWithItems by viewModel.charactersWithItems.collectAsStateWithLifecycle()
    val selectedPathFilter by viewModel.selectedPathFilter.collectAsStateWithLifecycle()
    val selectedTypeFilter by viewModel.selectedTypeFilter.collectAsStateWithLifecycle()

    CharacterScreenContent(
        appState = appState,
        onGridSizeSelected = viewModel::updateGridCellCount,
        onCardTypeSelected = viewModel::updateCardType,
        onAnimateCardPlacementChanged = viewModel::updateAnimateCardPlacement,
        charactersWithItems = charactersWithItems,
        onPathSelected = viewModel::updatePathFilter,
        onTypeSelected = viewModel::updateTypeFilter,
        onCharacterClick = onCharacterClick,
        updateOwnedOnly = viewModel::updateOwnedOnly,
        updateFiveStarOnly = viewModel::updateFiveStarOnly,
        updateGroupByLevel = viewModel::updateGroupByLevel,
        pathFilter = selectedPathFilter,
        typeFilter = selectedTypeFilter
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CharacterScreenContent(
    appState: AppState,
    onCharacterClick: (name: String) -> Unit,

    onGridSizeSelected: (size: Int) -> Unit,
    onCardTypeSelected: (cardType: CardType) -> Unit,
    onAnimateCardPlacementChanged: (animate: Boolean) -> Unit,
    onTypeSelected: (Type?) -> Unit,
    onPathSelected: (Path?) -> Unit,

    updateOwnedOnly: (Boolean) -> Unit,
    updateFiveStarOnly: (Boolean) -> Unit,
    updateGroupByLevel: (Grouping) -> Unit,

    charactersWithItems: ImmutableList<CharacterWithItems>,
    pathFilter: Path?,
    typeFilter: Type?
) {

    var displayOptionsBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var groupCharactersBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val appPrefs = appState.displayPrefs
    val displayPrefs = appPrefs.characterPrefs

    LaunchedOnSelectedDestinationClick(appState = appState, destination = HsrDestination.Character) {
        appState.bottomBarState.toggleProgress()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searching by rememberSaveable {
        mutableStateOf(false)
    }

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    UpdateTopBar(appState = appState, scrollBehavior = scrollBehavior) {
        SearchTopAppBar(
            onSearchText = { searchText = it },
            showTextField = searching,
            actions = {
                IconButton(
                    onClick = {
                        appState.bottomBarState.progress =
                            if (appState.bottomBarState.progress == SheetValue.Expanded)
                                SheetValue.Hidden
                            else
                                SheetValue.Expanded
                    }
                ) {
                    Icon(imageVector = Icons.Filled.FilterList, null)
                }
            },
            scrollBehavior = scrollBehavior,
            searchText = searchText,
            onSearchChanged = { searchState -> searching = searchState }
        )
    }

    UpdateBottomAppBar(
        appState = appState,
        peekContent = {
           CharacterPeekContent(
               selectedPath = pathFilter,
               selectedType = typeFilter,
               onTypeSelected =  onTypeSelected,
               onPathSelected = onPathSelected
           )
        },
        content = {
            CharacterExpandedBottomBarContent(
                showDisplayOptions = { displayOptionsBottomSheetVisible = true },
                showGroupingOptions = { groupCharactersBottomSheetVisible = true}
            )
        },
    )

    DisplayOptionsBottomSheet(
        visible = displayOptionsBottomSheetVisible,
        onDismissRequest = { displayOptionsBottomSheetVisible = false },
        onGridSizeSelected = onGridSizeSelected,
        onCardTypeSelected = onCardTypeSelected,
        onAnimatePlacementChanged = onAnimateCardPlacementChanged,
        prefs = displayPrefs
    )

    GroupCharactersBottomSheet(
        visible = groupCharactersBottomSheetVisible,
        prefs = appPrefs.characterGrouping,
        updateOwnedOnly = updateOwnedOnly,
        updateFiveStarOnly = updateFiveStarOnly,
        updateSortByLevel = updateGroupByLevel,
        onDismissRequest = { groupCharactersBottomSheetVisible = false }
    )

    val paddingValues = LocalPaddingValues.current

    Surface(
        Modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                bottom = LocalNavBarHeight.current
            )
            .fillMaxSize()
    ) {

        val filteredCharacter by remember(searchText, charactersWithItems) {
            derivedStateOf {
                charactersWithItems.filter {
                    searchText.isBlank() || searchText.lowercase() in it.character.name.lowercase()
                }
            }
        }

        if (displayPrefs.cardType == CardType.List) {
            ScrollbarLazyColumn {
                items(
                    items = filteredCharacter,
                    key = { item: CharacterWithItems -> item.character.name }
                ) {item ->
                    val height = (LocalConfiguration.current.screenHeightDp * 0.1f).dp
                    CharacterListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(60.dp, height)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    true,
                                    Dp.Unspecified,
                                    item.character.type.color
                                )
                            ) {
                                onCharacterClick(item.character.name)
                            }
                            .padding(4.dp)
                            .conditional(displayPrefs.animateCardPlacement) {
                                animateItemPlacement()
                            },
                        character = item.character
                    )
                }
            }
        } else {
            ScrollbarLazyGrid(
                columns = GridCells.Fixed(displayPrefs.gridCells),
                cellCount = displayPrefs.gridCells
            ) {
                items(
                    items = filteredCharacter,
                    key = { item: CharacterWithItems -> item.character.name }
                ) { item ->
                    when (displayPrefs.cardType) {
                        CardType.Full,
                        CardType.SemiCompact -> SemiCompactCharacterCard(
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth()
                                .conditional(displayPrefs.animateCardPlacement) {
                                    animateItemPlacement()
                                },
                            character = item.character.name,
                            type = item.character.type,
                            path = item.character.path,
                            fiveStar = item.character.is5star
                        ) {
                            onCharacterClick(item.character.name)
                        }
                        CardType.Compact -> CompactCharacterCard(
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth()
                                .conditional(displayPrefs.animateCardPlacement) {
                                    animateItemPlacement()
                                },
                            character = item.character.name,
                            type = item.character.type,
                            fiveStar = item.character.is5star
                        ) {
                            onCharacterClick(item.character.name)
                        }
                        else -> ExtraCompactCharacterCard(
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth()
                                .conditional(displayPrefs.animateCardPlacement) {
                                    animateItemPlacement()
                                },
                            character = item.character.name,
                            type = item.character.type,
                            fiveStar = item.character.is5star
                        ) {
                            onCharacterClick(item.character.name)
                        }
                    }
                }
            }
        }
    }
}