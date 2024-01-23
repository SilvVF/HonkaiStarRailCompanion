package io.silv.data.constants

import kotlin.math.roundToInt

typealias HP = Int
typealias ATK = Int
typealias DEF = Int
typealias SPD = Int


object CharacterStats {

    data class BaseStats(
        val hp: HP,
        val atk: ATK,
        val def: DEF,
        val spd: SPD
    )

    val levelRanges = listOf(
        1..20,
        20..40,
        40..50,
        50..60,
        60..70,
        70..80,
        80..90
    )

    /**
     * - key = Character name.
     * - value = Ascension to Pair(first = BaseStats at min lvl, second = BaseStats at max lvl).
     **/
    val stats by lazy {
        mapOf(
            "Bailu" to mapOf(
                0 to Pair(BaseStats(179, 76, 66, 98), BaseStats(350, 149, 128, 98)),
                1 to Pair(BaseStats(421, 179, 155, 98), BaseStats(511, 218, 188, 98)),
                2 to Pair(BaseStats(583, 248, 214, 98), BaseStats(673, 287, 247, 98)),
                3 to Pair(BaseStats(745, 317, 273, 98), BaseStats(834, 356, 306, 98)),
                4 to Pair(BaseStats(906, 386, 333, 98), BaseStats(996, 424, 366, 98)),
                5 to Pair(BaseStats(1068, 455, 392, 98), BaseStats(1157, 493, 425, 98)),
                6 to Pair(BaseStats(1229, 524, 452, 98), BaseStats(1319, 562, 485, 98))
            ),

            "Blade" to mapOf(
                0 to Pair(BaseStats(184, 73, 66, 97), BaseStats(360, 144, 128, 97)),
                1 to Pair(BaseStats(434, 173, 155, 97), BaseStats(526, 210, 188, 97)),
                2 to Pair(BaseStats(600, 240, 214, 97), BaseStats(693, 277, 247, 97)),
                3 to Pair(BaseStats(766, 306, 273, 97), BaseStats(859, 859, 306, 97)),
                4 to Pair(BaseStats(933, 373, 333, 97), BaseStats(1025, 410, 366, 97)),
                5 to Pair(BaseStats(1099, 439, 392, 97), BaseStats(1191, 476, 425, 97)),
                6 to Pair(BaseStats(1265, 506, 452, 97), BaseStats(1358, 543, 485, 97))
            ),

            "Bronya" to mapOf(
                0 to Pair(BaseStats(168	, 79, 72, 100), BaseStats(329, 154, 141, 100)),
                1 to Pair(BaseStats(397, 186, 170, 100), BaseStats(481, 225, 206, 100)),
                2 to Pair(BaseStats(549, 257, 235, 100), BaseStats(633, 297, 272, 100)),
                3 to Pair(BaseStats(701, 328, 301, 100), BaseStats(785, 368, 337, 100)),
                4 to Pair(BaseStats(853, 399, 366, 100), BaseStats(937, 439, 402, 100)),
                5 to Pair(BaseStats(1005, 471, 431, 100), BaseStats(1089, 510, 468, 100)),
                6 to Pair(BaseStats(1157, 542, 497, 100), BaseStats(1241, 582, 533, 100))
            ),

            "DanHengImbibitorLunae" to mapOf(
                0 to Pair(BaseStats(168	, 95, 19, 102), BaseStats(329, 185, 38, 102)),
                1 to Pair(BaseStats(397, 223, 45, 102), BaseStats(481, 270	, 55, 102)),
                2 to Pair(BaseStats(549, 308, 63, 102), BaseStats(633, 356, 73, 102)),
                3 to Pair(BaseStats(701, 394, 80, 102), BaseStats(785, 441, 90, 102)),
                4 to Pair(BaseStats(853, 479, 98, 102), BaseStats(937, 527, 108, 102)),
                5 to Pair(BaseStats(1005, 565, 116, 102), BaseStats(1089, 613, 125, 102)),
                6 to Pair(BaseStats(1157, 651, 133, 102), BaseStats(1241, 698, 143, 102))
            ),

            "FuXuan" to mapOf(
                0 to Pair(BaseStats(200	, 63, 82, 100), BaseStats(391, 123, 160, 100)),
                1 to Pair(BaseStats(471, 148, 193, 100), BaseStats(571, 180	, 235, 100)),
                2 to Pair(BaseStats(652, 205, 268, 100), BaseStats(752, 237, 309, 100)),
                3 to Pair(BaseStats(832, 262, 342, 100), BaseStats(932, 294, 383, 100)),
                4 to Pair(BaseStats(1013, 319, 416, 100), BaseStats(1113, 351, 457, 100)),
                5 to Pair(BaseStats(1193, 376, 490, 100), BaseStats(1294, 408, 532, 100)),
                6 to Pair(BaseStats(1374, 434, 565, 100), BaseStats(1474, 465, 606, 100))
            ),

            "Clara" to mapOf(
                0 to Pair(BaseStats(168	, 100, 66, 90), BaseStats(329, 195, 128, 90)),
                1 to Pair(BaseStats(397, 235, 155, 90), BaseStats(481, 285, 188, 90)),
                2 to Pair(BaseStats(549, 326, 214, 90), BaseStats(633, 376, 247, 90)),
                3 to Pair(BaseStats(701, 416, 273, 90), BaseStats(785, 466, 306, 90)),
                4 to Pair(BaseStats(853, 506, 333, 90), BaseStats(937, 556, 366, 90)),
                5 to Pair(BaseStats(1005, 596	, 392, 90), BaseStats(1089, 647, 425, 90)),
                6 to Pair(BaseStats(1157, 687, 452, 90), BaseStats(1241, 737, 485, 90))
            ),

            "Gepard" to mapOf(
                0 to Pair(BaseStats(190	, 73, 89, 92), BaseStats(370, 144, 173, 92)),
                1 to Pair(BaseStats(446, 173, 209, 92), BaseStats(541, 210, 253, 92)),
                2 to Pair(BaseStats(617, 240, 289, 92), BaseStats(712, 277, 334, 92)),
                3 to Pair(BaseStats(788, 306, 369, 92), BaseStats(883, 343, 414, 92)),
                4 to Pair(BaseStats(959, 373, 449, 92), BaseStats(1054, 410, 494, 92)),
                5 to Pair(BaseStats(1130, 439, 530, 92), BaseStats(1226, 476,574, 92)),
                6 to Pair(BaseStats(1302, 506, 610, 92), BaseStats(1397, 543, 654, 92))
            ),

            "Himeko" to mapOf(
                0 to Pair(BaseStats(142	, 102, 59, 96), BaseStats(277, 200, 115, 96)),
                1 to Pair(BaseStats(335, 241, 139, 96), BaseStats(406, 293, 169, 96)),
                2 to Pair(BaseStats(463, 334, 193, 96), BaseStats(534, 386, 222, 96)),
                3 to Pair(BaseStats(591, 427, 246, 96), BaseStats(662, 478, 276, 96)),
                4 to Pair(BaseStats(719, 519, 299, 96), BaseStats(791, 571, 329, 96)),
                5 to Pair(BaseStats(848, 612, 353, 96), BaseStats(919, 664,383, 96)),
                6 to Pair(BaseStats(976, 705, 406, 96), BaseStats(1047, 756, 436, 96))
            ),

            "JingYuan" to mapOf(
                0 to Pair(BaseStats(158	, 95, 66, 99), BaseStats(308, 185, 128, 99)),
                1 to Pair(BaseStats(372, 223, 155, 99), BaseStats(451, 270, 188, 99)),
                2 to Pair(BaseStats(514, 308, 214, 99), BaseStats(594, 356, 247, 99)),
                3 to Pair(BaseStats(657, 394, 273, 99), BaseStats(736, 441, 306, 99)),
                4 to Pair(BaseStats(799, 479, 333, 99), BaseStats(879, 527, 366, 99)),
                5 to Pair(BaseStats(942, 565, 392, 99), BaseStats(1021, 613,425, 99)),
                6 to Pair(BaseStats(1085, 651, 452, 99), BaseStats(1164, 698, 485, 99))
            ),

            "Jingliu" to mapOf(
                0 to Pair(BaseStats(195, 92, 66, 96), BaseStats(380, 180, 128, 96)),
                1 to Pair(BaseStats(459, 217, 155, 96), BaseStats(556, 263, 188, 96)),
                2 to Pair(BaseStats(634, 300, 214, 96), BaseStats(732, 346, 247, 96)),
                3 to Pair(BaseStats(810, 383, 273, 96), BaseStats(908, 429, 306, 96)),
                4 to Pair(BaseStats(986, 466, 333, 96), BaseStats(1084, 512, 366, 96)),
                5 to Pair(BaseStats(1162, 549, 392, 96), BaseStats(1260, 595, 425, 96)),
                6 to Pair(BaseStats(1338, 632, 452, 96), BaseStats(1435, 679, 485, 96))
            ),

            "Kafka" to mapOf(
                0 to Pair(BaseStats(147, 92, 66, 100), BaseStats(288, 180, 128, 100)),
                1 to Pair(BaseStats(347, 217, 155, 100), BaseStats(421, 263, 188, 100)),
                2 to Pair(BaseStats(480, 300, 214, 100), BaseStats(554, 346, 247, 100)),
                3 to Pair(BaseStats(613, 383, 273, 100), BaseStats(687, 429, 306, 100)),
                4 to Pair(BaseStats(746, 466, 333, 100), BaseStats(820, 512, 366, 100)),
                5 to Pair(BaseStats(879, 549, 392, 100), BaseStats(953, 595, 425, 100)),
                6 to Pair(BaseStats(1012, 632, 452, 100), BaseStats(1086, 679, 485, 100))
            ),

            "Luocha" to mapOf(
                0 to Pair(BaseStats(174, 102, 49, 101), BaseStats(339, 200, 96, 101)),
                1 to Pair(BaseStats(409, 241, 116, 101), BaseStats(496, 293, 141, 101)),
                2 to Pair(BaseStats(566, 334, 160, 101), BaseStats(653, 386, 185, 101)),
                3 to Pair(BaseStats(723, 427, 205, 101), BaseStats(810, 478, 230, 101)),
                4 to Pair(BaseStats(879, 519, 249, 101), BaseStats(967, 571, 274, 101)),
                5 to Pair(BaseStats(1036, 612, 294, 101), BaseStats(1123, 664, 319, 101)),
                6 to Pair(BaseStats(1193, 705, 339, 101), BaseStats(1280, 756, 363, 101))
            ),

            "Seele" to mapOf(
                0 to Pair(BaseStats(126, 87, 49, 115), BaseStats(247, 169, 96, 115)),
                1 to Pair(BaseStats(297, 204, 116, 115), BaseStats(361, 248, 141, 115)),
                2 to Pair(BaseStats(411, 283, 160, 115), BaseStats(475, 326, 185, 115)),
                3 to Pair(BaseStats(525, 361, 205, 115), BaseStats(589, 405, 230, 115)),
                4 to Pair(BaseStats(639, 439, 249, 115), BaseStats(703, 483, 274, 115)),
                5 to Pair(BaseStats(753, 518, 294, 115), BaseStats(817, 561, 319, 115)),
                6 to Pair(BaseStats(868, 596, 339, 115), BaseStats(931, 640, 363, 115))
            ),
            "SilverWolf" to mapOf(
                0 to Pair(BaseStats(142, 87, 62, 107), BaseStats(277, 169, 122, 107)),
                1 to Pair(BaseStats(335, 204, 147, 107), BaseStats(406, 248, 178, 107)),
                2 to Pair(BaseStats(463, 283, 203, 107), BaseStats(534, 326, 235, 107)),
                3 to Pair(BaseStats(591, 361, 260, 107), BaseStats(662, 405, 291, 107)),
                4 to Pair(BaseStats(719, 439, 316, 107), BaseStats(791, 483, 347, 107)),
                5 to Pair(BaseStats(848, 518, 373, 107), BaseStats(919, 561, 404, 107)),
                6 to Pair(BaseStats(976, 596, 429, 107), BaseStats(1047, 640, 460, 107))
            ),

            "TrailblazerFire" to mapOf(
                0 to Pair(BaseStats(163, 84, 62, 100), BaseStats(319, 164, 122, 100)),
                1 to Pair(BaseStats(384, 198, 147, 100), BaseStats(466, 240, 178, 100)),
                2 to Pair(BaseStats(531, 274, 203, 100), BaseStats(613, 316, 235, 100)),
                3 to Pair(BaseStats(679, 350, 260, 100), BaseStats(761, 392, 291, 100)),
                4 to Pair(BaseStats(826, 426, 316, 100), BaseStats(908, 468, 347, 100)),
                5 to Pair(BaseStats(973, 502, 373, 100), BaseStats(1055, 544, 404, 100)),
                6 to Pair(BaseStats(1121, 578, 429, 100), BaseStats(1203, 620, 460, 100))
            ),
            "TrailblazerPhysical" to mapOf(
                0 to Pair(BaseStats(163, 84, 62, 100), BaseStats(319, 164, 122, 100)),
                1 to Pair(BaseStats(384, 198, 147, 100), BaseStats(466, 240, 178, 100)),
                2 to Pair(BaseStats(531, 274, 203, 100), BaseStats(613, 316, 235, 100)),
                3 to Pair(BaseStats(679, 350, 260, 100), BaseStats(761, 392, 291, 100)),
                4 to Pair(BaseStats(826, 426, 316, 100), BaseStats(908, 468, 347, 100)),
                5 to Pair(BaseStats(973, 502, 373, 100), BaseStats(1055, 544, 404, 100)),
                6 to Pair(BaseStats(1121, 578, 429, 100), BaseStats(1203, 620, 460, 100))
            ),

            "Welt" to mapOf(
                0 to Pair(BaseStats(153, 84, 69, 102), BaseStats(298, 164, 135, 102)),
                1 to Pair(BaseStats(359, 198, 162, 102), BaseStats(436, 240, 197, 102)),
                2 to Pair(BaseStats(497, 274, 225, 102), BaseStats(574, 316, 259, 102)),
                3 to Pair(BaseStats(635, 350, 287, 102), BaseStats(712, 392, 322, 102)),
                4 to Pair(BaseStats(773, 426, 349, 102), BaseStats(849, 468, 384, 102)),
                5 to Pair(BaseStats(911, 502, 412, 102), BaseStats(987, 544, 446, 102)),
                6 to Pair(BaseStats(1048, 578, 474, 102), BaseStats(1125, 620, 509, 102))
            ),
        )
    }

    data class CalcInfo(
        val stats: BaseStats,
        val hp: Values,
        val atk: Values,
        val def: Values,
        val spd: Values
    ) {
        data class Values(
            val pct: Float,
            val additive: Int,
        )
    }

    fun calcStatsWithBonuses(
        baseStats: BaseStats,
        addStats: List<Pair<String, Float>>
    ): CalcInfo {

        val hpPctBonus = addStats.sumOf { if (it.first == "hpPct") it.second else 0f }
        val additiveHpBonus = addStats.sumOf { if (it.first == "hp") it.second else 0f }.roundToInt()

        val totalHp = baseStats.hp * (1 + hpPctBonus) + additiveHpBonus

        val atkPctBonus = addStats.sumOf { if(it.first == "atkPct") it.second else 0f }
        val additiveAtkBonus = addStats.sumOf { if(it.first == "atk") it.second else 0f }.roundToInt()

        val totalAtk = baseStats.atk * (1 + atkPctBonus) + additiveAtkBonus

        val defPctBonus = addStats.sumOf { if(it.first == "defPct") it.second else 0f }
        val additiveDefBonus = addStats.sumOf { if(it.first == "def") it.second else 0f }.roundToInt()

        val totalDef = baseStats.def * (1 + defPctBonus) + additiveDefBonus

        val spdPctBonus = addStats.sumOf { if(it.first == "spdPct") it.second else 0f }
        val additiveSpdBonus = addStats.sumOf { if(it.first == "spd") it.second else 0f }.roundToInt()

        val totalSpd = baseStats.spd * (1 + spdPctBonus) + additiveSpdBonus

        return CalcInfo(
            stats = BaseStats(totalHp.roundToInt(), totalAtk.roundToInt(), totalDef.roundToInt(), totalSpd.roundToInt()),
            atk = CalcInfo.Values(atkPctBonus, additiveAtkBonus),
            def = CalcInfo.Values(defPctBonus, additiveDefBonus),
            hp = CalcInfo.Values(hpPctBonus, additiveHpBonus),
            spd = CalcInfo.Values(spdPctBonus, additiveSpdBonus)
        )
    }


    fun calcBaseStatsOrNull(name: String, level: Int, maxLevel: Int): BaseStats? {
        val ascension = levelRanges
            .indexOfFirst { range -> range.last == maxLevel }
            .takeIf { it != -1 }
            ?: return null

        val range = levelRanges[ascension].last - levelRanges[ascension].first
        val dif = maxLevel - level

        val (min, max) = stats[name]?.get(ascension) ?: return null

        return when(dif) {
            0 -> max
            range -> min
            else -> {

                val hpInc = (max.hp - min.hp) / range
                val atkInc = (max.atk - min.atk) / range
                val defInc = (max.def - min.def) / range

                BaseStats(
                    hp = min.hp + (hpInc * dif),
                    atk = min.atk + (atkInc * dif),
                    def = min.def + (defInc * dif),
                    spd = min.spd
                )
            }
        }
    }

    fun calcBaseStats(name: String, level: Int, maxLevel: Int): BaseStats {
        val ascension = levelRanges
            .indexOfFirst { range -> range.last == maxLevel }

        val range = levelRanges[ascension].last - levelRanges[ascension].first
        val dif = maxLevel - level

        val (min, max) = stats[name]!![ascension]!!

        return when(dif) {
            0 -> max
            range -> min
            else -> {

                val hpInc = (max.hp - min.hp) / range
                val atkInc = (max.atk - min.atk) / range
                val defInc = (max.def - min.def) / range

                BaseStats(
                    hp = min.hp + (hpInc * dif),
                    atk = min.atk + (atkInc * dif),
                    def = min.def + (defInc * dif),
                    spd = min.spd
                )
            }
        }
    }
}

private fun <E> List<E>.sumOf(function: (E) -> Float): Float {
    return this.sumOf { function(it) }
}
