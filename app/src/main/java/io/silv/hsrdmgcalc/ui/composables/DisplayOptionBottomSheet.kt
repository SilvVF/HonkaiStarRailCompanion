package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import kotlin.math.roundToInt

enum class CardType(val string: String) {
    Full("Full Card"),
    List("List"),
    SemiCompact("Semi-Compact Card"),
    Compact("Compact Card"),
    ExtraCompact("Extra-Compact Card")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayOptionsBottomSheet(
    visible: Boolean,
    prefs: DisplayPrefs.Prefs,
    onDismissRequest: () -> Unit,
    onGridSizeSelected: (Int) -> Unit,
    onAnimatePlacementChanged: (Boolean) -> Unit,
    onCardTypeSelected: (CardType) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(key1 = visible) {
        if (visible)
            sheetState.show()
        else
            sheetState.hide()
    }

    if (visible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
        ) {
            SelectCardType(
                cardType = prefs.cardType,
                onCardTypeSelected = onCardTypeSelected
            )
            GridSizeSelector(
                Modifier.fillMaxWidth(),
                onSizeSelected = onGridSizeSelected,
                size = prefs.gridCells
            )
            AnimatePlacementCheckBox(
                animatePlacement = prefs.animateCardPlacement,
                onCheckChanged = onAnimatePlacementChanged,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun AnimatePlacementCheckBox(
    animatePlacement: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = animatePlacement,
            onCheckedChange = onCheckChanged,
            enabled = true
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Animate card placement",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun SelectCardType(
    cardType: CardType,
    onCardTypeSelected: (CardType) -> Unit
) {
    val types = remember { CardType.values().toList() }

    types.fastForEachIndexed { i, type ->
        if (i == 2) {
            Divider()
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onCardTypeSelected(type)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = cardType == type,
                onClick = {
                    onCardTypeSelected(type)
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = type.string,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun GridSizeSelector(
    modifier: Modifier = Modifier,
    onSizeSelected: (Int) -> Unit,
    size: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Grid size",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$size per row",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            )
        }
        Slider(
            modifier = Modifier.weight(1f),
            valueRange = 0f..100f,
            onValueChange = { value ->
                onSizeSelected(
                    when (value.roundToInt()) {
                        0 -> 1
                        in 0..25 -> 2
                        in 0 ..50 -> 3
                        in 0..75 -> 4
                        else -> 5
                    }
                )
            },
            steps = 3,
            value = when (size) {
                1 -> 0f
                2 -> 25f
                3 -> 50f
                4 -> 75f
                else -> 100f
            }
        )
        Text(
            text = "Reset",
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable { onSizeSelected(3) }
        )
    }
}
