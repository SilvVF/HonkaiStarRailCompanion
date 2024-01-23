package io.silv.data.constants

import androidx.compose.ui.graphics.Color
import io.silv.data.constants.HonkaiConstants.Piece.Body
import io.silv.data.constants.HonkaiConstants.Piece.Feet
import io.silv.data.constants.HonkaiConstants.Piece.Hand
import io.silv.data.constants.HonkaiConstants.Piece.Head
import io.silv.data.constants.HonkaiConstants.Piece.Rope
import io.silv.data.constants.HonkaiConstants.Piece.Sphere

enum class Path(val color: Color) {
    Destruction(PHYSICAL),
    TheHunt(PHYSICAL),
    Erudition(PHYSICAL),
    Harmony(PHYSICAL),
    Nihility(PHYSICAL),
    Preservation(PHYSICAL),
    Abundance(PHYSICAL),
}

val IMAGINARY = Color(0xffF8EB70)
val PHYSICAL = Color(0xffC5C5C5)
val LIGHTNING = Color(0xffDB77F4)
val FIRE = Color(0xffE62929)
val ICE = Color(0xff8BD4EF)
val WIND = Color(0xff87DAA7)
val QUANTUM = Color(0xff746DD1)

enum class Type(val color: Color) {
    Physical(PHYSICAL),
    Fire(FIRE),
    Ice(ICE),
    Lightning(LIGHTNING),
    Wind(WIND),
    Quantum(QUANTUM),
    Imaginary(IMAGINARY),
}

object HonkaiConstants {

    const val CharacterListVersion = 1
    const val LightConeListVersion = 1

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

    val lightConeToPathMap by lazy { lightCones.toMap() }

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

    enum class Piece { Head, Hand, Body, Feet, Rope, Sphere }

    val relics by lazy {
        mapOf(
            "BandOfSizzlingThunder" to mapOf(
                Feet to "BandsAnkleBootsWithRivets",
                Body to "BandsLeatherJacketWithStuds",
                Head to "BandsPolarizedSunglasses",
                Hand to "BandsTouringBracelet",
            ),


            "BelobogOfTheArchitects" to mapOf(
                Sphere to "BelobogsFortressOfPreservation",
                Rope to "BelobogsIronDefense",
            ),

            "BrokenKeel" to mapOf(
                Sphere to "InsumoususWhalefallShip",
                Rope to "InsumoususFrayedHawser"
            ),


            "CelestialDifferentiator" to mapOf(
                Sphere to "PlanetScrewllumsMechanicalSun",
                Rope to "PlanetScrewllumsRingSystem"
            ),

            "ChampionOfStreetwiseBoxing" to mapOf(
                Body to "ChampionsChestGuard",
                Feet to "ChampionsFleetfootBoots",
                Head to "ChampionsHeadgear",
                Hand to "ChampionsHeavyGloves",
            ),


            "LongevousDisciple" to mapOf(
                Feet to "DisciplesCelestialSilkSandals",
                Body to "DisciplesDewyFeatherGarb",
                Hand to "DisciplesIngeniumHand",
                Head to "DisciplesProstheticEye",
            ),

            "EagleOfTwilightLine" to mapOf(
                Head to "EaglesBeakedHelmet",
                Feet to "EaglesQuiltedPuttees",
                Hand to "EaglesSoaringRing",
                Body to "EaglesWingedSuitHarness",
            ),


            "FiresmithOfLavaforging" to mapOf(
                Feet to "FiresmithsAlloyLeg",
                Body to "FiresmithsFireproofApron",
                Head to "FiresmithsObsidianGoggles",
                Hand to "FiresmithsRingOfFlamemastery",
            ),

            "FleetOfTheAgeless" to mapOf(
                Rope to "TheXianzhouLuofusAmbrosialArborVines",
                Sphere to "TheXianzhouLuofusCelestialArk",
            ),

            "GeniusOfBrilliantStars" to mapOf(
                Hand to "GeniussFrequencyCatcher",
                Feet to "GeniussGravityWalker",
                Body to "GeniussMetafieldSuit",
                Head to "GeniussUltraremoteSensingVisor",
            ),

            "GuardOfWutheringSnow" to mapOf(
                Head to "GuardsCastIronHelmet",
                Hand to "GuardsShiningGauntlets",
                Feet to "GuardsSilverGreaves",
                Body to "GuardsUniformOfOld",
            ),

            "SpaceSealingStation" to mapOf(
                Sphere to "HertasSpaceStation",
                Rope to "HertasWanderingTrek",
            ),

            "HunterOfGlacialForest" to mapOf(
                Head to "HuntersArtaiusHood",
                Body to "HuntersIceDragonCloak",
                Hand to "HuntersLizardGloves",
                Feet to "HuntersSoftElkskinBoots",
            ),

            "InertSalsotto" to mapOf(
                Sphere to "SalsottosMovingCity",
                Rope to "SalsottosTerminatorLine",
            ),


            "KnightOfPurityPalace" to mapOf(
                Head to "KnightsForgivingCasque",
                Feet to "KnightsIronBootsOfOrder",
                Hand to "KnightsSilentOathRing",
                Body to "KnightsSolemnBreastplate",
            ),

            "MessengerTraversingHackerspace" to mapOf(
                Head to  "MessengersHolovisor",
                Feet to "MessengersParkoolSneakers",
                Body to "MessengersSecretSatchel",
                Hand to "MessengersTransformativeArm"
            ),

            "MusketeerOfWildWheat" to mapOf(
                Hand to "MusketeersCoarseLeatherGloves",
                Feet to "MusketeersRivetsRidingBoots",
                Head to "MusketeersWildWheatFeltHat",
                Body to "MusketeersWindhuntingShawl",
            ),


            "PancosmicCommercialEnterprise" to mapOf(
                Sphere to "TheIpcsMegaHq",
                Rope to "TheIpcsTradeRoute",
            ),

            "PasserbyOfWanderingCloud" to mapOf(
                Body to "PasserbysRaggedEmbroidedCoat",
                Head to "PasserbysRejuvenatedWoodenHairstick",
                Hand to "PasserbysRoamingDragonBracer",
                Feet to "PasserbysStygianHikingBoots",
            ),

            "RutilantArena" to mapOf(
                Rope to "TaikiyansArclightRaceTrack",
                Sphere to "TaikiyanLaserStadium",
            ),

            "SprightlyVonwacq" to mapOf(
                Rope to "VonwacqsIslandicCoast",
                Sphere to "VonwacqsIslandOfBirth",
            ),

            "TaliaKingdomOfBanditry" to mapOf(
                Rope to "TaliasExposedElectricWire",
                Sphere to "TaliasNailscrapTown",
            ),

            "ThiefOfShootingMeteor" to mapOf(
                Hand to "ThiefsGlovesWithPrints",
                Feet to "ThiefsMeteorBoots",
                Head to "ThiefsMyriadfacedMask",
                Body to "ThiefsSteelGrapplingHook",
            ),

            "WastelanderOfBanditryDesert" to mapOf(
                Hand to  "WastelandersDesertTerminal",
                Body to "WastelandersFriarRobe",
                Feet to "WastelandersPoweredGreaves",
                Head to "WastelandersBreathingMask"
            ),
        )
    }
}