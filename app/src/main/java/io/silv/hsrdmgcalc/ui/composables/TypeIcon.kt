package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import io.silv.hsrdmgcalc.R

@Composable
fun TypeIcon(
    type: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = typePainterResource(type = type),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

@Composable
private fun typePainterResource(type: String): Painter {

    val typeLower = remember { type.lowercase() }

    return painterResource(
        when(typeLower) {
            "physical" -> R.drawable.type_physical
            "fire" -> R.drawable.type_fire
            "ice" -> R.drawable.type_ice
            "lightning" -> R.drawable.type_lightning
            "wind" -> R.drawable.type_wind
            "quantum" -> R.drawable.type_quantum
            "imaginary" -> R.drawable.type_imaginary
            else -> R.drawable.type_physical
        }
    )
}
