package io.silv.hsrdmgcalc

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
    var sum: Float = 0.toFloat()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}


@OptIn(ExperimentalContracts::class)
inline fun <T1, T2, R> scopeNotNull(
    t1: T1?,
    t2: T2?,
    block: (T1, T2) -> R
): R? {

    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    if (t1 != null && t2 != null) {
        return block(t1, t2)
    }

    return null
}



