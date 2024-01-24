package io.silv.data.character.characters

import io.silv.data.character.BaseStats
import io.silv.data.character.Character
import io.silv.data.character.Eidolon
import io.silv.data.character.Trace
import io.silv.data.constants.Path
import io.silv.data.constants.Type

object Luocha: Character {

    override val id: Long = 0L
    override val stars: Short = 5
    override val name: String = "luocha"
    override val type: Type = Type.Ice
    override val path: Path = Path.Destruction

    override val baseStats: Map<Int, Pair<BaseStats, BaseStats>> by lazy {
        mapOf(
            0 to Pair(
                BaseStats(195, 92, 66, 96),
                BaseStats(380, 180, 128, 96)
            ),
            1 to Pair(
                BaseStats(459, 217, 155, 96),
                BaseStats(556, 263, 188, 96)
            ),
            2 to Pair(
                BaseStats(634, 300, 214, 96),
                BaseStats(732, 346, 247, 96)
            ),
            3 to Pair(
                BaseStats(810, 383, 273, 96),
                BaseStats(908, 429, 306, 96)
            ),
            4 to Pair(
                BaseStats(986, 466, 333, 96),
                BaseStats(1084, 512, 366, 96)
            ),
            5 to Pair(
                BaseStats(1162, 549, 392, 96),
                BaseStats(1260, 595, 425, 96)
            ),
            6 to Pair(
                BaseStats(1338, 632, 452, 96),
                BaseStats(1435, 679, 485, 96)
            )
        )
    }

    override val eidolons: List<Eidolon> get() = TODO()

    override val traces: List<Trace> get() = TODO()
}