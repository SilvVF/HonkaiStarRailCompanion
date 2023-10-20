package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import io.silv.hsrdmgcalc.R

@Composable
fun CharacterSplashArt(
    character: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = characterPainterResource(character = character),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

@Composable
private fun characterPainterResource(character: String): Painter {

    val characterLower = remember { character.lowercase() }

    return painterResource(
        id = when (characterLower) {
            "bailu" -> R.drawable.character_bailu_splash_art
            "blade" -> R.drawable.character_blade_splash_art
            "bronya" -> R.drawable.character_bronya_splash_art
            "clara" -> R.drawable.character_clara_splash_art
            "danhengimbibitorlunae" -> R.drawable.character_dan_heng_imbibitor_lunae_splash_art
            "fuxuan" -> R.drawable.character_fu_xuan_splash_art
            "gepard" -> R.drawable.character_gepard_splash_art
            "himeko" -> R.drawable.character_himeko_splash_art
            "jingyuan" -> R.drawable.character_jing_yuan_splash_art
            "jingliu" -> R.drawable.character_jingliu_splash_art
            "kafka" -> R.drawable.character_kafka_splash_art
            "luocha" -> R.drawable.character_luocha_splash_art
            "seele" -> R.drawable.character_seele_splash_art
            "silverwolf" -> R.drawable.character_silver_wolf_splash_art
            "trailblazer" -> R.drawable.character_trailblazer_splash_art
            "welt" -> R.drawable.character_welt_splash_art
            "yanqing" -> R.drawable.character_yanqing_splash_art
            "arlan" -> R.drawable.character_arlan_splash_art
            "asta" -> R.drawable.character_asta_splash_art
            "danheng" -> R.drawable.character_dan_heng_splash_art
            "herta" -> R.drawable.character_herta_splash_art
            "hook" -> R.drawable.character_hook_splash_art
            "luka" -> R.drawable.character_luka_splash_art
            "lynx" -> R.drawable.character_lynx_splash_art
            "march7th" -> R.drawable.character_march_7th_splash_art
            "natasha" -> R.drawable.character_natasha_splash_art
            "pela" -> R.drawable.character_pela_splash_art
            "qingque" -> R.drawable.character_qingque_splash_art
            "sampo" -> R.drawable.character_sampo_splash_art
            "serval" -> R.drawable.character_serval_splash_art
            "sushang" -> R.drawable.character_sushang_splash_art
            "tingyun" -> R.drawable.character_tingyun_splash_art
            "yukong" -> R.drawable.character_yukong_splash_art
            else -> R.drawable.character_trailblazer_splash_art
        }
    )
}