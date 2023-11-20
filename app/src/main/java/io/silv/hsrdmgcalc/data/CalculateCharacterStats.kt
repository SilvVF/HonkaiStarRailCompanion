package io.silv.hsrdmgcalc.data




class CalculateCharacterStats {

    data class StatInfo(
        val stats: CharacterStats.BaseStats,
        val relResult: CharacterStats.CalcInfo,
        val lcResult: CharacterStats.CalcInfo
    )

    operator fun invoke(
        name: String,
        level: Int,
        maxLevel: Int
        ,
        relicsStats: List<Pair<String, Float>>,

        lightConeName: String?
    ): Result<StatInfo> {
        return runCatching{

            val baseStats = CharacterStats.calcBaseStats(name, level, maxLevel)

            val relicResult = CharacterStats.calcStatsWithBonuses(
                baseStats = baseStats,
                addStats = relicsStats
            )

            val lightConeResult = CharacterStats.calcStatsWithBonuses(
                baseStats = relicResult.stats,
                addStats = HonkaiUtils.getLightConeBonuses(lightConeName)
            )

            StatInfo(
                stats = lightConeResult.stats,
                relResult = relicResult,
                lcResult = lightConeResult
            )
        }
    }
}