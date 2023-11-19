package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonRippleColor(
    modifier: Modifier = Modifier,
    rippleColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(40.0.dp)
            .clip(CircleShape)
            .background(color = Color.Transparent)
            .clickable(
                onClick = onClick,
                enabled = true,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = 40.dp / 2,
                    color = rippleColor
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
