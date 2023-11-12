package io.silv.hsrdmgcalc.data

import io.silv.hsrdmgcalc.ui.composables.Path
import io.silv.hsrdmgcalc.ui.composables.Type

object HonkaiConstants {

    const val CharacterListVersion = 1

    val characters by lazy {
        listOf(
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

    const val LightConeListVersion = 1

    val lightCones by lazy {
        listOf(
            "Adversarial" to Path.TheHunt,
            "Amber" to Path.Preservation,
            "Arrows" to Path.TheHunt,
            "ASecretVow" to Path.Destruction,
            "BeforeDawn" to Path.Erudition,
            "BeforeTheTutorialMissionStarts" to Path.Nihility,
            "BrighterThanTheSun" to Path.Destruction,
            "ButTheBattleIsntOver" to Path.Harmony,
            "CarveTheMoonWeaveTheClouds" to Path.Harmony,
            "Chorus" to Path.Harmony,
            "CollapsingSky" to Path.Destruction,
            "Cornucopia" to Path.Abundance,
            "CruisingInTheStellarSea" to Path.TheHunt,
            "DanceDance" to Path.Harmony,
            "DartingArrow" to Path.TheHunt,
            "DataBank" to Path.Erudition,
            "DayOneOfMyNewLife" to Path.Preservation,
            "Defense" to Path.Preservation,
            "EchoesOfTheCoffin" to Path.Abundance,
            "EyesOfThePrey" to Path.Nihility,
            "Fermata" to Path.Nihility,
            "FineFruit" to Path.Abundance,
            "GeniusesRepose" to Path.Erudition,
            "GoodNightAndSleepWell" to Path.Nihility,
            "HiddenShadow" to Path.Nihility,
            "IncessantRain" to Path.Nihility,
            "InTheNameOfTheWorld" to Path.Nihility,
            "InTheNight" to Path.TheHunt,
            "IShallBeMyOwnSword" to Path.Destruction,
            "LandausChoice" to Path.Preservation,
            "Loop" to Path.Nihility,
            "MakeTheWorldClamor" to Path.Erudition,
            "Mediation" to Path.Harmony,
            "MemoriesOfThePast" to Path.Harmony,
            "MeshingCogs" to Path.Harmony,
            "MomentOfVictory" to Path.Preservation,
            "Multiplication" to Path.Abundance,
            "MutualDemise" to Path.Destruction,
            "NightOnTheMilkyWay" to Path.Erudition,
            "NowhereToRun" to Path.Destruction,
            "OnlySilenceRemains" to Path.TheHunt,
            "OnTheFallOfAnAeon" to Path.Destruction,
            "Passkey" to Path.Erudition,
            "PastAndFuture" to Path.Harmony,
            "PatienceIsAllYouNeed" to Path.Nihility,
            "PerfectTiming" to Path.Abundance,
            "Pioneering" to Path.Preservation,
            "PlanetaryRendezvous" to Path.Harmony,
            "PostopConversation" to Path.Abundance,
            "QuidProQuo" to Path.Abundance,
            "ResolutionShinesAsPearlsOfSweat" to Path.Nihility,
            "ReturnToDarkness" to Path.TheHunt,
            "RiverFlowsInSpring" to Path.TheHunt,
            "Sagacity" to Path.Erudition,
            "SharedFeeling" to Path.Abundance,
            "ShatteredHome" to Path.Destruction,
            "SheAlreadyShutHerEyes" to Path.Preservation,
            "SleepLikeTheDead" to Path.TheHunt,
            "SolitaryHealing" to Path.Nihility,
            "SomethingIrreplaceable" to Path.Destruction,
            "SubscribeForMore" to Path.TheHunt,
            "Swordplay" to Path.TheHunt,
            "TextureOfMemories" to Path.Preservation,
            "TheBirthOfTheSelf" to Path.Erudition,
            "TheMolesWelcomeYou" to Path.Destruction,
            "TheSeriousnessOfBreakfast" to Path.Erudition,
            "TheUnreachableSide" to Path.Destruction,
            "ThisIsMe" to Path.Preservation,
            "TimeWaitsForNoOne" to Path.Abundance,
            "TodayIsAnotherPeacefulDay" to Path.Erudition,
            "TrendOfTheUniversalMarket" to Path.Preservation,
            "UnderTheBlueSky" to Path.Destruction,
            "Void" to Path.Nihility,
            "WarmthShortensColdNights" to Path.Abundance,
            "WeAreWildfire" to Path.Preservation,
            "WeWillMeetAgain" to Path.Nihility,
            "Woof!Walk!Time!" to Path.Destruction,
            "WorrisomeBlissful" to Path.TheHunt
        )
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

    val relics by lazy {
        listOf(
            "BandsAnkleBootsWithRivets",
            "BandsLeatherJacketWithStuds",
            "BandsPolarizedSunglasses",
            "BandsTouringBracelet",
            "BandOfSizzlingThunder",
            "BelobogsFortressOfPreservation",
            "BelobogsIronDefense",
            "BelobogOfTheArchitects",
            "BrokenKeel",
            "CelestialDifferentiator",

            "ChampionsChestGuard",
            "ChampionsFleetfootBoots",
            "ChampionsHeadgear",
            "ChampionsHeavyGloves",
            "ChampionOfStreetwiseBoxing",

            "DisciplesCelestialSilkSandals",
            "DisciplesDewyFeatherGarb",
            "DisciplesIngeniumHand",
            "DisciplesProstheticEye",
            "EaglesBeakedHelmet",
            "EaglesQuiltedPuttees",
            "EaglesSoaringRing",
            "EaglesWingedSuitHarness",
            "EagleOfTwilightLine",
            "FiresmithsAlloyLeg",
            "FiresmithsFireproofApron",
            "FiresmithsObsidianGoggles",
            "FiresmithsRingOfFlamemastery",
            "FiresmithOfLavaforging",
            "FleetOfTheAgeless",
            "GeniussFrequencyCatcher",
            "GeniussGravityWalker",
            "GeniussMetafieldSuit",
            "GeniussUltraremoteSensingVisor",
            "GeniusOfBrilliantStars",
            "GuardsCastIronHelmet",
            "GuardsShiningGauntlets",
            "GuardsSilverGreaves",
            "GuardsUniformOfOld",
            "GuardOfWutheringSnow",
            "HertasSpaceStation",
            "HertasWanderingTrek",

            "HuntersArtaiusHood",
            "HuntersIceDragonCloak",
            "HuntersLizardGloves",
            "HuntersSoftElkskinBoots",
            "HunterOfGlacialForest",

            "InertSalsotto",
            "InsumoususFrayedHawser",
            "InsumoususWhalefallShip",

            "KnightsForgivingCasque",
            "KnightsIronBootsOfOrder",
            "KnightsSilentOathRing",
            "KnightsSolemnBreastplate",
            "KnightOfPurityPalace",

            "LongevousDisciple",

            "MessengersHolovisor",
            "MessengersParkoolSneakers",
            "MessengersSecretSatchel",
            "MessengersTransformativeArm",
            "MessengerTraversingHackerspace",

            "MusketeersCoarseLeatherGloves",
            "MusketeersRivetsRidingBoots",
            "MusketeersWildWheatFeltHat",
            "MusketeersWindhuntingShawl",
            "MusketeerOfWildWheat",

            "PancosmicCommercialEnterprise",

            "PasserbysRaggedEmbroidedCoat",
            "PasserbysRejuvenatedWoodenHairstick",
            "PasserbysRoamingDragonBracer",
            "PasserbysStygianHikingBoots",
            "PasserbyOfWanderingCloud",

            "PlanetScrewllumsMechanicalSun",
            "PlanetScrewllumsRingSystem",
            "RutilantArena",
            "SalsottosMovingCity",
            "SalsottosTerminatorLine",
            "SpaceSealingStation",
            "SprightlyVonwacq",
            "TaikiyansArclightRaceTrack",
            "TaikiyanLaserStadium",

            "TaliasExposedElectricWire",
            "TaliasNailscrapTown",
            "TaliaKingdomOfBanditry",

            "TheIpcsMegaHq",
            "TheIpcsTradeRoute",

            "TheXianzhouLuofusAmbrosialArborVines",
            "TheXianzhouLuofusCelestialArk",

            "ThiefsGlovesWithPrints",
            "ThiefsMeteorBoots",
            "ThiefsMyriadfacedMask",
            "ThiefsSteelGrapplingHook",
            "ThiefOfShootingMeteor",

            "VonwacqsIslandicCoast",
            "VonwacqsIslandOfBirth",

            "WastelandersBreathingMask",
            "WastelandersDesertTerminal",
            "WastelandersFriarRobe",
            "WastelandersPoweredGreaves",
            "WastelanderOfBanditryDesert",
        )
    }
}