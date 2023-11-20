package io.silv.hsrdmgcalc.ui.screens.light_cone

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.composables.CardType
import io.silv.hsrdmgcalc.ui.composables.DisplayOptionsBottomSheet
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.LightConeIcon
import io.silv.hsrdmgcalc.ui.composables.SearchTopAppBar
import io.silv.hsrdmgcalc.ui.composables.UpdateBottomAppBar
import io.silv.hsrdmgcalc.ui.composables.pathFilterRow
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.navigation.NavResultCallback
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.LightConeInfo
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightConeScreen(
    appState: AppState,
    viewModel: LightConeViewModel = koinViewModel(),
    navigateToAddLightConeForResult: (NavResultCallback<LightConeInfo?>) -> Unit,
    onLightConeClick: (id: String) -> Unit
) {

    val lightCones by viewModel.lightCones.collectAsStateWithLifecycle()
    val pathFilter by viewModel.pathFilter.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LightConeEvent.LightConeAdded -> {
                    appState.showSnackbar(
                        message = "Light cone added ${event.info.key}",
                        actionLabel = "undo",
                        duration = SnackbarDuration.Short,
                        onActionPerformed = {
                            viewModel.deleteLightCone(event.id)
                        }
                    )
                }
                is LightConeEvent.LightConeDeleted -> {
                    appState.showSnackbar(
                        message = "Light cone deleted ${event.info.key}",
                        actionLabel = "undo",
                        duration = SnackbarDuration.Short,
                        onActionPerformed = {
                            viewModel.addLightCone(event.info)
                        }
                    )
                }
            }
        }
    }

    LaunchedOnSelectedDestinationClick(
        appState = appState,
        destination = HsrDestination.LightCone
    ) {
        appState.bottomBarState.toggleProgress()
    }

    var displayOptionsVisible by rememberSaveable { mutableStateOf(false) }

    UpdateBottomAppBar(
        appState = appState,
        peekContent = {
            LazyRow(Modifier.padding(vertical = 12.dp)) {
                pathFilterRow(
                    startSelected = pathFilter,
                    onPathSelected = viewModel::updatePathFilter
                )
            }
        }
    ) {
        LightConeExpandedBottomBarContent(
            showGroupingOptions = {  },
            showDisplayOptions = { displayOptionsVisible = true }
        )
    }

    LightConeScreenContent(
        lightCones = lightCones,
        displayOptionsVisible = displayOptionsVisible,
        hideDisplayOptions = { displayOptionsVisible = false },
        displayPrefs = appState.displayPrefs.lightConePrefs,
        onLightConeClick = onLightConeClick,
        onGridSizeSelected = viewModel::updateGridCellCount,
        onAnimatePlacementChanged = viewModel::updateAnimateCardPlacement,
        onCardTypeSelected = viewModel::updateCardType,
        onAddLightConeClick = {
            navigateToAddLightConeForResult { info ->
                if (info != null) {
                    viewModel.addLightCone(info)
                }
            }
        },
        toggleExpandableBottomBarContent = {
            appState.bottomBarState.progress =
                if (appState.bottomBarState.progress == SheetValue.Expanded)
                    SheetValue.Hidden
                else
                    SheetValue.Expanded
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LightConeScreenContent(
    displayPrefs: DisplayPrefs.Prefs,
    toggleExpandableBottomBarContent: () -> Unit,
    onAddLightConeClick: () -> Unit,
    onLightConeClick: (id: String) -> Unit,
    lightCones: ImmutableList<UiLightCone>,
    displayOptionsVisible: Boolean,
    onGridSizeSelected: (Int) -> Unit,
    onAnimatePlacementChanged: (Boolean) -> Unit,
    onCardTypeSelected: (CardType) -> Unit,
    hideDisplayOptions: () -> Unit
) {

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
        onDismissRequest = hideDisplayOptions,
        onGridSizeSelected = onGridSizeSelected,
        onAnimatePlacementChanged = onAnimatePlacementChanged,
        onCardTypeSelected = onCardTypeSelected
    )


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searching by rememberSaveable {
        mutableStateOf(false)
    }

    var searchText by rememberSaveable {
        mutableStateOf("")
    }



    Scaffold(
        Modifier
            .fillMaxSize()
            .padding(bottom = LocalNavBarHeight.current),
        topBar = {
            SearchTopAppBar(
                onSearchText = { searchText = it },
                showTextField = searching,
                actions = {
                    IconButton(
                        onClick = onAddLightConeClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircleOutline,
                            null
                        )
                    }
                    IconButton(onClick = toggleExpandableBottomBarContent) {
                        Icon(imageVector = Icons.Filled.FilterList, null)
                    }
                },
                scrollBehavior = scrollBehavior,
                searchText = searchText,
                onSearchChanged = { searchState -> searching = searchState }
            )
        }
    ) { paddingValues ->
        if (displayPrefs.cardType == CardType.List) {
            LazyColumn(Modifier.padding(paddingValues)) {
                items(lightCones) {lightCone ->
                    Column {
                        LightConeIcon(name = lightCone.name)
                        Text(text = lightCone.name)
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(displayPrefs.gridCells),
                Modifier.padding(paddingValues)
            ) {
                items(lightCones) {lightCone ->
                    Column {
                        LightConeIcon(name = lightCone.name, Modifier.size(200.dp))
                        Text(text = lightCone.name)
                    }
                }
            }
        }
    }
}