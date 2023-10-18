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
fun PathIcon(
    path: Path,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = pathPainterResource(path = path.name.lowercase()),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}


//Destruction
//The Hunt
//Erudition
//Harmony
//Nihility
//Preservation
//Abundance

@Composable
private fun pathPainterResource(path: String): Painter {

    val typeLower = remember { path.lowercase() }

    return painterResource(
        when(typeLower) {
            "destruction" -> R.drawable.path_destruction
            "thehunt" -> R.drawable.path_the_hunt
            "erudition" -> R.drawable.path_erudition
            "harmony" -> R.drawable.path_harmony
            "nihility" -> R.drawable.path_nihility
            "preservation" -> R.drawable.path_preservation
            "abundance" -> R.drawable.path_abundance
            else -> R.drawable.path_destruction
        }
    )
}