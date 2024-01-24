package io.silv.hsrdmgcalc.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabNavigator
import io.silv.hsrdmgcalc.ui.character.CharacterTab
import kotlinx.collections.immutable.persistentListOf

object HomeScreen: Screen {

    private val tabs = persistentListOf(
        CharacterTab
    )

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        TabNavigator(CharacterTab) { tabNavigator ->
            Scaffold(
                contentWindowInsets = WindowInsets(0),
                bottomBar = {
                    NavigationBar(
                        Modifier.fillMaxWidth()
                    ) {
                        tabs.fastForEach { tab ->
                            NavigationBarItem(
                                selected = tabNavigator.current == tab,
                                onClick = { tabNavigator.current = tab },
                                icon = {
                                    tab.options.icon?.let { painter ->
                                        Icon(
                                            painter = painter,
                                            contentDescription = tab.options.title
                                        )
                                    }
                                },
                                label = { Text(tab.options.title) },
                                alwaysShowLabel = true
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                ) {
                    CompositionLocalProvider(
                        LocalNavigator provides navigator,
                    ) {
                        CrossfadeTabTransition(tabNavigator)
                    }
                }
            }
        }
    }
}

@Composable
private fun CrossfadeTabTransition(
    tabNavigator: TabNavigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
) {
    val currentTab = tabNavigator.current

    Crossfade(
        targetState = currentTab,
        label = label
    ) { tab ->
        tabNavigator.saveableState(key = "currentTab", tab) {
            tab.Content()
        }
    }
}