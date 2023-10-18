package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull

//Destruction
//The Hunt
//Erudition
//Harmony
//Nihility
//Preservation
//Abundance
enum class Path(val color: Color) {
    Destruction(PHYSICAL),
    TheHunt(PHYSICAL),
    Erudition(PHYSICAL),
    Harmony(PHYSICAL),
    Nihility(PHYSICAL),
    Preservation(PHYSICAL),
    Abundance(PHYSICAL),
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.pathFilterRow(
    modifier: Modifier = Modifier,
    onPathSelected: (Path?) -> Unit,
) {
    item { Spacer(Modifier.width(8.dp)) }
    item {
        val selectedList = remember {
            mutableStateListOf(
                TagSelection(Path.Abundance),
                TagSelection(Path.Erudition),
                TagSelection(Path.Nihility),
                TagSelection(Path.Harmony),
                TagSelection(Path.TheHunt),
                TagSelection(Path.Preservation),
                TagSelection(Path.Destruction)
            )
        }
        MultiFilterTag(
            modifier = modifier.heightIn(38.dp),
            tagSelections = selectedList,
            label = { (item, selected) ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = item.name,
                        color = if (!selected) item.color else MaterialTheme.colorScheme.surface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    PathIcon(
                        path = item,
                        contentDescription = item.name,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            selectedColor = selectedList.fastFirstOrNull { it.selected }?.item?.color
                ?: MaterialTheme.colorScheme.secondaryContainer,
            onTagSelected = {
                onPathSelected(
                    if (it.selected) null else it.item
                )
                val idx = selectedList.indexOf(it)
                val item = selectedList[idx]
                selectedList[idx] = item.copy(selected = !item.selected)
            }
        )
    }
    item { Spacer(Modifier.width(8.dp)) }
}