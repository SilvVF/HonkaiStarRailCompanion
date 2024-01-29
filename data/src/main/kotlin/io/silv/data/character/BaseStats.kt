package io.silv.data.character


operator fun BaseStats.get(
    attribute: Attribute
): Double = when(attribute) {
    ATK -> atk
    DEF -> def
    HP -> hp
    SPD -> spd
}

data class BaseStats(
    val hp: Double,
    val atk: Double,
    val def: Double,
    val spd: Double
) {

    companion object {

        private inline fun avg(vararg nums: Double): Double {
            return nums.asList().sum() / nums.size
        }

        fun of(high: BaseStats, low: BaseStats): BaseStats {
            return BaseStats(
                hp = avg(high.hp, low.hp),
                atk = avg(high.atk, low.atk),
                def = avg(high.def, low.def),
                spd = avg(high.spd, low.spd)
            )
        }
    }
}
