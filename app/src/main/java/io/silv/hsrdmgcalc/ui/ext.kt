package io.silv.hsrdmgcalc.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.silv.data.constants.FIRE
import io.silv.data.constants.ICE
import io.silv.data.constants.IMAGINARY
import io.silv.data.constants.LIGHTNING
import io.silv.data.constants.PHYSICAL
import io.silv.data.constants.QUANTUM
import io.silv.data.constants.Type
import io.silv.data.constants.WIND

fun Modifier.conditional(condition : Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

@Composable
fun Type.toColor(): Color = remember {
    when(this) {
        Type.Physical -> PHYSICAL
        Type.Fire -> FIRE
        Type.Ice -> ICE
        Type.Lightning -> LIGHTNING
        Type.Wind -> WIND
        Type.Quantum -> QUANTUM
        Type.Imaginary -> IMAGINARY
    }
}