package io.silv.hsrdmgcalc.ui.character

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import io.silv.data.constants.HonkaiConstants

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
                    AsyncImage(
                        model = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(280.dp),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )
                }
            }
        }
    }
}