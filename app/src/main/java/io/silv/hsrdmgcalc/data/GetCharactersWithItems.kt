package io.silv.hsrdmgcalc.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastFirstOrNull
import io.silv.honkai.Character
import io.silv.honkai.LightCone
import io.silv.honkai.Relic
import io.silv.hsrdmgcalc.ui.UiCharacter
import io.silv.hsrdmgcalc.ui.UiLightCone
import io.silv.hsrdmgcalc.ui.UiRelic
import io.silv.hsrdmgcalc.ui.composables.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine

class GetCharactersWithItems(
    private val dataRepository: HonkaiDataRepository
) {

    operator fun invoke() = combine(
        dataRepository.observeAllCharacters(),
        dataRepository.observeAllRelics(),
        dataRepository.observeAllLightCones()
    ) { characters, relics, lightCones ->

        characters.map { character ->
            CharacterWithItems(
                character = character.toUi(),
                lightCone = lightCones
                    .fastFirstOrNull { lightCone -> lightCone.location == character.name }
                    ?.toUi(),
                relics = relics
                    .filter { relic -> relic.location == character.name }
                    .map { relic -> relic.toUi() }
                    .toImmutableList()
            )
        }
    }
}

class GetCharacterWithItems(
    private val dataRepository: HonkaiDataRepository
) {

    fun invoke(name: String) = combine(
        dataRepository.observeCharacterByName(name),
        dataRepository.observeLightConeForCharacter(name),
        dataRepository.observeRelicsForCharacter(name)
    ) { character, lightCone, relics ->

        CharacterWithItems(
            character = character.toUi(),
            lightCone = lightCone?.toUi(),
            relics = relics
                .map { relic -> relic.toUi() }
                .toImmutableList()
        )
    }
}

fun Character.toUi(): UiCharacter {
    return UiCharacter(
        name = name,
        owned = owned,
        level = level.toInt(),
        maxLevel = maxLevel.toInt(),
        traceSkill = trace_skill.toInt(),
        traceBasicAtk = trace_basic_atk.toInt(),
        traceUltimate = trace_ultimate.toInt(),
        traceTechnique = trace_technique.toInt(),
        traceTalent = trace_talent.toInt(),
        applyStatBonuses = apply_stat_bonuses,
        type = HonkaiConstants.characterType(name),
        path = HonkaiConstants.characterPath(name),
        is5star = HonkaiConstants.is5Star(name),
    )
}

fun LightCone.toUi(): UiLightCone {
    return UiLightCone(
        id = id,
        name = name,
        level = level.toInt(),
        superimpose = superimpose.toInt(),
        location = location,
        path = Path.Nihility
    )
}

fun Relic.toUi(): UiRelic {
    return UiRelic(
        id = id,
        set = relic_set,
        slot = slot,
        location = location,
        level = level.toInt(),
        stats = stats
    )
}

@Stable
@Immutable
data class CharacterWithItems(
    val character: UiCharacter,
    val lightCone: UiLightCone?,
    val relics: ImmutableList<UiRelic>
)