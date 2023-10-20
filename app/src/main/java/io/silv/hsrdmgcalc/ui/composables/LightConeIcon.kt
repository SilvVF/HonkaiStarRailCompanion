package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import io.silv.hsrdmgcalc.R
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LightConeIcon(
    name: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {

    Image(
        painter = lightConePainterResource(name = name),
        contentDescription = name,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter,
    )
}

val lightCones = persistentListOf(
    "adversarial",
    "amber",
    "arrows",
    "asecretvow"
)

@Composable
private fun lightConePainterResource(name: String): Painter {

    return painterResource(
        id = when (name) {
            "adversarial" -> R.drawable.lightconeadversarial
            "amber" -> R.drawable.lightconeamber
            "arrows" -> R.drawable.lightconearrows
            "asecretvow" -> R.drawable.a_secret_vow_sm
            else -> R.drawable.light_cone_selected
        }
    )
}