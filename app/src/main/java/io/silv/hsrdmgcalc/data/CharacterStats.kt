package io.silv.hsrdmgcalc.data

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

            "Kafka" to mapOf(),
        )
    }
}