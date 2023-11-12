package io.silv.hsrdmgcalc.ui.screens.add_light_cone

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.LocalNavBarHeight
import io.silv.hsrdmgcalc.R
import io.silv.hsrdmgcalc.data.HonkaiConstants
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.composables.LightConeIcon
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.PathIcon
import io.silv.hsrdmgcalc.ui.composables.ScrollbarLazyColumn
import io.silv.hsrdmgcalc.ui.composables.pathFilterRow
import io.silv.hsrdmgcalc.ui.conditional
import io.silv.hsrdmgcalc.ui.previews.DevicePreviews
import io.silv.hsrdmgcalc.ui.theme.HsrDmgCalcTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddLightConeScreen(
    appState: AppState,
    navigateBack: (Triple<String,Int, Int>?) -> Unit,
    viewModel: AddLightConeViewModel = koinViewModel(),
) {

    SideEffect { appState.clearDraggableBottomBarContent() }

    AddLightConeScreenContent(
        navigateBack = { navigateBack(null) },
        superimpose = 1,
        level = 1,
        state = viewModel.state,
        addLightCone = { name, level, superimpose ->
            navigateBack(Triple(name, level, superimpose))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SelectLightConeSearch(
    state: LightConeSearchStateHolder,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    navigateBack: () -> Unit,
    onLightConeSelected: (name: String) -> Unit,
) {

    val filters by state.filters.collectAsState()

    SearchBar(
        query = state.query,
        onQueryChange = state::updateQuery,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                animateDpAsState(
                    targetValue = if (active) 0.dp else 12.dp,
                    label = "search-bar-padding"
                )
                    .value
            ),
        onSearch = {},
        placeholder = {
            Text("Search light cones...")
        },
        active = active,
        onActiveChange = onActiveChange,
        leadingIcon = {
            IconButton(
                onClick = {
                    if (active) {
                        onActiveChange(false)
                    } else {
                        navigateBack()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    ) {
        ScrollbarLazyColumn(
            modifier = Modifier
                .padding(bottom = LocalNavBarHeight.current)
        ) {
            item(key = "path-filter") {
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    pathFilterRow(
                        startSelected = filters.pathFilter,
                        onPathSelected = state::pathClicked
                    )
                }
            }
            item(key = "check-box-filters") {
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    CheckBoxWithLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                        label = "Show five star only",
                        checked = filters.fiveStarOnly,
                        onCheckChanged = { state.showFiveStarOnlyClicked() }
                    )
                    CheckBoxWithLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                        label = "Hide 3 star",
                        checked = filters.hideThreeStar,
                        onCheckChanged = { state.hide3StarClicked() }
                    )
                }
            }
            if (state.searching) {
                item(key = "searching") {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(40.dp)
                    ) {
                        CircularProgressIndicator(
                            Modifier
                                .size(32.dp)
                                .align(Alignment.Center))
                    }
                }
            }
            items(
                items = state.filteredLightCones,
                key = { it.first }
            ) {(name, path) ->
                val starCount = remember(name) { HonkaiConstants.lightConeStars(name) }

                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                color = when (starCount) {
                                    4 -> Color(0xff855DAF)
                                    5 -> Color(0xffe6ac54)
                                    else -> Color(0xFF44ABD1)
                                }
                            )
                        ) {
                            onLightConeSelected(name)
                        }
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LightConeIcon(
                        name = name,
                        modifier = Modifier.height(90.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = formatText(text = name),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        if (starCount != 3) {
                            Image(
                                painter = painterResource(
                                    id = when(starCount) {
                                        5 -> R.drawable.honkai_5_star
                                        4 -> R.drawable.honkai_4_star
                                        else -> R.drawable.honkai_4_star
                                    }
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .offset(x = -((5 - starCount) * 10).dp)
                            )
                        }
                    }
                    PathIcon(
                        path = path,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(22.dp))
                }
            }
        }
    }
}

@Composable
private fun BackGroundGradient(
    starCount: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(
            when (starCount) {
                5 ->  Brush.verticalGradient(
                    listOf(Color(0xff695453), Color(0xffe6ac54))
                )
                4 ->  Brush.verticalGradient(
                    listOf(Color(0xff424369), Color(0xff855DAF))
                )
                else -> Brush.verticalGradient(
                    listOf(Color(0xFF2C2D58), Color(0xFF44ABD1))
                )
            }
        )
    )
}


@Composable
private fun CheckBoxWithLabel(
    modifier: Modifier,
    label: String,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckChanged,
            enabled = true
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLightConeScreenContent(
    navigateBack: () -> Unit,
    state: LightConeSearchStateHolder,
    superimpose: Int,
    level: Int,
    addLightCone: (name: String, level: Int, superimpose: Int) -> Unit,
) {

    var active by rememberSaveable {
        mutableStateOf(false)
    }

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
                    active = false
                }
            )
        },
        floatingActionButton = {

            val startCount by remember {
                derivedStateOf {
                    selectedLightCone?.let {
                        HonkaiConstants.lightConeStars(it)
                    }
                }
            }

            FloatingActionButton(
                containerColor = animateColorAsState(
                    targetValue =  when (startCount) {
                            5 ->  Color(0xffe6ac54)
                            4 ->  Color(0xff855DAF)
                            3 -> Color(0xFF44ABD1)
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    },
                    label = ""
                ).value,
                onClick = {
                    selectedLightCone?.let {
                        addLightCone(it, level,  superimpose)
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
                clearSelection = { selectedLightCone = null },
                onEdit = { active = true }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedLightCone(
    modifier: Modifier = Modifier,
    name: String?,
    borderColor: Color =  MaterialTheme.colorScheme.primary,
    clearSelection: () -> Unit,
    onEdit: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(10),
        onClick = onEdit,
        modifier = modifier
            .conditional(name == null) {
                border(1.dp, borderColor, RoundedCornerShape(10))
            }
    ) {
        AnimatedContent(
            targetState = name,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            modifier = Modifier.fillMaxSize(),
            label = ""
        ) {
            if (it != null) {
                Box(Modifier.fillMaxSize()) {
                    BackGroundGradient(
                        starCount = remember(it) { HonkaiConstants.lightConeStars(it) },
                        modifier = Modifier.matchParentSize()
                    )
                    LightConeIcon(
                        name = it,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        contentScale = ContentScale.FillWidth
                    )
                    PathIcon(
                        path = Path.Abundance,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(42.dp)
                            .align(Alignment.TopStart)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.DarkGray.copy(alpha = 0.6f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = formatText(text = it),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                    IconButton(
                        onClick = clearSelection,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Box(Modifier.weight(1f, true), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Add light cone",
                        style = MaterialTheme.typography.labelLarge
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
            superimpose = 1,
            level = 1,
            addLightCone = { name, level, superimpose ->  },
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
            superimpose = 1,
            level = 1,
            addLightCone = { name, level, superimpose ->  },
            navigateBack = {},
            state = LightConeSearchStateHolder(scope = rememberCoroutineScope())
        )
    }
}

@Composable
private fun formatText(text: String) = remember(text) {
        buildString {
            for((i, c) in text.withIndex()) {
                if (i != 0 && c.isUpperCase() || c.isDigit()) {
                    append(' ')
                }
                append(c)
            }
        }
}