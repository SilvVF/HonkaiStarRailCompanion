package io.silv.hsrdmgcalc.ui.screens.add_light_cone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.data.HonkaiUtils
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.previews.DevicePreviews
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.LevelSelector
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.SelectLightConeSearch
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.SelectedLightCone
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.rememberLevelSelectorState
import io.silv.hsrdmgcalc.ui.theme.HsrDmgCalcTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddLightConeScreen(
    appState: AppState,
    navigateBack: (LightConeInfo?) -> Unit,
    viewModel: AddLightConeViewModel = koinViewModel(),
) {

    SideEffect { appState.clearDraggableBottomBarContent() }

    AddLightConeScreenContent(
        navigateBack = { navigateBack(null) },
        state = viewModel.state
    ) { name, level, maxLevel, superimpose ->
        navigateBack(LightConeInfo(key = name, superimpose, level, maxLevel))
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLightConeScreenContent(
    navigateBack: () -> Unit,
    state: LightConeSearchStateHolder,
    addLightCone: (name: String, level: Int, maxLevel: Int, superimpose: Int) -> Unit,
) {

    var active by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedSuperimpose by rememberSaveable {
        mutableIntStateOf(1)
    }

    val levelSelectorState = rememberLevelSelectorState()

    var selectedLightCone by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        topBar = {
            SelectLightConeSearch(
                active = active,
                state = state,
                onActiveChange = { active = it },
                navigateBack = navigateBack,
                onLightConeSelected = { lightCone ->
                    selectedLightCone = lightCone
                    selectedSuperimpose = 1
                    levelSelectorState.reset()
                    active = false
                }
            )
        },
        floatingActionButton = {

            val starCount by remember {
                derivedStateOf {
                    selectedLightCone?.let {
                        HonkaiUtils.lightConeStars(it)
                    }
                }
            }

            FloatingActionButton(
                containerColor = animateColorAsState(
                    targetValue =  when (starCount) {
                            5 ->  Color(0xffe6ac54)
                            4 ->  Color(0xff855DAF)
                            3 -> Color(0xFF44ABD1)
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    },
                    label = ""
                ).value,
                onClick = {
                    selectedLightCone?.let { name ->
                        addLightCone(name, levelSelectorState.level, levelSelectorState.maxLevel, selectedSuperimpose)
                    } ?: run {
                        active = true
                    }
                },
                modifier = Modifier
                    .offset(y = (-100).dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectedLightCone(
                name = selectedLightCone,
                modifier = Modifier
                    .padding(22.dp)
                    .fillMaxWidth()
                    .height(
                        (LocalConfiguration.current.screenHeightDp * 0.5f).dp
                    ),
                clearSelection = {
                    selectedLightCone = null
                    selectedSuperimpose = 1
                    levelSelectorState.reset()
                },
                onEdit = { active = true }
            )
            AnimatedVisibility(visible = selectedLightCone != null) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    SuperImposeSelector(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        superimpose = selectedSuperimpose,
                        onSuperimposeChange = { superimpose ->
                            selectedSuperimpose = superimpose
                        }
                    )
                    LevelSelector(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        state = levelSelectorState
                    )
                }
            }
        }
    }
}




@DevicePreviews
@Composable
fun DevicePreviewsAddLightConeScreenContent() {
    HsrDmgCalcTheme {
        AddLightConeScreenContent(
            addLightCone = { name, level, max, superimpose ->  },
            navigateBack = {},
            state = LightConeSearchStateHolder(scope = rememberCoroutineScope())
        )
    }
}

@Preview
@Composable
fun PreviewAddLightConeScreenContent() {
    HsrDmgCalcTheme {
        AddLightConeScreenContent(
            addLightCone = { name, level, max, superimpose ->  },
            navigateBack = {},
            state = LightConeSearchStateHolder(scope = rememberCoroutineScope())
        )
    }
}
