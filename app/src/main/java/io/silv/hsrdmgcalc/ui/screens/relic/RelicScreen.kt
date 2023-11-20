package io.silv.hsrdmgcalc.ui.screens.relic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.composables.LaunchedOnSelectedDestinationClick
import io.silv.hsrdmgcalc.ui.composables.SearchTopAppBar
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.navigation.NavResultCallback
import io.silv.hsrdmgcalc.ui.screens.add_relic.RelicInfo
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelicScreen(
    appState: AppState,
    viewModel: RelicViewModel = koinViewModel(),
    navigateToAddRelicForResult: (NavResultCallback<RelicInfo?>) -> Unit
) {

    LaunchedOnSelectedDestinationClick(
        appState = appState,
        destination = HsrDestination.LightCone
    ) {
        appState.bottomBarState.toggleProgress()
    }

    RelicScreenContent(
        toggleExpandableBottomBarContent = {
            appState.bottomBarState.progress =
                if (appState.bottomBarState.progress == SheetValue.Expanded)
                    SheetValue.Hidden
                else
                    SheetValue.Expanded
        },
        onAddRelicClick = {
            navigateToAddRelicForResult {

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RelicScreenContent(
    toggleExpandableBottomBarContent: () -> Unit,
    onAddRelicClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searching by rememberSaveable { mutableStateOf(false) }

    var searchText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = LocalNavBarHeight.current),
        topBar = {
            SearchTopAppBar(
                onSearchText = { searchText = it },
                showTextField = searching,
                actions = {
                    IconButton(
                        onClick = onAddRelicClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircleOutline,
                            null
                        )
                    }
                    IconButton(
                        onClick = {
                           toggleExpandableBottomBarContent()
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
        Box(modifier = Modifier.padding(paddingValues)) {
            Text(text = "Relic")
        }
    }
}