package io.silv.hsrdmgcalc.data

class ValidateCharacterLevel {

    operator fun invoke(level: Int, maxLevel: Int): Boolean {

        val levelRange = maxLevel
            .takeIf { max -> max in listOf(20, 40, 50, 60, 70, 80, 90) }
            ?.let { max ->
                CharacterStats.levelRanges.find { range -> range.last == max }
            }
            ?: return false

        return level !in levelRange
    }
}