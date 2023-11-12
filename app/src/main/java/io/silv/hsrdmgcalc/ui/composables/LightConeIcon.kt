package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import io.silv.hsrdmgcalc.R

@Composable
fun LightConeIcon(
    name: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {

    Image(
        painter = lightConePainterResource(name = name),
        contentDescription = name,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter,
    )
}

@Composable
private fun lightConePainterResource(name: String): Painter {


    return painterResource(
        id = when (name) {
            "Adversarial" -> R.drawable.light_cone_adversarial
            "Amber" -> R.drawable.light_cone_amber
            "Arrows" -> R.drawable.light_cone_arrows
            "ASecretVow" -> R.drawable.light_cone_a_secret_vow
            "BeforeDawn" -> R.drawable.light_cone_before_dawn
            "BeforeTheTutorialMissionStarts" -> R.drawable.light_cone_before_the_tutorial_mission_starts
            "BrighterThanTheSun" -> R.drawable.light_cone_brighter_than_the_sun
            "ButTheBattleIsntOver" -> R.drawable.light_cone_but_the_battle_isnt_over
            "CarveTheMoonWeaveTheClouds" -> R.drawable.light_cone_carve_the_moonc_weave_the_clouds
            "Chorus" -> R.drawable.light_cone_chorus
            "CollapsingSky" -> R.drawable.light_cone_collapsing_sky
            "Cornucopia" -> R.drawable.light_cone_cornucopia
            "CruisingInTheStellarSea" -> R.drawable.light_cone_cruising_in_the_stellar_sea
            "DanceDance" -> R.drawable.light_cone_dance21_dance21
            "DartingArrow" -> R.drawable.light_cone_darting_arrow
            "DataBank" -> R.drawable.light_cone_data_bank
            "DayOneOfMyNewLife" -> R.drawable.light_cone_day_one_of_my_new_life
            "Defense" -> R.drawable.light_cone_defense
            "EchoesOfTheCoffin" -> R.drawable.light_cone_echoes_of_the_coffin
            "EyesOfThePrey" -> R.drawable.light_cone_eyes_of_the_prey
            "Fermata" -> R.drawable.light_cone_fermata
            "FineFruit" -> R.drawable.light_cone_fine_fruit
            "GeniusesRepose" -> R.drawable.light_cone_geniuses_repose
            "GoodNightAndSleepWell" -> R.drawable.light_cone_good_night_and_sleep_well
            "HiddenShadow" -> R.drawable.light_cone_hidden_shadow
            "IncessantRain" -> R.drawable.light_cone_incessant_rain
            "InTheNameOfTheWorld" -> R.drawable.light_cone_in_the_name_of_the_world
            "InTheNight" -> R.drawable.light_cone_in_the_night
            "IShallBeMyOwnSword" -> R .drawable.light_cone_i_shall_be_my_own_sword
            "LandausChoice" -> R.drawable.light_cone_landaus_choice
            "Loop" -> R.drawable.light_cone_loop
            "MakeTheWorldClamor" -> R.drawable.light_cone_make_the_world_clamor
            "Mediation" -> R.drawable.light_cone_mediation
            "MemoriesOfThePast" -> R.drawable.light_cone_memories_of_the_past
            "MeshingCogs" -> R.drawable.light_cone_meshing_cogs
            "MomentOfVictory" -> R.drawable.light_cone_moment_of_victory
            "Multiplication" -> R.drawable.light_cone_multiplication
            "MutualDemise" -> R.drawable.light_cone_mutual_demise
            "NightOnTheMilkyWay" -> R.drawable.light_cone_night_on_the_milky_way
            "NowhereToRun" -> R.drawable.light_cone_nowhere_to_run
            "OnlySilenceRemains" -> R.drawable.light_cone_only_silence_remains
            "OnTheFallOfAnAeon" -> R.drawable.light_cone_on_the_fall_of_an_aeon
            "Passkey" -> R.drawable.light_cone_passkey
            "PastAndFuture" -> R.drawable.light_cone_past_and_future
            "PatienceIsAllYouNeed" -> R.drawable.light_cone_patience_is_all_you_need
            "PerfectTiming" -> R.drawable.light_cone_perfect_timing
            "Pioneering" -> R.drawable.light_cone_pioneering
            "PlanetaryRendezvous" -> R.drawable.light_cone_planetary_rendezvous
            "PostopConversation" -> R.drawable.light_cone_postop_conversation
            "QuidProQuo" -> R.drawable.light_cone_quid_pro_quo
            "ResolutionShinesAsPearlsOfSweat" -> R.drawable.light_cone_resolution_shines_as_pearls_of_sweat
            "ReturnToDarkness" -> R.drawable.light_cone_return_to_darkness
            "RiverFlowsInSpring" -> R.drawable.light_cone_river_flows_in_spring
            "Sagacity" -> R.drawable.light_cone_sagacity
            "SharedFeeling" -> R.drawable.light_cone_shared_feeling
            "ShatteredHome" -> R.drawable.light_cone_shattered_home
            "SheAlreadyShutHerEyes" -> R.drawable.light_cone_she_already_shut_her_eyes
            "SleepLikeTheDead" -> R.drawable.light_cone_sleep_like_the_dead
            "SolitaryHealing" -> R.drawable.light_cone_solitary_healing
            "SomethingIrreplaceable" -> R.drawable.light_cone_something_irreplaceable
            "SubscribeForMore" -> R.drawable.light_cone_subscribe_for_more
            "Swordplay" -> R.drawable.light_cone_swordplay
            "TextureOfMemories" -> R.drawable.light_cone_texture_of_memories
            "TheBirthOfTheSelf" -> R.drawable.light_cone_the_birth_of_the_self
            "TheMolesWelcomeYou" -> R.drawable.light_cone_the_moles_welcome_you
            "TheSeriousnessOfBreakfast" -> R.drawable.light_cone_the_seriousness_of_breakfast
            "TheUnreachableSide" -> R.drawable.light_cone_the_unreachable_side
            "ThisIsMe" -> R.drawable.light_cone_this_is_me
            "TimeWaitsForNoOne" -> R.drawable.light_cone_time_waits_for_no_one
            "TodayIsAnotherPeacefulDay" -> R.drawable.light_cone_today_is_another_peaceful_day
            "TrendOfTheUniversalMarket" -> R.drawable.light_cone_trend_of_the_universal_market
            "UnderTheBlueSky" -> R.drawable.light_cone_under_the_blue_sky
            "Void" -> R.drawable.light_cone_void
            "WarmthShortensColdNights" -> R.drawable.light_cone_warmth_shortens_cold_nights
            "WeAreWildfire" -> R.drawable.light_cone_we_are_wildfire
            "WeWillMeetAgain" -> R.drawable.light_cone_we_will_meet_again
            "Woof!Walk!Time!" -> R.drawable.light_cone_woof21
            "WorrisomeBlissful" -> R.drawable.light_cone_worrisome_blissful
            else -> R.drawable.light_cone_selected
        }
    )
}