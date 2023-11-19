package io.silv.hsrdmgcalc.ui.screens.add_light_cone

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperImposeSelector(
    modifier: Modifier = Modifier,
    superimpose: Int,
    onSuperimposeChange: (Int) -> Unit,
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = remember(superimpose) { "Superimpose $superimpose" },
            onValueChange = {},
            label = { Text("superimpose") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            (1..5).forEach { superimposeLevel ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Superimpose $superimposeLevel",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = {
                        onSuperimposeChange(superimposeLevel)
                        expanded = false
                    }
                )
            }
        }
    }
}
