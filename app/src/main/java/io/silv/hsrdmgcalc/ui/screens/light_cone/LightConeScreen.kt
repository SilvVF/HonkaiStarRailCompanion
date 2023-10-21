package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.LocalPaddingValues
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.DisplayOptionsBottomSheet
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.LightConeIcon
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.SearchTopAppBar
import io.silv.hsrdmgcalc.ui.composables.UpdateBottomAppBar
import io.silv.hsrdmgcalc.ui.composables.UpdateTopBar
import io.silv.hsrdmgcalc.ui.composables.pathFilterRow
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun LightConeScreen(
    appState: AppState,
    viewModel: LightConeViewModel = koinViewModel(),
    onLightConeClick: (id: String) -> Unit
) {

    val lightCones by viewModel.lightCones.collectAsStateWithLifecycle()

    LightConeScreenContent(
        appState = appState,
        lightCones = lightCones,
        onLightConeClick = onLightConeClick,
        onGridSizeSelected = viewModel::updateGridCellCount,
        onAnimatePlacementChanged = viewModel::updateAnimateCardPlacement,
        onCardTypeSelected = viewModel::updateCardType,
        onPathFilterSelected = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LightConeScreenContent(
    appState: AppState,
    onLightConeClick: (id: String) -> Unit,
    lightCones: ImmutableList<UiLightCone>,

    onGridSizeSelected: (Int) -> Unit,
    onAnimatePlacementChanged: (Boolean) -> Unit,
    onCardTypeSelected: (CardType) -> Unit,

    onPathFilterSelected: (Path?) -> Unit
) {
    val displayPrefs = appState.displayPrefs.lightConePrefs

    var displayOptionsVisible by rememberSaveable {
        mutableStateOf(false)
    }
    
    DisplayOptionsBottomSheet(
        visible = displayOptionsVisible,
        prefs = displayPrefs,
        optionsTitle = {
            Text(
                text = "Light cone options",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(12.dp)
            )
        },
        onDismissRequest = { displayOptionsVisible = false },
        onGridSizeSelected = onGridSizeSelected,
        onAnimatePlacementChanged = onAnimatePlacementChanged,
        onCardTypeSelected = onCardTypeSelected
    )

    LaunchedOnSelectedDestinationClick(
        appState = appState,
        destination = HsrDestination.LightCone
    ) {
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
            LazyRow(Modifier.padding(vertical = 12.dp)) {
                pathFilterRow { path ->
                    onPathFilterSelected(path)
                }
            }
        }
    ) {
        LightConeExpandedBottomBarContent {
            displayOptionsVisible = true
        }
    }


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
        if (displayPrefs.cardType == CardType.List) {
            LazyColumn {
                items(lightCones) {lightCone ->

                    LightConeIcon(name = lightCone.name)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(displayPrefs.gridCells)
            ) {
                items(lightCones) {lightCone ->

                    LightConeIcon(name = lightCone.name, Modifier.size(200.dp))
                }
            }
        }
    }
}