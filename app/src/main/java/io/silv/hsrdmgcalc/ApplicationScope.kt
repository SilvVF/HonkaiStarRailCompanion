package io.silv.hsrdmgcalc

import kotlinx.coroutines.CoroutineScope

interface ApplicationScope {
    val scope: CoroutineScope
}