package io.silv.hsrdmgcalc.ui.composables

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SheetValue.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberExpandableState(startProgress: SheetValue) = rememberSaveable(
    saver = Saver(
        save = { it.progress },
        restore = { ExpandableState(it) }
    ),
) {
    ExpandableState(startProgress)
}

@OptIn(ExperimentalMaterial3Api::class)
class ExpandableState(
    startProgress: SheetValue
) {

    var progress by mutableStateOf<SheetValue>(startProgress)

    fun toggleProgress() {
        progress = when (progress) {
            Hidden -> PartiallyExpanded
            Expanded -> Hidden
            PartiallyExpanded -> Expanded
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableInfoLayout(
    state: ExpandableState = rememberExpandableState(startProgress = Hidden),
    peekContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    var dragHeightOffset by remember {
        mutableFloatStateOf(0f)
    }

    var maxHeightPx by remember {
        mutableIntStateOf(0)
    }
    var peekHeightPx by remember {
        mutableIntStateOf(0)
    }

    var handled by remember {
        mutableStateOf(true)
    }

    val dragState = rememberDraggableState {
        if (!handled) { return@rememberDraggableState }
        scope.launch {
            dragHeightOffset += it
        }
    }

    Layout(
        content = {
            peekContent()
            content()
        },
        modifier = Modifier
            .draggable(
                dragState,
                Orientation.Vertical,
                onDragStopped = {
                    if (!handled) { return@draggable }
                    handled = false
                    val progress = state.progress


                    val dragEndHeight = when (progress) {
                        Expanded -> maxHeightPx
                        PartiallyExpanded -> peekHeightPx
                        else -> 0
                    } + -dragHeightOffset

                    Log.d("ExpandableInfoLayout", "${state.progress} ON Drag end  end: $dragEndHeight   max: $maxHeightPx  peek: $peekHeightPx")

                    Log.d("ExpandableInfoLayout", "Drag offset $dragHeightOffset")

                    state.progress = when (progress) {
                        PartiallyExpanded -> when {
                            dragEndHeight < peekHeightPx / 3 -> Hidden
                            dragEndHeight > peekHeightPx -> Expanded
                            else -> PartiallyExpanded
                        }
                        Expanded -> when {
                            dragEndHeight < peekHeightPx / 3 -> Hidden
                            dragEndHeight < maxHeightPx - (peekHeightPx / 3)  -> PartiallyExpanded
                            else -> Expanded
                        }
                        else -> progress
                    }
                    dragHeightOffset = 0f
                    handled = true
                }
            )
            .animateContentSize()
    ) { measurable, constraints ->

        val placeables = measurable.map { it.measure(constraints) }

        val peekHeight = placeables.first().height.also { peekHeightPx = it }

        val maxHeight = placeables.sumOf { it.height }.also { maxHeightPx = it }

        val height = when(state.progress) {
            Hidden -> -10f
            Expanded ->
                (maxHeight + -dragHeightOffset).coerceAtMost(maxHeight.toFloat())
            PartiallyExpanded ->
                (peekHeight + -dragHeightOffset).coerceAtMost(maxHeight.toFloat())
        }

        layout(constraints.maxWidth, height.roundToInt()) {

            var y = 0

            placeables.forEach { placeable ->
                placeable.placeRelative(0, y.also { y += placeable.height })
            }
        }
    }
}
