@file:OptIn(ExperimentalMaterial3Api::class)

package io.silv.hsrdmgcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
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
