package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.R
import io.silv.data.constants.CharacterStats
import io.silv.hsrdmgcalc.scopeNotNull
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.UiCharacter
import io.silv.hsrdmgcalc.ui.UiRelic
import io.silv.hsrdmgcalc.ui.composables.IconButtonRippleColor
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.LevelSelector
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.rememberLevelSelectorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetails(
    appState: AppState,
    viewModel: CharacterDetailsViewModel = koinViewModel()
) {
    SideEffect { appState.clearDraggableBottomBarContent() }

    when (val state = viewModel.characterDetailsState.collectAsStateWithLifecycle().value) {
        CharacterDetailsState.Loading -> Unit
        is CharacterDetailsState.Success -> CharacterDetailSuccessScreen(
            characterDetailsState = state,
            updateLevel = viewModel::updateLevel,
            updateCharacterOwned = viewModel::updateCharacterOwned
        )
    }
}

@Composable
fun CharacterDetailSuccessScreen(
    characterDetailsState: CharacterDetailsState.Success,
    updateLevel: (maxLevel: Int, level: Int) -> Unit,
    updateCharacterOwned: (owned: Boolean) -> Unit
) {
    val character = characterDetailsState.character

    CollapsingToolbarLayout(
        character = character,
    ) {
        Column(
            Modifier
                .heightIn(LocalConfiguration.current.screenHeightDp.dp)
                .padding(top = 32.dp)
        ) {
            MarkOwnedCheckBox(
                owned = character.owned,
                onCheckChanged = updateCharacterOwned,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                checkedColor = character.type.color
            )
            CharacterLevelDisplay(
                character = character,
                updateLevel = updateLevel
            )
            CharacterBaseStats(
                characterColor = character.type.color,
                baseStats = characterDetailsState.baseStats,
                calcInfo = characterDetailsState.relicCalcInfo
            )
            CharacterRelics(characterDetailsState.relics)
        }
    }
}

@Composable
fun CharacterRelics(
    relics: ImmutableList<UiRelic>
) {
    relics.fastForEach {

    }
}


@Composable
fun MarkOwnedCheckBox(
    owned: Boolean,
    checkedColor: Color,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = owned,
            onCheckedChange = onCheckChanged,
            enabled = true,
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Character is acquired",
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Composable
fun CharacterBaseStats(
    characterColor: Color,
    baseStats: CharacterStats.BaseStats?,
    calcInfo: CharacterStats.CalcInfo?,
) {
    Column(
        Modifier.padding(horizontal = 18.dp)
    ) {
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider(color = characterColor)
        scopeNotNull(calcInfo, baseStats) { info, base ->
            val (stats, hp, atk, def, spd) = info
            StatsItem(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_hp),
                        contentDescription = "Health",
                        modifier = Modifier.size(32.dp)
                    )
                },
                tag = "HP",
                tooltipText = "Char. HP ${base.hp} * (1 + Rel. HP ${hp.pct}) + Rel. HP ${hp.additive}",
                statValue = remember(baseStats) { stats.hp.toFloat() },
                rippleColor = characterColor
            )
            StatsItem(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_atk),
                        contentDescription = "attack",
                        modifier = Modifier.size(32.dp)
                    )
                },
                tooltipText = "Char. ATK ${base.atk} * (1 + Rel. ATK ${atk.pct}) + Rel. ATK ${atk.additive}",
                tag = "ATK",
                statValue = remember(baseStats) { stats.atk.toFloat() },
                rippleColor = characterColor
            )
            StatsItem(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_def),
                        contentDescription = "defense",
                        modifier = Modifier.size(32.dp)
                    )
                },
                tag = "DEF",
                tooltipText = "Char. DEF ${base.def} * (1 + Rel. DEF ${def.pct}) + Rel. DEF ${def.additive}",
                statValue = remember(baseStats) { stats.def.toFloat() },
                rippleColor = characterColor
            )
            StatsItem(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_spd),
                        contentDescription = "speed",
                        modifier = Modifier.size(32.dp)
                    )
                },
                tag = "SPD",
                tooltipText = "Char. SPD ${base.spd} * (1 + Rel. SPD ${spd.pct}) + Rel. SPD ${spd.additive}",
                statValue = remember(baseStats) { stats.spd.toFloat() },
                rippleColor = characterColor
            )
        }
            ?: Text(text = "stat calculation for character not yet implemented")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    tag: String,
    tooltipText: String? = null,
    statValue: Float,
    rippleColor: Color,
) {
    val tooltipState = remember { RichTooltipState() }
    val scope = rememberCoroutineScope()

    fun showTooltip() {
        scope.launch { tooltipState.show() }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(1f)
        ) {
            icon()
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = tag, style = MaterialTheme.typography.labelMedium)
        }


        RichTooltipBox(
            tooltipState = tooltipState,
            action = {
                CompositionLocalProvider(LocalContentColor provides rippleColor) {
                    Text(
                        text = "Calculation info",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            text = {
                Text(
                    text = tooltipText ?: "not implemented",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = remember(statValue) { statValue.toString() })
                Spacer(modifier = Modifier.width(4.dp))
                IconButtonRippleColor(
                    rippleColor = rippleColor,
                    onClick = ::showTooltip,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "calculation info"
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterLevelDisplay(
    character: UiCharacter,
    updateLevel: (maxLevel: Int, level: Int) -> Unit
) {
    var editing by rememberSaveable { mutableStateOf(false) }

    AnimatedContent(
        targetState = editing,
        label = ""
    ) { isEditing ->
        if (isEditing) {

            val levelSelectorState = rememberLevelSelectorState(
                remember { character.level },
                remember { character.maxLevel }
            )

            LaunchedEffect(levelSelectorState) {
                snapshotFlow { levelSelectorState.maxLevel to levelSelectorState.level }
                    .collectLatest { (maxLevel, level) ->
                        updateLevel(maxLevel, level)
                    }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LevelSelector(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .weight(1f),
                    state = levelSelectorState,
                    maxLevelTextFieldColor = character.type.color,
                    levelTextFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = character.type.color,
                        unfocusedLabelColor = character.type.color,
                        unfocusedBorderColor = character.type.color,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedBorderColor = character.type.color,
                        cursorColor = character.type.color,
                    )
                )
                IconButtonRippleColor(
                    onClick = { editing = !editing },
                    rippleColor = character.type.color
                ) {
                    Icon(
                        imageVector = Icons.Filled.EditOff,
                        contentDescription = null,
                        tint = character.type.color
                    )
                }
            }
        } else {
            Column(Modifier.padding(horizontal = 18.dp)) {
                Text(
                    text = "Levels",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Divider(color = character.type.color)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.labelLarge.toSpanStyle()
                                ) {
                                    append("Character level:   ")
                                }
                                withStyle(
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ).toSpanStyle()
                                ) {
                                    append("${character.level}")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.labelLarge.toSpanStyle()
                                ) {
                                    append("Max level:".padEnd(24, ' '))
                                }
                                withStyle(
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ).toSpanStyle()
                                ) {
                                    append("${character.maxLevel}")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.labelLarge.toSpanStyle()
                                ) {
                                    append("Ascension:".padEnd(22, ' '))
                                }
                                withStyle(
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ).toSpanStyle()
                                ) {
                                    append("${character.ascension}")
                                }
                            }
                        )
                    }
                    IconButtonRippleColor(
                        onClick = { editing = !editing },
                        modifier = Modifier.padding(end = 22.dp),
                        rippleColor = character.type.color
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = character.type.color
                        )
                    }
                }
            }
        }
    }
}

