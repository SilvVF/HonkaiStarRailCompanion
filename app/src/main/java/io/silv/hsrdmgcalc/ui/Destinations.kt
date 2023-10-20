package io.silv.hsrdmgcalc.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.R

private val iconSize: Dp = 32.dp

sealed class SubDestination(
    val route: String,
) {

    data object CharacterDetails: SubDestination("character_details/{name}") {

        const val nameArg = "name"

        fun buildRoute(name: String): String {
            return "character_details/$name"
        }
    }
}

sealed class HsrDestination(
    val icon: @Composable (selected: Boolean) -> Unit,
    val route: String,
) {
    data object Relic: HsrDestination(
        icon = { selected ->
            AnimatedContent(
                selected,
                label = "",
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) {
                if (it) {
                    Icon(
                        painterResource(id = R.drawable.relic_selected),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.relic_icon_unselected),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        },
        route = "relic",
    )


    data object LightCone: HsrDestination(
        icon = { selected ->
            AnimatedContent(
                selected,
                label = "",
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) {
                if (it) {
                    Icon(
                        painter = painterResource(id = R.drawable.light_cone_selected),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.cube_icon),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        },
        route = "light_cone",
    )

    data object Character: HsrDestination(
        icon = { selected ->
            AnimatedContent(
                selected,
                label = "",
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) {
                if (it) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PeopleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        },
        route = "character",
    )

}