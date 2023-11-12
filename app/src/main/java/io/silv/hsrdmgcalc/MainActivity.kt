@file:OptIn(ExperimentalMaterial3Api::class)

package io.silv.hsrdmgcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import io.silv.hsrdmgcalc.ui.composables.BottomBarWithDraggableContent
import io.silv.hsrdmgcalc.ui.composables.ExpandableInfoLayout
import io.silv.hsrdmgcalc.ui.navigation.HsrDestination
import io.silv.hsrdmgcalc.ui.navigation.Navigation
import io.silv.hsrdmgcalc.ui.rememberAppState
import io.silv.hsrdmgcalc.ui.theme.HsrDmgCalcTheme

val LocalPaddingValues = compositionLocalOf<PaddingValues> { error("not provided yet") }
val LocalNavBarHeight = compositionLocalOf<Dp> { error("not provided yet") }

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            HsrDmgCalcTheme {

                val navBarHeight = 75.dp
                val snackbarHostState = remember { SnackbarHostState() }

                val appState = rememberAppState(
                    navController = rememberNavController(),
                    windowSizeClass = calculateWindowSizeClass(activity = this@MainActivity),
                    snackbarHostState = snackbarHostState
                )

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.statusBars),
                    bottomBar = {
                        if (appState.shouldShowBottomBar) {
                            BottomBarWithDraggableContent(
                                navBarHeight = navBarHeight,
                                appState = appState,
                            )
                        }
                    }
                ) { paddingValues ->
                    CompositionLocalProvider(
                        LocalPaddingValues provides paddingValues,
                        LocalNavBarHeight provides if (appState.shouldShowBottomBar) navBarHeight else 0.dp
                    ) {
                        Row(Modifier.fillMaxSize()) {
                            if(appState.shouldShowNavRail) {
                                val selectedDest by appState.currentTopLevelDest.collectAsState(initial = HsrDestination.Character)
                                NavigationRail {
                                    appState.destinations.fastForEach {dest ->
                                        NavigationRailItem(
                                            selected = dest == selectedDest,
                                            onClick = {
                                                appState.handleDestinationClick(dest)
                                            },
                                            icon = {
                                                dest.icon(dest == selectedDest)
                                            })
                                    }
                                }
                            }
                            Column {
                                Box(
                                    contentAlignment = Alignment.BottomCenter,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Navigation(
                                        appState = appState
                                    )
                                    if (appState.shouldShowNavRail) {
                                        AnimatedContent(
                                            targetState = appState.draggableContent to appState.draggablePeekContent,
                                            label = "bottom-bar-content",
                                            transitionSpec = {
                                                fadeIn() togetherWith fadeOut()
                                            },
                                            modifier = Modifier.systemBarsPadding()
                                        ) { (content, peek) ->
                                            ExpandableInfoLayout(
                                                appState.bottomBarState,
                                                peekContent = {
                                                    Box { peek?.invoke() }
                                                },
                                                content = {
                                                    Box { content?.invoke() }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
