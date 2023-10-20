package io.silv.hsrdmgcalc.ui.screens.character.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.Type
import io.silv.hsrdmgcalc.ui.composables.pathFilterRow
import io.silv.hsrdmgcalc.ui.composables.typeFilterRow

@Composable
fun CharacterPeekContent(
    selectedPath: Path?,
    selectedType: Type?,
    onTypeSelected: (Type?) -> Unit,
    onPathSelected: (Path?) -> Unit
) {
    Column {
        val modifier = Modifier
            .fillMaxWidth()
            .heightIn(38.dp)
            .padding(bottom = 6.dp)
        LazyRow(
            modifier = modifier
        ) {
            typeFilterRow(
                startSelected = selectedType
            ) { type ->
                onTypeSelected(type)
            }
        }
        LazyRow(
            modifier = modifier,
        ) {
            pathFilterRow(
                startSelected = selectedPath
            ) { path ->
                onPathSelected(path)
            }
        }
    }
}