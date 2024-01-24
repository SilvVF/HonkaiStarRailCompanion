package io.silv.hsrdmgcalc.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher

typealias IODispatcher = CoroutineDispatcher
typealias MainDispatcher = CoroutineDispatcher
typealias UnconfinedDispatcher = CoroutineDispatcher
typealias DefaultDispatcher = CoroutineDispatcher

interface AppDispatchers {
    val io: IODispatcher
    val main: MainDispatcher
    val unconfined: UnconfinedDispatcher
    val default: DefaultDispatcher
}
