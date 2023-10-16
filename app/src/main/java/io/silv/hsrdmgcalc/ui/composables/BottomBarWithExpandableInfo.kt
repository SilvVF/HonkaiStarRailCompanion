package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SheetValue.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.*
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.ui.AppState
import io.silv.hsrdmgcalc.ui.HsrDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null,
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarWithDraggableContent(
    appState: AppState,
    navBarHeight: Dp,
) {
    val selectedDest by appState.currentDestination.collectAsState(initial = HsrDestination.Character)
    val density = LocalDensity.current

    Column {
        var showBottomBarContent by rememberSaveable {
            mutableStateOf(false)
        }

        val hasContent by remember {
            derivedStateOf {
                appState.draggableContent != null || appState.draggablePeekContent != null
            }
        }

        LaunchedEffect(appState.bottomBarState.progress) {
            when (appState.bottomBarState.progress) {
                Hidden -> {
                    if(appState.bottomBarState.progress == Hidden && !appState.bottomBarState.dragInProgress) {
                        showBottomBarContent = false
                    }
                }
                Expanded -> showBottomBarContent = true
                PartiallyExpanded -> showBottomBarContent = true
            }
        }
        // TODO: Look into using a layout to solve below
        // Using animated visibility to allow the content time to be measured through the modifier.
        // This should maybe be a custom layout to avoid needing to do this.
        // Without the animated visibility the content has a snapping effect that is noticeable every time it is changed.
        AnimatedVisibility(
            visible = showBottomBarContent && hasContent
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .snapToPositionDraggable(
                        state = appState.bottomBarState,
                    )
            ) {
                appState.draggablePeekContent?.invoke(
                    Modifier.onSizeChanged {
                        if (it.height > 0) {
                            appState.bottomBarState.peekHeight = with(density){
                                it.height.toDp().value.roundToInt().toFloat()
                            }
                        }
                    }
                )
                appState.draggableContent?.invoke()
            }
        }
        BottomAppBar(
            Modifier.height(navBarHeight)
        ) {
            appState.destinations.forEach { dest ->
                NavigationBarItem(
                    selected = selectedDest == dest,
                    onClick = {
                        appState.bottomBarState.snapProgressTo(
                            when(appState.bottomBarState.progress) {
                                Hidden -> PartiallyExpanded
                                Expanded -> Hidden
                                PartiallyExpanded -> Expanded
                            }
                        )
                    },
                    icon = {
                        Image(
                            painter = if(dest == selectedDest)
                                dest.selectedIcon()
                            else
                                dest.unselectedIcon(),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun Modifier.snapToPositionDraggable(
    state: DraggableBottomBarState,
) = composed {

    val density = LocalDensity.current

    val dragState = rememberDraggableState {
        state.onDrag(
            with(density) { it.toDp().value }
        )
    }

    val height by animateDpAsState(
        targetValue = state.peekHeight.dp,
        label = "height-anim"
    )

    // setting height to 0.dp while hidden to avoid
    // consuming drags gestures
    return@composed this
        .onSizeChanged {
            if (it.height <= 0) {
                return@onSizeChanged
            }
            val maxHeight = with(density) {
                it.height.toDp().value
                    .roundToInt()
                    .toFloat()
            }
            if (maxHeight != state.peekHeight) {
                state.maxHeight = maxHeight
            }
        }
        .draggable(
            dragState,
            Orientation.Vertical,
            onDragStopped = {
                state.onDragEnd()
            }
        )
        .conditional(
            condition = state.progress == Hidden && !state.dragInProgress,
            ifTrue = {
                height(0.dp)
            },
        )
        .conditional(
            condition = !state.dragInProgress && state.progress == PartiallyExpanded,
            ifTrue = {
                height(height)
            },
            ifFalse = {
                wrapContentHeight()
                offset {
                    IntOffset(
                        x = 0,
                        y = state.offset.value.dp.roundToPx()
                    )
                }
            }
        )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDraggableBottomBarState(
    scope: CoroutineScope = rememberCoroutineScope(),
    start: SheetValue = Hidden
) = rememberSaveable(
    scope,
    start,
    saver = Saver(
        save = {
            it.progress
        },
        restore = {
            DraggableBottomBarState(
                scope, it
            )
        }
    ),
) {
    DraggableBottomBarState(scope, start)
}

@OptIn(ExperimentalMaterial3Api::class)
class DraggableBottomBarState(
    private val scope: CoroutineScope,
    start: SheetValue = Hidden
) {

    var maxHeight by mutableFloatStateOf(Float.MAX_VALUE)
    var peekHeight by mutableFloatStateOf(Float.MAX_VALUE)

    var dragInProgress by mutableStateOf(false)

    init {
        scope.launch {
            snapshotFlow { maxHeight }.collectLatest {
                snapProgressTo(progress)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    var progress by mutableStateOf(start)
        private set

    @OptIn(ExperimentalMaterial3Api::class)
    val offset = Animatable(
        // when restoring state set the Animatable to the correct progress value.
        when(progress) {
            Hidden -> maxHeight
            Expanded -> 0f
            PartiallyExpanded -> maxHeight - peekHeight
        }
    )

    fun onDrag(deltaAsDp: Float) {
        dragInProgress = true
        scope.launch {
            offset.snapTo(
                (offset.value + deltaAsDp).coerceAtLeast(0f)
            )
            progress = when {
                offset.value < maxHeight - peekHeight -> Expanded
                else -> if (offset.value >  maxHeight - peekHeight / 2) Hidden else PartiallyExpanded
            }
        }
    }

    suspend fun onDragEnd() {
        when (progress) {
            Hidden -> offset.animateTo(maxHeight)
            Expanded -> offset.animateTo(0f)
            PartiallyExpanded -> offset.animateTo(maxHeight - peekHeight)
        }
        dragInProgress = false
    }

    fun snapProgressTo(sheetValue: SheetValue) {
        scope.launch {
            if (sheetValue != Hidden)
                progress = sheetValue
            when (sheetValue) {
                Hidden -> offset.animateTo(maxHeight)
                Expanded -> offset.animateTo(0f)
                PartiallyExpanded -> offset.animateTo(maxHeight - peekHeight)
            }
            if (sheetValue == Hidden)
                progress = sheetValue
        }
    }
}
