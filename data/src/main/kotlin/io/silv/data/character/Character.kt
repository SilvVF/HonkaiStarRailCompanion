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

    /**
     *  this is the percentage value you can find in the skill description (Deal DMG equal to XX%)
     *  default is 0% bonus.
     */
    val skillMultiplier: Float get() = .0f

    /**
     * this appears only on some skills, like Dan Heng's Ultimate that
     * deals additional damage to slowed enemies.
     */
    val extraMultiplier: Float get() = .0f

    /**
     * this is the attribute the skill scales off - in most cases it's ATK,
     */
    val scalingAttribute: Attribute get() = ATK

    /**
     * this is the flat additional damage that appears on some skills.
      */
    val extraDmg: Double get() = 0
}