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
inline fun <T, V, R> scopeNotNull(
    t: T?,
    v: V?,
    block: (T, V) -> R
): R? {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (t != null && v != null) {
        return block(t, v)
    }

    return null
}

