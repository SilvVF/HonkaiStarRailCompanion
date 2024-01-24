package io.silv.data.character

import androidx.annotation.IntRange

interface Eidolon {

    @get:IntRange(1, 6)
    val number: Int

    val modifiers: List<Any> get() = TODO()
}