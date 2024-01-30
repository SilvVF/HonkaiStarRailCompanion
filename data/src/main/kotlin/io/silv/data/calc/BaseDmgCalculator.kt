package io.silv.data.calc

import io.silv.data.character.ATK
import io.silv.data.character.Attribute
import io.silv.data.character.BaseStats
import io.silv.data.character.Character
import io.silv.data.character.DEF
import io.silv.data.character.HP
import io.silv.data.character.SPD
import io.silv.data.character.get
import io.silv.data.lightcone.LightCone
import kotlin.math.roundToInt

/*

BASE STATS
HP Total = (Character Base HP + LC Base HP) * (1 + HP%) + Flat HP

ATK Total = (Character Base ATK + LC Base ATK) * (1 + ATK%) + Flat ATK

DEF Total = (Character Base DEF + LC Base DEF) * (1 + DEF%) + Flat DEF

Speed Total = Character Base Speed * (1 + Speed%) + Flat Speed
 */

typealias AttributeModifier = List<Pair<Attribute, Double>>


/**
 * Base DMG = (Skill Multiplier + Extra Multiplier) * Scaling Attribute + Extra DMG
 */
class BaseDmgCalculator {

    private fun AttributeModifier.sumOf(attribute: Attribute) =
        filter { it.first == attribute }.sumOf { it.second }.roundToInt()

    private operator fun AttributeModifier.get(attribute: Attribute): Int {
        return this.firstOrNull { attribute == it.first }?.second?.roundToInt() ?: 0
    }

    fun calculate(
        c: Character,
        tier: Int,
        lc: LightCone,
        flatStats: AttributeModifier,
        pctStats: AttributeModifier
    ): Double {
        val (high, low) = c.baseStats[tier] ?: error("tier not found")

        val base = BaseStats.of(high, low)

        val baseStats = base.copy(
            hp = ((base.hp + lc.baseHP) * (1 + pctStats[HP]) + flatStats.sumOf(HP)).roundToInt(),
            atk = ((base.atk + lc.baseATK) * (1 + pctStats[ATK]) + flatStats.sumOf(ATK)).roundToInt(),
            def = ((base.def  + lc.baseDEF) * (1 + pctStats[DEF]) + flatStats.sumOf(DEF)).roundToInt(),
            spd = base.spd * (1 + pctStats[SPD]) + flatStats.sumOf(SPD)
        )

       return (c.skillMultiplier + c.extraMultiplier) * baseStats[c.scalingAttribute] + c.extraDmg
    }
}