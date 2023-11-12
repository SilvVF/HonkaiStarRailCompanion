package io.silv.hsrdmgcalc.ui.screens.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.data.CharacterWithItems
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.preferences.Grouping
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.DisplayOptionsBottomSheet
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.ScrollbarLazyColumn
import io.silv.hsrdmgcalc.ui.composables.ScrollbarLazyGrid
import io.silv.hsrdmgcalc.ui.composables.SearchTopAppBar
import io.silv.hsrdmgcalc.ui.composables.UpdateBottomAppBar
import io.silv.hsrdmgcalc.ui.conditional
import io.silv.hsrdmgcalc.ui.screens.character.composables.CharacterExpandedBottomBarContent
import io.silv.hsrdmgcalc.ui.screens.character.composables.CharacterListItem
import io.silv.hsrdmgcalc.ui.screens.character.composables.CharacterPeekContent
import io.silv.hsrdmgcalc.ui.screens.character.composables.CompactCharacterCard
import io.silv.hsrdmgcalc.ui.screens.character.composables.ExtraCompactCharacterCard
import io.silv.hsrdmgcalc.ui.screens.character.composables.GroupCharactersBottomSheet
import io.silv.hsrdmgcalc.ui.screens.character.composables.SemiCompactCharacterCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = koinViewModel(),
    appState: AppState,
    onCharacterClick: (String) -> Unit,
) {
    val charactersWithItems by viewModel.charactersWithItems.collectAsStateWithLifecycle()
    val selectedPathFilter by viewModel.selectedPathFilter.collectAsStateWithLifecycle()
    val selectedTypeFilter by viewModel.selectedTypeFilter.collectAsStateWithLifecycle()


    val prefs = appState.displayPrefs

    LaunchedOnSelectedDestinationClick(
        appState = appState,
        destination = HsrDestination.Character
    ) {
        appState.bottomBarState.toggleProgress()
    }


    var displayOptionsBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var groupCharactersBottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    UpdateBottomAppBar(
        appState = appState,
        peekContent = {
            CharacterPeekContent(
                selectedPath = selectedPathFilter,
                selectedType = selectedTypeFilter,
                onTypeSelected =  viewModel::updateTypeFilter,
                onPathSelected = viewModel::updatePathFilter
            )
        },
        content = {
            CharacterExpandedBottomBarContent(
                showDisplayOptions = { displayOptionsBottomSheetVisible = true },
                showGroupingOptions = { groupCharactersBottomSheetVisible = true}
            )
        },
    )


    CharacterScreenContent(
        onGridSizeSelected = viewModel::updateGridCellCount,
        onCardTypeSelected = viewModel::updateCardType,
        onAnimateCardPlacementChanged = viewModel::updateAnimateCardPlacement,
        charactersWithItems = charactersWithItems,
        onCharacterClick = onCharacterClick,
        updateOwnedOnly = viewModel::updateOwnedOnly,
        updateFiveStarOnly = viewModel::updateFiveStarOnly,
        updateGroupByLevel = viewModel::updateGroupByLevel,
        characterPrefs = prefs.characterPrefs,
        characterGrouping = prefs.characterGrouping,
        updateDisplayOptionsBottomSheetVisible = { visible ->
            displayOptionsBottomSheetVisible = visible
        },
        updateGroupCharactersBottomSheetVisible = { visible ->
            groupCharactersBottomSheetVisible = visible
        },
        displayOptionsBottomSheetVisible = displayOptionsBottomSheetVisible,
        groupCharactersBottomSheetVisible = groupCharactersBottomSheetVisible,
        shouldShowBottomBar = appState.shouldShowBottomBar,
        expandablebBottomBarProgress = appState.bottomBarState.progress,
        updateExpandableBottomBarProgress = {
            appState.bottomBarState.progress = it
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CharacterScreenContent(
    shouldShowBottomBar: Boolean,
    expandablebBottomBarProgress: SheetValue,
    characterPrefs: DisplayPrefs.Prefs,
    characterGrouping: DisplayPrefs.CharacterGrouping,
    updateExpandableBottomBarProgress: (SheetValue) -> Unit,

    onCharacterClick: (name: String) -> Unit,

    onGridSizeSelected: (size: Int) -> Unit,
    onCardTypeSelected: (cardType: CardType) -> Unit,
    onAnimateCardPlacementChanged: (animate: Boolean) -> Unit,
    updateOwnedOnly: (Boolean) -> Unit,
    updateFiveStarOnly: (Boolean) -> Unit,
    updateGroupByLevel: (Grouping) -> Unit,

    displayOptionsBottomSheetVisible: Boolean,
    updateDisplayOptionsBottomSheetVisible: (Boolean) -> Unit,
    groupCharactersBottomSheetVisible: Boolean,
    updateGroupCharactersBottomSheetVisible: (Boolean) -> Unit,


    charactersWithItems: ImmutableList<CharacterWithItems>,
) {

    DisplayOptionsBottomSheet(
        visible = displayOptionsBottomSheetVisible,
        optionsTitle = {
                       Text(
                           text = "Character options",
                           style = MaterialTheme.typography.titleLarge,
                           modifier = Modifier.padding(12.dp)
                       )
        },
        onDismissRequest = { updateDisplayOptionsBottomSheetVisible(false) },
        onGridSizeSelected = onGridSizeSelected,
        onCardTypeSelected = onCardTypeSelected,
        onAnimatePlacementChanged = onAnimateCardPlacementChanged,
        prefs = characterPrefs
    )

    GroupCharactersBottomSheet(
        visible = groupCharactersBottomSheetVisible,
        prefs = characterGrouping,
        updateOwnedOnly = updateOwnedOnly,
        updateFiveStarOnly = updateFiveStarOnly,
        updateSortByLevel = updateGroupByLevel,
        onDismissRequest = { updateGroupCharactersBottomSheetVisible(false) }
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searching by rememberSaveable {
        mutableStateOf(false)
    }

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    val filteredCharacter by remember(searchText, charactersWithItems) {
        derivedStateOf {
            charactersWithItems.filter {
                searchText.isBlank() || searchText.lowercase() in it.character.name.lowercase()
            }
                .toImmutableList()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchTopAppBar(
                onSearchText = { searchText = it },
                showTextField = searching,
                actions = {
                    IconButton(
                        onClick = {
                            updateExpandableBottomBarProgress(
                                if (expandablebBottomBarProgress == SheetValue.Expanded)
                                    SheetValue.Hidden
                                else
                                    SheetValue.Expanded
                            )
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
    ) { paddingValues ->
        if (characterPrefs.cardType == CardType.List) {
            ScrollbarLazyColumn(
                Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = if (shouldShowBottomBar)
                            LocalNavBarHeight.current
                        else
                            0.dp
                    )
            ) {
                items(
                    items = filteredCharacter,
                    key = { item: CharacterWithItems -> item.character.name }
                ) {item ->
                    CharacterListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                if (shouldShowBottomBar)
                                    (LocalConfiguration.current.screenHeightDp * .1f).dp
                                else
                                   (LocalConfiguration.current.screenHeightDp * .3f).dp
                            )
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
                            .conditional(characterPrefs.animateCardPlacement) {
                                animateItemPlacement()
                            },
                        character = item.character
                    )
                }
            }
        } else {
            val maxHeight =  (LocalConfiguration.current.screenHeightDp * 0.45f).dp
            ScrollbarLazyGrid(
                columns = GridCells.Fixed(characterPrefs.gridCells),
                cellCount = characterPrefs.gridCells,
                Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = if (shouldShowBottomBar)
                            LocalNavBarHeight.current
                        else
                            0.dp
                    )
            ) {
                items(
                    items = filteredCharacter,
                    key = { item: CharacterWithItems -> item.character.name }
                ) { item ->
                    when (characterPrefs.cardType) {
                        CardType.Full,
                        CardType.SemiCompact -> SemiCompactCharacterCard(
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth()
                                .conditional(characterPrefs.animateCardPlacement) {
                                    animateItemPlacement()
                                }
                                .conditional(
                                    !shouldShowBottomBar
                                ) {
                                    heightIn(min = 0.dp, max = maxHeight)
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
                                .conditional(characterPrefs.animateCardPlacement) {
                                    animateItemPlacement()
                                }
                                .conditional(
                                    !shouldShowBottomBar
                                ) {
                                    heightIn(min = 0.dp, max = maxHeight)
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
                                .conditional(characterPrefs.animateCardPlacement) {
                                    animateItemPlacement()
                                }
                                .conditional(
                                    !shouldShowBottomBar
                                ) {
                                    heightIn(min = 0.dp, max = maxHeight)
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