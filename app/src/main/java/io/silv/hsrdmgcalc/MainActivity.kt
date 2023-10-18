@file:OptIn(ExperimentalMaterial3Api::class)

package io.silv.hsrdmgcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.More
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import io.silv.hsrdmgcalc.ui.Navigation
import io.silv.hsrdmgcalc.ui.composables.BottomBarWithDraggableContent
import io.silv.hsrdmgcalc.ui.rememberAppState
import io.silv.hsrdmgcalc.ui.theme.HsrDmgCalcTheme

val LocalPaddingValues = compositionLocalOf<PaddingValues> { error("not provided yet") }
val LocalNavBarHeight = compositionLocalOf<Dp> { error("not provided yet") }

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HsrDmgCalcTheme {

                val navBarHeight = 75.dp


                val appState = rememberAppState(
                    navController = rememberNavController(),
                )

                Scaffold(
                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets
                        .exclude(WindowInsets.statusBars),
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(appState.topAppBar.first.nestedScrollConnection),
                    topBar = {
                        appState.topAppBar.second()
                    },
                    bottomBar = {
                        BottomBarWithDraggableContent(
                            navBarHeight = navBarHeight,
                            appState = appState,
                        )
                    }
                ) { paddingValues ->
                    CompositionLocalProvider(
                        LocalPaddingValues provides paddingValues,
                        LocalNavBarHeight provides navBarHeight
                    ) {
                        Navigation(appState = appState)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandedBottomBarContent(
    showDisplayOptions: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(6.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.More,
                contentDescription = "Group characters by...",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(180f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Group characters by...")
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { showDisplayOptions() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Options",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Display options")
        }
    }
}
