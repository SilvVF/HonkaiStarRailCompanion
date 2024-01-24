package io.silv.hsrdmgcalc.util.koin

import org.koin.core.component.KoinComponent

inline fun <T> runInKoinComponent(
    crossinline block: KoinComponent.() -> T
): T {
    val koin = object: KoinComponent{}

    return block(koin)
}

suspend inline fun <T> runInKoinComponentSuspend(
    crossinline block: suspend KoinComponent.() -> T
): T {
    val koin = object: KoinComponent{}

    return block(koin)
}