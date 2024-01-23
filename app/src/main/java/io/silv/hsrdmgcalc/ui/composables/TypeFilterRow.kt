package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import io.silv.data.constants.Type
import kotlinx.collections.immutable.persistentListOf


/*
1.1	Physical
1.2	Fire
1.3	Ice
1.4	Lightning
1.5	Wind
1.6	Quantum
1.7	Imaginary
 */


@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.typeFilterRow(
    modifier: Modifier = Modifier,
    startSelected: Type? = null,
    onElementSelected: (Type?) -> Unit,
) {
    item { Spacer(Modifier.width(8.dp)) }
    item {

        var selectedType by rememberSaveable {
            mutableStateOf<Type?>(startSelected)
        }

        val selectedList by remember(selectedType) {
            derivedStateOf {
                persistentListOf(
                    TagSelection(Type.Physical, Type.Physical == selectedType),
                    TagSelection(Type.Wind, Type.Wind == selectedType),
                    TagSelection(Type.Quantum, Type.Quantum == selectedType),
                    TagSelection(Type.Fire, Type.Fire == selectedType),
                    TagSelection(Type.Ice, Type.Ice == selectedType),
                    TagSelection(Type.Imaginary, Type.Imaginary == selectedType),
                    TagSelection(Type.Lightning, Type.Lightning == selectedType)
                )
            }
        }
        MultiFilterTag(
            modifier = modifier.heightIn(38.dp),
            tagSelections = selectedList,
            label = { (item, selected) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.name,
                        color = if (!selected) item.color else MaterialTheme.colorScheme.surface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TypeIcon(
                        type = item.name,
                        contentDescription = item.name,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            selectedColor = selectedList.fastFirstOrNull { it.selected }?.item?.color
                ?: MaterialTheme.colorScheme.secondaryContainer
        ) {
            onElementSelected(
                if (it.selected) null else it.item
            )
            selectedType = if (it.item == selectedType)
                null
            else
                it.item
        }
    }
    item { Spacer(Modifier.width(8.dp)) }
}

