package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.silv.hsrdmgcalc.R
import io.silv.hsrdmgcalc.data.CharacterStats
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.UiCharacter
import io.silv.hsrdmgcalc.ui.composables.IconButtonRippleColor
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.LevelSelector
import io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables.rememberLevelSelectorState
import kotlinx.coroutines.flow.collectLatest
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
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
            )
            CharacterLevelDisplay(
                character = character,
                updateLevel = updateLevel
            )
            CharacterBaseStats(baseStats = characterDetailsState.baseStats)
        }
    }
}

@Composable
fun MarkOwnedCheckBox(
    owned: Boolean,
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
            enabled = true
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
    baseStats: CharacterStats.BaseStats?,
) {
    Column(
        Modifier.padding(horizontal = 18.dp)
    ) {
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider()
        baseStats?.let {
            StatsItem(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_hp),
                        contentDescription = "Health"
                    )
                },
                tag = "HP",
                statValue = remember(baseStats) { baseStats.hp.toFloat() }
            )
            StatsItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_atk),
                        contentDescription = "attack"
                    )
                },
                tag = "ATK",
                statValue = remember(baseStats) { baseStats.atk.toFloat() }
            )
            StatsItem(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_def),
                        contentDescription = "defense"
                    )
                },
                tag = "DEF",
                statValue = remember(baseStats) { baseStats.def.toFloat() }
            )
        }
            ?: Text(text = "stat calculation for character not yet implemented")
    }
}

@Composable
fun StatsItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    tag: String,
    hintText: String? = null,
    statValue: Float
) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = remember(statValue) { statValue.toString() })
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "calculation info"
            )
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
                snapshotFlow { levelSelectorState.maxLevel to levelSelectorState.level }.collectLatest { (maxLevel, level) ->
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
                    state = levelSelectorState
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(18.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleLarge.toSpanStyle()
                            ) {
                                append("Character level:   ")
                            }
                            withStyle(
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp
                                ).toSpanStyle()
                            ) {
                                append("${character.level}")
                            }
                        }
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleLarge.toSpanStyle()
                            ) {
                                append("Max level:".padEnd(24, ' '))
                            }
                            withStyle(
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp
                                ).toSpanStyle()
                            ) {
                                append("${character.maxLevel}")
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

