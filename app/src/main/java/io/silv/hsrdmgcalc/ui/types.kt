package io.silv.hsrdmgcalc.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.Type



@Stable
@Immutable
data class UiCharacter(
    val name: String,
    val owned: Boolean,
    val level: Int,
    val ascension: Int,
    val traceSkill: Int,
    val traceBasicAtk: Int,
    val traceUltimate: Int,
    val traceTechnique: Int,
    val traceTalent: Int,
    val applyStatBonuses: Boolean,
    val type: Type,
    val path: Path,
    val is5star: Boolean,
)

@Stable
@Immutable
data class UiLightCone(
    val id: Long,
    val name: String,
    val level: Int,
    val superimpose: Int,
    val location: String?,
    val path: Path
)

@Stable
@Immutable
data class UiRelic(
    val id: Long,
    val set: String,
    val slot: String,
    val location: String?,
    val level: Int,
    val stats: List<Pair<String, Float>>
)
