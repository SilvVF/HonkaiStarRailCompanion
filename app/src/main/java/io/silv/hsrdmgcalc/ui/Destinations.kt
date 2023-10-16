package io.silv.hsrdmgcalc.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import io.silv.hsrdmgcalc.R

sealed class HsrDestination(
    val selectedIcon: @Composable () -> Painter,
    val unselectedIcon:  @Composable () -> Painter,
    val route: String,
) {

    data object Character: HsrDestination(
        selectedIcon = { painterResource(R.drawable.character_icon) },
        unselectedIcon = {
            painterResource(R.drawable.character_icon)
        },
        route = "character",
    )

}