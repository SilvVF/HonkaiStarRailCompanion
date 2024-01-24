package io.silv.data.character

import androidx.annotation.IntRange
import androidx.annotation.Size
import io.silv.data.constants.Path
import io.silv.data.constants.Type

interface Character {
    val id: Long

    @get:IntRange(4, 5)
    val stars: Short

    val name: String

    val type: Type

    val path: Path

    @get:Size(7)
    val baseStats: Map<Int, Pair<BaseStats, BaseStats>>

    @get:Size(6)
    val eidolons: List<Eidolon>

    val traces: List<Trace>
}