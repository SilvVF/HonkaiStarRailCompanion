package io.silv.data.character


typealias HP = Int
typealias ATK = Int
typealias DEF = Int
typealias SPD = Int

data class BaseStats(
    val hp: HP,
    val atk: ATK,
    val def: DEF,
    val spd: SPD
)
