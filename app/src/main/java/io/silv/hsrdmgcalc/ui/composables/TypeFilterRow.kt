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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull

val IMAGINARY = Color(0xffF8EB70)
val PHYSICAL = Color(0xffC5C5C5)
val LIGHTNING = Color(0xffDB77F4)
val FIRE = Color(0xffE62929)
val ICE = Color(0xff8BD4EF)
val WIND = Color(0xff87DAA7)
val QUANTUM = Color(0xff746DD1)

enum class Type(val color: Color) {
    Physical(PHYSICAL),
    Fire(FIRE),
    Ice(ICE),
    Lightning(LIGHTNING),
    Wind(WIND),
    Quantum(QUANTUM),
    Imaginary(IMAGINARY),
}

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
    onElementSelected: (Type?) -> Unit,
) {
    item { Spacer(Modifier.width(8.dp)) }
    item {
        val selectedList = remember {
            mutableStateListOf(
                TagSelection(Type.Physical),
                TagSelection(Type.Wind),
                TagSelection(Type.Quantum),
                TagSelection(Type.Fire),
                TagSelection(Type.Ice),
                TagSelection(Type.Imaginary),
                TagSelection(Type.Lightning)
            )
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
                ?: MaterialTheme.colorScheme.secondaryContainer,
            onTagSelected = {
                onElementSelected(
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

