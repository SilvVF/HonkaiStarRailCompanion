package io.silv.hsrdmgcalc.ui.screens.add_light_cone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import io.silv.hsrdmgcalc.data.HonkaiUtils
import io.silv.hsrdmgcalc.ui.composables.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LightConeFilters(
    val fiveStarOnly: Boolean = false,
    val hideThreeStar: Boolean = false,
    val pathFilter: Path? = null
)

class LightConeSearchStateHolder(
    initialFilters: LightConeFilters = LightConeFilters(),
    initialQuery: String = "",
    private val lightConesList: ImmutableList<Pair<String, Path>> = persistentListOf(),
    private val scope: CoroutineScope
) {
    private val mutableFilters = MutableStateFlow(initialFilters)
    val filters = mutableFilters.asStateFlow()

    var query by mutableStateOf(initialQuery)
        private set

    var searching by mutableStateOf(false)
        private set

    var filteredLightCones by mutableStateOf(lightConesList)
        private set

    fun updateQuery(query: String) {
        this.query = query
    }

    fun showFiveStarOnlyClicked() {
        scope.launch {
            mutableFilters.update {
                it.copy(
                    fiveStarOnly = !it.fiveStarOnly
                )
            }
        }
    }

    fun hide3StarClicked() {
        scope.launch {
            mutableFilters.update {
                it.copy(
                    hideThreeStar = !it.hideThreeStar
                )
            }
        }
    }

    fun pathClicked(path: Path?) {
        scope.launch {
            mutableFilters.update {
                it.copy(
                    pathFilter = if (path == it.pathFilter) null else path
                )
            }
        }
    }

    init {
        scope.launch {
            snapshotFlow { query }
                .onEach { searching = true }
                .combine(mutableFilters) { query, filters -> query to filters }
                .collectLatest { pair ->
                    val query = pair.first
                    val (fiveStarOnly, hideThreeStar, pathFilter) = pair.second
                    filteredLightCones = lightConesList
                        .asSequence()
                        .filter { query.isBlank() || query.lowercase() in it.first.lowercase() }
                        .filter { lightCone ->
                            !hideThreeStar || HonkaiUtils.lightConeStars(lightCone.first) != 3
                        }
                        .filter { lightCone ->
                            !fiveStarOnly || HonkaiUtils.lightConeStars(lightCone.first) == 5
                        }
                        .filter { lightCone ->
                            pathFilter == null || lightCone.second == pathFilter
                        }
                        .toImmutableList()

                    delay(300)
                    searching = false
                }
        }
    }
}
