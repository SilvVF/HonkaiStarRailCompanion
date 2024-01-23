package io.silv.hsrdmgcalc.ui.screens.add_light_cone.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.silv.data.constants.HonkaiUtils
import io.silv.hsrdmgcalc.ui.composables.LightConeIcon
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.PathIcon
import io.silv.hsrdmgcalc.ui.conditional

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
                        starCount = remember(it) { HonkaiUtils.lightConeStars(it) },
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