package io.silv.hsrdmgcalc.ui.screens.character.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.preferences.DisplayPrefs
import io.silv.hsrdmgcalc.preferences.Grouping

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCharactersBottomSheet(
    visible: Boolean,
    prefs: DisplayPrefs.CharacterGrouping,
    updateOwnedOnly: (Boolean) -> Unit,
    updateFiveStarOnly: (Boolean) -> Unit,
    updateSortByLevel: (Grouping) -> Unit,
    onDismissRequest: () -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        updateSortByLevel(
                            when (prefs.level) {
                                Grouping.ASC -> Grouping.NONE
                                Grouping.DSC -> Grouping.ASC
                                Grouping.NONE -> Grouping.DSC
                            }
                        )
                    }
            ) {
                Box(modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(0.1f)
                    .heightIn(40.dp),
                    Alignment.Center
                ) {
                   AnimatedContent(targetState = prefs.level, label = "") {
                       when (it) {
                           Grouping.ASC -> Icon(
                               imageVector = Icons.Filled.ArrowUpward,
                               contentDescription = null,
                               tint = MaterialTheme.colorScheme.primary
                           )
                           Grouping.DSC -> Icon(
                               imageVector = Icons.Filled.ArrowDownward,
                               contentDescription = null,
                               tint = MaterialTheme.colorScheme.primary
                           )
                           Grouping.NONE -> Text(text = "N/A", color = MaterialTheme.colorScheme.primary)
                       }
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sort by level")
            }
            CheckBoxWithLabel(
                modifier = Modifier.fillMaxWidth(),
                label = "Show five star only",
                checked = prefs.fiveStarOnly,
                onCheckChanged = updateFiveStarOnly
            )
            CheckBoxWithLabel(
                modifier = Modifier.fillMaxWidth(),
                label = "Show owned only",
                checked = prefs.ownedOnly,
                onCheckChanged = updateOwnedOnly
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CheckBoxWithLabel(
    modifier: Modifier,
    label: String,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckChanged,
            enabled = true
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall
        )
    }
}