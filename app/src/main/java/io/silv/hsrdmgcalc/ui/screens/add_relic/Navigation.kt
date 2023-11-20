package io.silv.hsrdmgcalc.ui.screens.add_relic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.silv.hsrdmgcalc.data.HonkaiConstants
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.navigation.NavResultCallback
import io.silv.hsrdmgcalc.ui.navigation.SubDestination
import io.silv.hsrdmgcalc.ui.navigation.navigateForResult

data class RelicInfo(
    val set: String,
    val item: String,
    val piece: HonkaiConstants.Piece,
    val level: Int,
    val stats: List<Pair<String, Float>>
)

fun NavController.navigateToAddRelicScreen(navResultCallback: NavResultCallback<RelicInfo?>) {
    return this.navigateForResult(
        route = SubDestination.RelicAdd.route,
        navResultCallback = navResultCallback,
        builder = { launchSingleTop = true }
    )
}

fun NavGraphBuilder.addRelicScreen(
    appState: AppState,
    navigateBack: (RelicInfo?) -> Unit,
) {
    composable(
        route = SubDestination.RelicAdd.route,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Add Relic")
        }
    }
}