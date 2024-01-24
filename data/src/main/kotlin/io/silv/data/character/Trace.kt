package io.silv.data.character

interface Trace {

    val level: Int

    val modifiers: List<Any> get() = TODO()
    val bonuses: List<Any> get() = TODO()
}