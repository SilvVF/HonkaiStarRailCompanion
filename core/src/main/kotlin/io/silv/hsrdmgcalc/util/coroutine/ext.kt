package io.silv.hsrdmgcalc.util.coroutine

import io.silv.hsrdmgcalc.util.koin.runInKoinComponent
import io.silv.hsrdmgcalc.util.koin.runInKoinComponentSuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Think twice before using this. This is a delicate API. It is easy to accidentally create resource or memory leaks when GlobalScope is used.
 *
 * **Possible replacements**
 * - suspend function
 * - custom scope like view or presenter scope
 */
@DelicateCoroutinesApi
fun launchUI(block: suspend CoroutineScope.() -> Unit): Job =
    runInKoinComponent {
        get<ApplicationScope>()
            .launch(Dispatchers.Main, CoroutineStart.DEFAULT, block)
    }

/**
 * Think twice before using this. This is a delicate API. It is easy to accidentally create resource or memory leaks when GlobalScope is used.
 *
 * **Possible replacements**
 * - suspend function
 * - custom scope like view or presenter scope
 */
@DelicateCoroutinesApi
fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
    runInKoinComponent {
        get<ApplicationScope>()
            .launch(Dispatchers.IO, CoroutineStart.DEFAULT, block)
    }

/**
 * Think twice before using this. This is a delicate API. It is easy to accidentally create resource or memory leaks when GlobalScope is used.
 *
 * **Possible replacements**
 * - suspend function
 * - custom scope like view or presenter scope
 */
@DelicateCoroutinesApi
fun launchNow(block: suspend CoroutineScope.() -> Unit): Job =
    runInKoinComponent {
        get<ApplicationScope>()
            .launch(Dispatchers.Main, CoroutineStart.UNDISPATCHED, block)
    }

fun CoroutineScope.launchUI(block: suspend CoroutineScope.() -> Unit): Job =
    runInKoinComponent {
        launch(get<MainDispatcher>(), block = block)
    }

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit): Job =
    runInKoinComponent {
        launch(get<IODispatcher>(), block = block)
    }

fun CoroutineScope.launchNonCancellable(block: suspend CoroutineScope.() -> Unit): Job =
    launchIO { withContext(NonCancellable, block) }

suspend fun <T> withUIContext(block: suspend CoroutineScope.() -> T) = runInKoinComponentSuspend {
    withContext(
        get<MainDispatcher>(),
        block,
    )
}

suspend fun <T> withIOContext(block: suspend CoroutineScope.() -> T) = runInKoinComponentSuspend {
    withContext(
        get<IODispatcher>(),
        block,
    )
}

suspend fun <T> withNonCancellableContext(block: suspend CoroutineScope.() -> T) =
    withContext(NonCancellable, block)