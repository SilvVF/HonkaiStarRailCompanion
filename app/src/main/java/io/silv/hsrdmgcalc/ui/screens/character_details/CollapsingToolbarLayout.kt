package io.silv.hsrdmgcalc.ui.screens.character_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import io.silv.hsrdmgcalc.ui.UiCharacter
import io.silv.hsrdmgcalc.ui.composables.CharacterSplashArt
import io.silv.hsrdmgcalc.ui.composables.PathIcon
import io.silv.hsrdmgcalc.ui.composables.TypeIcon

private val headerHeight = 250.dp
private val toolbarHeight = 56.dp

private val paddingMedium = 16.dp

private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp

private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f


// Example code found in this article from ProAndroidDev
// https://proandroiddev.com/collapsing-toolbar-with-parallax-effect-and-curve-motion-in-jetpack-compose-9ed1c3c0393f
// Jetsnack sample
// https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/snackdetail/SnackDetail.kt#L274

@Composable
fun CollapsingToolbarLayout(
    character: UiCharacter,
    modifier: Modifier = Modifier,
    body: @Composable ColumnScope.() -> Unit
) {

    val scroll: ScrollState = rememberScrollState(0)
    val localDensity = LocalDensity.current

    val headerHeightPx = with(localDensity) { headerHeight.toPx() }
    val toolbarHeightPx = with(localDensity) { toolbarHeight.toPx() }

    Box(modifier = modifier) {
        Header(
            scroll = scroll,
            headerHeightPx = headerHeightPx,
            character = character,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(headerHeight))
            body()
        }

        val toolbarBottom by remember {
            mutableFloatStateOf(headerHeightPx - toolbarHeightPx)
        }

        val showToolbar by remember {
            derivedStateOf {
                scroll.value >= toolbarBottom
            }
        }

        Toolbar(
            character = character,
            showToolbar = showToolbar
        )
        Title(
            scroll = scroll,
            text = formatText(text = character.name),
            modifier = Modifier.statusBarsPadding(),
            showFullText = !showToolbar
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(
    character: UiCharacter,
    showToolbar: Boolean,
) {
    AnimatedVisibility(
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(character.type.color, MaterialTheme.colorScheme.surface)
                )
            ),
            navigationIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            title = {},
            actions = {
                TypeIcon(
                    type = character.type.name,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(36.dp)
                )
                PathIcon(
                    path = character.path,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(36.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
        )
    }
}

@Composable
private fun Title(
    text: String,
    scroll: ScrollState,
    modifier: Modifier = Modifier,
    showFullText: Boolean
) {
    var titleHeightPx by remember { mutableFloatStateOf(0f) }
    var titleWidthPx by remember { mutableFloatStateOf(0f) }

    Text(
        text = formatText(text = text),
        fontSize = 32.sp,
        maxLines = if(showFullText) 4 else 2,
        lineHeight = 32.sp,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth(0.7f)
            .graphicsLayer {
                val collapseRange: Float = (headerHeight.toPx() - toolbarHeight.toPx())
                val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

                val scaleXY = lerp(
                    titleFontScaleStart.dp,
                    titleFontScaleEnd.dp,
                    collapseFraction
                )

                val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                val titleYFirstInterpolatedPoint = lerp(
                    headerHeight - titleHeightPx.toDp() - paddingMedium,
                    headerHeight / 2,
                    collapseFraction
                )

                val titleXFirstInterpolatedPoint = lerp(
                    titlePaddingStart,
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    collapseFraction
                )

                val titleYSecondInterpolatedPoint = lerp(
                    headerHeight / 2,
                    toolbarHeight / 2 - titleHeightPx.toDp() / 2,
                    collapseFraction
                )

                val titleXSecondInterpolatedPoint = lerp(
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    titlePaddingEnd - titleExtraStartPadding,
                    collapseFraction
                )

                val titleY = lerp(
                    titleYFirstInterpolatedPoint,
                    titleYSecondInterpolatedPoint,
                    collapseFraction
                )

                val titleX = lerp(
                    titleXFirstInterpolatedPoint,
                    titleXSecondInterpolatedPoint,
                    collapseFraction
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()
                scaleX = scaleXY.value
                scaleY = scaleXY.value
            }
            .onGloballyPositioned {
                titleHeightPx = it.size.height.toFloat()
                titleWidthPx = it.size.width.toFloat()
            }
    )
}

@Composable
private fun Header(
    character: UiCharacter,
    scroll: ScrollState,
    headerHeightPx: Float,
    modifier: Modifier = Modifier,
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .height(headerHeight)
        .graphicsLayer {
            translationY = - scroll.value.toFloat() / 2f // Parallax effect
            alpha = (-1f / headerHeightPx) * scroll.value + 1
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(character.type.color, Color.Transparent),
                        startY = 3 * headerHeightPx / 4 // Gradient applied to wrap the title only
                    )
//                    brush = Brush.verticalGradient(
//
//                        colors = listOf(character.type.color, MaterialTheme.colorScheme.surface),
//                        // startY = 3 * headerHeightPx / 4 // to wrap the title only
//                    )
                )
        )
        CharacterSplashArt(
            character = character.name,
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.align(Alignment.CenterEnd)
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