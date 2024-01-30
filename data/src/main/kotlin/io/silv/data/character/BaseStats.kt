package io.silv.data.character


operator fun BaseStats.get(
    attribute: Attribute
): Double = when(attribute) {
    ATK -> atk.toDouble()
    DEF -> def.toDouble()
    HP -> hp.toDouble()
    SPD -> spd.toDouble()
}

data class BaseStats(
    val hp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int
) {

    companion object {

        private fun avg(vararg nums: Int): Int {
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
