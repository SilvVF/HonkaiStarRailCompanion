package io.silv.data.constants


object HonkaiUtils {

    fun getLightConeBonuses(name: String?): List<Pair<String, Float>> {
        return emptyList()
    }

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

    fun lightConeStars(name: String): Int {
        return when (name) {
            in listOf(
                "Adversarial",
                "Amber",
                "Arrows",
                "Chorus",
                "CollapsingSky",
                "Cornucopia",
                "DartingArrow",
                "DataBank",
                "Defense",
                "FineFruit",
                "HiddenShadow",
                "Loop",
                "Meditation",
                "MeshingCogs",
                "Multiplication",
                "MutualDemise",
                "Passkey",
                "Pioneering",
                "Sagacity",
                "ShatteredHome",
                "Void"
            ) -> 3
            in listOf(
                "BeforeDawn",
                "BrighterThanTheSun",
                "ButTheBattleInstOver",
                "CruisingInTheStellarSea",
                "EchoesOfTheCoffin",
                "IShallBeMyOwnSword",
                "InTheNameOfTheWorld",
                "InTheNight",
                "IncessantRain",
                "MomentOfVictory",
                "NightOnTheMilkyWay",
                "OnTheFallOfAnAeon",
                "PatienceIsAllYouNeed",
                "SheAlreadyShutHerEyes",
                "SleepLikeTheDead",
                "SolitaryHealing",
                "SomethingIrreplaceable",
                "TextureOfTheMemories",
                "TheUnreachableSide",
                "TimeWaitsForNoOne",
                "WorrisomeBlissful"
            ) -> 5
            else -> 4
        }
    }

}