package io.silv.data.constants

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import io.silv.data.character.Character
import io.silv.data.character.characters.Bailu
import io.silv.data.character.characters.Blade
import io.silv.data.character.characters.Bronya
import io.silv.data.character.characters.Clara
import io.silv.data.character.characters.DanHeng
import io.silv.data.character.characters.FuXuan
import io.silv.data.character.characters.JingLiu
import io.silv.data.character.characters.Kafka
import io.silv.data.character.characters.Luocha
import io.silv.data.character.characters.Lynx
import io.silv.data.character.characters.Natasha
import io.silv.data.character.characters.Pela
import io.silv.data.character.characters.Qingque
import io.silv.data.character.characters.Serval
import io.silv.data.character.characters.SilverWolf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class Path {
    Destruction,
    TheHunt,
    Erudition,
    Harmony,
    Nihility,
    Preservation,
    Abundance,
}

val IMAGINARY = Color(0xffF8EB70)
val PHYSICAL = Color(0xffC5C5C5)
val LIGHTNING = Color(0xffDB77F4)
val FIRE = Color(0xffE62929)
val ICE = Color(0xff8BD4EF)
val WIND = Color(0xff87DAA7)
val QUANTUM = Color(0xff746DD1)

enum class Type {
    Physical,
    Fire,
    Ice,
    Lightning,
    Wind,
    Quantum,
    Imaginary,
}

object HonkaiConstants {

    const val CharacterListVersion = 1
    const val LightConeListVersion = 1

    @Stable
    val characters: ImmutableList<Character> by lazy {
        persistentListOf(
            Bailu,
            Blade,
            Bronya,
            Clara,
            DanHeng,
            FuXuan,
            JingLiu,
            Kafka,
            Luocha,
            SilverWolf,
            Lynx,
            Natasha,
            Pela,
            Qingque,
            Serval
        )
    }

    enum class Piece { Head, Hand, Body, Feet, Rope, Sphere }
}