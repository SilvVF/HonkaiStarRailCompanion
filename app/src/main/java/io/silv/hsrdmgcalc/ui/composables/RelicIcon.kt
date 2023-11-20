package io.silv.hsrdmgcalc.ui.composables

import androidx.annotation.DrawableRes
import io.silv.hsrdmgcalc.R



@DrawableRes
fun getRelicsResource(name: String): Int {
    return when(name) {
        "BandsAnkleBootsWithRivets" -> R.drawable.item_bands_ankle_boots_with_rivets
        "BandsLeatherJacketWithStuds" -> R.drawable.item_bands_leather_jacket_with_studs
        "BandsPolarizedSunglasses" -> R.drawable.item_bands_polarized_sunglasses
        "BandsTouringBracelet" -> R.drawable.item_bands_touring_bracelet
        "BandOfSizzlingThunder" -> R.drawable.item_band_of_sizzling_thunder
        "BelobogsFortressOfPreservation" -> R.drawable.item_belobogs_fortress_of_preservation
        "BelobogsIronDefense" -> R.drawable.item_belobogs_iron_defense
        "BelobogOfTheArchitects" -> R.drawable.item_belobog_of_the_architects
        "BrokenKeel" -> R.drawable.item_broken_keel
        "CelestialDifferentiator" -> R.drawable.item_celestial_differentiator
        "ChampionsChestGuard" -> R.drawable.item_champions_chest_guard
        "ChampionsFleetfootBoots" -> R.drawable.item_champions_fleetfoot_boots
        "ChampionsHeadgear" -> R.drawable.item_champions_headgear
        "ChampionsHeavyGloves" -> R.drawable.item_champions_heavy_gloves
        "ChampionOfStreetwiseBoxing" -> R.drawable.item_champion_of_streetwise_boxing
        "DisciplesCelestialSilkSandals" -> R.drawable.item_disciples_celestial_silk_sandals
        "DisciplesDewyFeatherGarb" -> R.drawable.item_disciples_dewy_feather_garb
        "DisciplesIngeniumHand" -> R.drawable.item_disciples_ingenium_hand
        "DisciplesProstheticEye" -> R.drawable.item_disciples_prosthetic_eye
        "EaglesBeakedHelmet" -> R.drawable.item_eagles_beaked_helmet
        "EaglesQuiltedPuttees" -> R.drawable.item_eagles_quilted_puttees
        "EaglesSoaringRing" -> R.drawable.item_eagles_soaring_ring
        "EaglesWingedSuitHarness" -> R.drawable.item_eagles_winged_suit_harness
        "EagleOfTwilightLine" -> R.drawable.item_eagle_of_twilight_line
        "FiresmithsAlloyLeg" -> R.drawable.item_firesmiths_alloy_leg
        "FiresmithsFireproofApron" -> R.drawable.item_firesmiths_fireproof_apron
        "FiresmithsObsidianGoggles" -> R.drawable.item_firesmiths_obsidian_goggles
        "FiresmithsRingOfFlamemastery" -> R.drawable.item_firesmiths_ring_of_flamemastery
        "FiresmithOfLavaforging" -> R.drawable.item_firesmith_of_lavaforging
        "FleetOfTheAgeless" -> R.drawable.item_fleet_of_the_ageless
        "GeniussFrequencyCatcher" -> R.drawable.item_geniuss_frequency_catcher
        "GeniussGravityWalker" -> R.drawable.item_geniuss_gravity_walker
        "GeniussMetafieldSuit" -> R.drawable.item_geniuss_metafield_suit
        "GeniussUltraremoteSensingVisor" -> R.drawable.item_geniuss_ultraremote_sensing_visor
        "GeniusOfBrilliantStars" -> R.drawable.item_genius_of_brilliant_stars
        "GuardsCastIronHelmet" -> R.drawable.item_guards_cast_iron_helmet
        "GuardsShiningGauntlets" -> R.drawable.item_guards_shining_gauntlets
        "GuardsSilverGreaves" -> R.drawable.item_guards_silver_greaves
        "GuardsUniformOfOld" -> R.drawable.item_guards_uniform_of_old
        "GuardOfWutheringSnow" -> R.drawable.item_guard_of_wuthering_snow
        "HertasSpaceStation" -> R.drawable.item_hertas_space_station
        "HertasWanderingTrek" -> R.drawable.item_hertas_wandering_trek
        "HuntersArtaiusHood" -> R.drawable.item_hunters_artaius_hood
        "HuntersIceDragonCloak" -> R.drawable.item_hunters_ice_dragon_cloak
        "HuntersLizardGloves" -> R.drawable.item_hunters_lizard_gloves
        "HuntersSoftElkskinBoots" -> R.drawable.item_hunters_soft_elkskin_boots
        "HunterOfGlacialForest" -> R.drawable.item_hunter_of_glacial_forest
        "InertSalsotto" -> R.drawable.item_inert_salsotto
        "InsumoususFrayedHawser" -> R.drawable.item_insumousus_frayed_hawser
        "InsumoususWhalefallShip" -> R.drawable.item_insumousus_whalefall_ship
        "KnightsForgivingCasque" -> R.drawable.item_knights_forgiving_casque
        "KnightsIronBootsOfOrder" -> R.drawable.item_knights_iron_boots_of_order
        "KnightsSilentOathRing" -> R.drawable.item_knights_silent_oath_ring
        "KnightsSolemnBreastplate" -> R.drawable.item_knights_solemn_breastplate
        "KnightOfPurityPalace" -> R.drawable.item_knight_of_purity_palace
        "LongevousDisciple" -> R.drawable.item_longevous_disciple
        "MessengersHolovisor" -> R.drawable.item_messengers_holovisor
        "MessengersParkoolSneakers" -> R.drawable.item_messengers_parkool_sneakers
        "MessengersSecretSatchel" -> R.drawable.item_messengers_secret_satchel
        "MessengersTransformativeArm" -> R.drawable.item_messengers_transformative_arm
        "MessengerTraversingHackerspace" -> R.drawable.item_messenger_traversing_hackerspace
        "MusketeersCoarseLeatherGloves" -> R.drawable.item_musketeers_coarse_leather_gloves
        "MusketeersRivetsRidingBoots" -> R.drawable.item_musketeers_rivets_riding_boots
        "MusketeersWildWheatFeltHat" -> R.drawable.item_musketeers_wild_wheat_felt_hat
        "MusketeersWindhuntingShawl" -> R.drawable.item_musketeers_windhunting_shawl
        "MusketeerOfWildWheat" -> R.drawable.item_musketeer_of_wild_wheat
        "PancosmicCommercialEnterprise" -> R.drawable.item_pancosmic_commercial_enterprise
        "PasserbysRaggedEmbroidedCoat" -> R.drawable.item_passerbys_ragged_embroided_coat
        "PasserbysRejuvenatedWoodenHairstick" -> R.drawable.item_passerbys_rejuvenated_wooden_hairstick
        "PasserbysRoamingDragonBracer" -> R.drawable.item_passerbys_roaming_dragon_bracer
        "PasserbysStygianHikingBoots" -> R.drawable.item_passerbys_stygian_hiking_boots
        "PasserbyOfWanderingCloud" -> R.drawable.item_passerby_of_wandering_cloud
        "PlanetScrewllumsMechanicalSun" -> R.drawable.item_planet_screwllums_mechanical_sun
        "PlanetScrewllumsRingSystem" -> R.drawable.item_planet_screwllums_ring_system
        "RutilantArena" -> R.drawable.item_rutilant_arena
        "SalsottosMovingCity" -> R.drawable.item_salsottos_moving_city
        "SalsottosTerminatorLine" -> R.drawable.item_salsottos_terminator_line
        "SpaceSealingStation" -> R.drawable.item_space_sealing_station
        "SprightlyVonwacq" -> R.drawable.item_sprightly_vonwacq
        "TaikiyansArclightRaceTrack" -> R.drawable.item_taikiyans_arclight_race_track
        "TaikiyanLaserStadium" -> R.drawable.item_taikiyan_laser_stadium
        "TaliasExposedElectricWire" -> R.drawable.item_talias_exposed_electric_wire
        "TaliasNailscrapTown" -> R.drawable.item_talias_nailscrap_town
        "TaliaKingdomOfBanditry" -> R.drawable.item_talia_kingdom_of_banditry
        "TheIpcsMegaHq" -> R.drawable.item_the_ipcs_mega_hq
        "TheIpcsTradeRoute" -> R.drawable.item_the_ipcs_trade_route
        "TheXianzhouLuofusAmbrosialArborVines" -> R.drawable.item_the_xianzhou_luofus_ambrosial_arbor_vines
        "TheXianzhouLuofusCelestialArk" -> R.drawable.item_the_xianzhou_luofus_celestial_ark
        "ThiefsGlovesWithPrints" -> R.drawable.item_thiefs_gloves_with_prints
        "ThiefsMeteorBoots" -> R.drawable.item_thiefs_meteor_boots
        "ThiefsMyriadfacedMask" -> R.drawable.item_thiefs_myriadfaced_mask
        "ThiefsSteelGrapplingHook" -> R.drawable.item_thiefs_steel_grappling_hook
        "ThiefOfShootingMeteor" -> R.drawable.item_thief_of_shooting_meteor
        "VonwacqsIslandicCoast" -> R.drawable.item_vonwacqs_islandic_coast
        "VonwacqsIslandOfBirth" -> R.drawable.item_vonwacqs_island_of_birth
        "WastelandersBreathingMask" -> R.drawable.item_wastelanders_breathing_mask
        "WastelandersDesertTerminal" -> R.drawable.item_wastelanders_desert_terminal
        "WastelandersFriarRobe" -> R.drawable.item_wastelanders_friar_robe
        "WastelandersPoweredGreaves" -> R.drawable.item_wastelanders_powered_greaves
        "WastelanderOfBanditryDesert" -> R.drawable.item_wastelander_of_banditry_desert
        else -> R.drawable.relic_selected
    }
}