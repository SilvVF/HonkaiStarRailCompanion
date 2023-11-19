package io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.data.CharacterStats

class LevelSelectorState(
    initialFirstMax: Boolean = true,
    initialLevel: Int = 1,
    initialLvlText: String = ""
) {
    private val levelRanges = CharacterStats.levelRanges

    var level by mutableIntStateOf(initialLevel)
        private set

    var lvlText by mutableStateOf(initialLvlText)
        private set

    var firstMax by mutableStateOf(initialFirstMax)
        private set

    val hasOtherMax by derivedStateOf {
        levelRanges.filter { range -> range.contains(level) }.size > 1
    }

    val maxLevel by derivedStateOf {

        val maxes = levelRanges.filter { range -> range.contains(level) }

        if (firstMax) {
            maxes.firstOrNull()?.last ?: 20
        } else {
            maxes.lastOrNull()?.last ?: 20
        }
    }

    val maxLvlText by derivedStateOf {
        "/ $maxLevel"
    }

    fun toggleMax() {
        if (hasOtherMax)
            firstMax = !firstMax
    }

    fun changeLevel(input: String) {
        if (input.isBlank()) {
            level = 1
            lvlText = input
        } else {
            input.toIntOrNull()?.let { num ->
                level = num.coerceIn(1.. 90)
                lvlText = level.toString()
            }
        }
    }

    fun reset() {
        level = 1
        firstMax = true
        lvlText = ""
    }
}

@Composable
fun rememberLevelSelectorState(
    initialLevel: Int = 1,
    initialMaxLevel: Int = 20
): LevelSelectorState = rememberSaveable(
    saver = Saver(
        save = {
            arrayOf(it.level, it.firstMax, it.lvlText)
        },
        restore = { saved ->
            LevelSelectorState(
                initialLevel = saved[0] as? Int ?: 1,
                initialFirstMax = saved[1] as? Boolean ?: true,
                initialLvlText = saved[2] as? String ?: ""
            )
        }
    )
) {
    LevelSelectorState(
        initialFirstMax = CharacterStats.levelRanges
            .filter { initialLevel in it }
            .takeIf { it.size > 1 }
            ?.let { it.first().last == initialMaxLevel }
            ?: true,
        initialLevel = initialLevel,
        initialLvlText = initialLevel.toString()
    )
}

@Composable
fun LevelSelector(
    modifier: Modifier = Modifier,
    state: LevelSelectorState
) {
    Row(modifier) {
        OutlinedTextField(
            value = state.lvlText,
            label = { Text("level") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { text ->
                state.changeLevel(text)
            },
            modifier = Modifier.weight(0.6f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            val enabledColor = MaterialTheme.colorScheme.primary
            val disabledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            val textFieldColor = remember(state.hasOtherMax) {
                if (state.hasOtherMax) { enabledColor }
                else { disabledColor }
            }
            OutlinedTextField(
                readOnly = true,
                enabled = false,
                value = state.maxLvlText,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { state.toggleMax() },
                onValueChange = {},
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor =textFieldColor,
                    disabledTextColor = textFieldColor,
                    disabledLabelColor = textFieldColor
                ),
                label = { Text(text = "max level") }
            )
        }
    }
}