package io.silv.hsrdmgcalc.ui.character

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import io.silv.data.constants.HonkaiConstants
import io.silv.hsrdmgcalc.data.coil.toStorageItem
import io.silv.hsrdmgcalc.ui.toColor
import kotlinx.collections.immutable.persistentListOf

object CharacterTab: Tab {

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "Characters",
            icon = rememberVectorPainter(image = Icons.Default.Home)
        )

    @Composable
    override fun Content() {

        val hazeState = remember { HazeState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    // Need to make app bar transparent to see the content behind
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    title = { Text("Characters") },
                    modifier = Modifier
                        .hazeChild(state = hazeState)
                        .fillMaxWidth(),
                )
            }
        ) { paddingValues ->

            val characters = remember {
                HonkaiConstants.characters
            }

            LazyVerticalGrid(
                state = rememberLazyGridState(),
                columns = GridCells.Fixed(2),
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .haze(
                        state = hazeState,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                    ),
            ) {
                items(characters, key = { it.name }) {

                    val backgroundColor = MaterialTheme.colorScheme.background
                    val typeColor = it.type.toColor()

                    Box(
                        Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = it.toStorageItem(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(280.dp)
                                .drawBehind {
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(typeColor, backgroundColor)
                                        )
                                    )
                                },
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                        Text(
                            text = it.name,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEDE0DD),
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .drawWithCache {
                                    onDrawBehind {
                                        drawRect(
                                            brush =
                                            Brush.verticalGradient(
                                                colors =
                                                persistentListOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.7f),
                                                    Color.Black.copy(alpha = 0.9f),
                                                ),
                                            ),
                                        )
                                    }
                                }
                                .padding(top = 32.dp, bottom = 16.dp)
                                .padding(horizontal = 6.dp)
                                .align(Alignment.BottomCenter),
                        )
                    }
                }
            }
        }
    }
}