package io.silv.hsrdmgcalc.data

import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.Type

object HonkaiConstants {

    val characters = listOf(
        "Bailu",
        "Blade",
        "Bronya",
        "Clara",
        "DanHengImbibitorLunae",
        "FuXuan",
        "Gepard",
        "Himeko",
        "JingYuan",
        "Jingliu",
        "Kafka",
        "Luocha",
        "Seele",
        "SilverWolf",
        "TrailblazerFire",
        "TrailblazerPhysical",
        "Welt",
        "Yanqing",
        "Arlan",
        "Asta",
        "DanHeng",
        "Herta",
        "Hook",
        "Luka",
        "Lynx",
        "March7th",
        "Natasha",
        "Pela",
        "Qingque",
        "Sampo",
        "Serval",
        "Sushang",
        "Tingyun",
        "Yukong",
    )

    fun characterType(name: String): Type {
        return when (name.lowercase()) {
            in listOf("asta", "guinaifen", "himeko", "hook", "trailblazerfire") -> Type.Fire
            in listOf("gepard", "herta", "jingliu", "march7th", "pela", "yanqing") -> Type.Ice
            in listOf("arlan", "bailu", "jingyuan", "kafka", "serval", "tingyun") -> Type.Lightning
            in listOf("blade", "bronya", "danheng", "huohuo", "sampo") -> Type.Wind
            in listOf("fuxuan", "lynx", "qingque", "seele", "silverwolf") -> Type.Quantum
            in listOf("danhengimbibitorlunae", "luocha", "welt", "yukong") -> Type.Imaginary
            else -> Type.Physical
        }
    }

    fun is5Star(name: String): Boolean {
        return name in listOf(
            "Bailu", "Blade", "Bronya", "Clara", "DanHengImbibitorLunae",
            "FuXuan", "Gepard", "Himeko", "JingYuan", "Jingliu", "Kafka",
            "Luocha", "Seele", "SilverWolf", "TrailblazerFire",
            "TrailblazerPhysical", "Welt", "Yanqing")
    }

    fun characterPath(name: String): Path {
        return when(name.lowercase()) {
            in listOf("bailu", "huohuo", "luocha", "lynx", "natasha") -> Path.Abundance
            in listOf("arlan", "blade", "clara", "danhengimbibitorlunae", "hook", "jingliu", "trailblazerphysical") -> Path.Destruction
            in listOf("argenti", "herta", "himeko", "jingyuan", "qingque", "serval") -> Path.Erudition
            in listOf("asta", "bronya", "hanya", "tingyun", "yukong") -> Path.Harmony
            in listOf("danheng", "seele", "sushang", "topaz&numby", "yanqing") -> Path.TheHunt
            in listOf("guinaifen", "kafka", "luka", "pela", "sampo", "silverwolf", "welt") -> Path.Nihility
            else -> Path.Preservation
        }
    }
}