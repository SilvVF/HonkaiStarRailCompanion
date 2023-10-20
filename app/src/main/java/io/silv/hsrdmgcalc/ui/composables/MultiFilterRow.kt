package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEachIndexed

@Stable
@Immutable
data class TagSelection<T>(
    val item: T,
    val selected: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiFilterTag(
    modifier: Modifier = Modifier,
    tagSelections: List<TagSelection<T>>,
    shape: Shape = RoundedCornerShape(50),
    label: @Composable (item: TagSelection<T>) -> Unit,
    selectedColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onTagSelected: (item: TagSelection<T>) -> Unit
) {

    val anySelected by remember(tagSelections) {
        derivedStateOf { tagSelections.fastAny { (_, selected) -> selected } }
    }

    Row(
        modifier
            .clip(shape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                shape
            )
            .background(
                animateColorAsState(
                    targetValue = if (anySelected) {
                        selectedColor
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    label = "selected-color-anim"
                ).value
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tagSelections.fastForEachIndexed { i, selection ->
            val (_, selected) = selection
            AnimatedVisibility(
                visible = selected || !anySelected,
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                FilterChip(
                    selected = selected,
                    onClick = { onTagSelected(selection) },
                    label = {
                        label(selection)
                    },
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color.Transparent
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = selectedColor
                    ),
                    modifier = Modifier
                        .widthIn(60.dp)
                        .indication(
                            interactionSource = interactionSource,
                            indication = rememberRipple (true, Dp.Unspecified, selectedColor)
                        ),
                    shape = shape,
                )
            }
            AnimatedVisibility(visible = !anySelected && i != tagSelections.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(0.5.dp)
                        .heightIn(30.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}