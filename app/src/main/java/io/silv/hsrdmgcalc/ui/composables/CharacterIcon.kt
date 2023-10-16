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
fun CharacterIcon(
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
            "bailu" -> R.drawable.character_bailu
            "blade" -> R.drawable.character_blade
            "bronya" -> R.drawable.character_bronya
            "clara" -> R.drawable.character_clara
            "danhengimbibitorlunae" -> R.drawable.character_dan_heng_imbibitor
            "fuxuan" -> R.drawable.character_fu_xuan
            "gepard" -> R.drawable.character_gepard
            "himeko" -> R.drawable.character_himeko
            "jingyuan" -> R.drawable.character_jing_yuan
            "jingliu" -> R.drawable.character_jingliu
            "kafka" -> R.drawable.character_kafka
            "luocha" -> R.drawable.character_luocha
            "seele" -> R.drawable.character_seele
            "silverwolf" -> R.drawable.character_silver_wolf
            "trailblazer" -> R.drawable.character_trailblazer
            "welt" -> R.drawable.character_welt
            "yanqing" -> R.drawable.character_yanqing
            "arlan" -> R.drawable.character_arlan
            "asta" -> R.drawable.character_asta
            "danheng" -> R.drawable.character_dan_heng
            "herta" -> R.drawable.character_herta
            "hook" -> R.drawable.character_hook
            "luka" -> R.drawable.character_luka
            "lynx" -> R.drawable.character_lynx
            "march7th" -> R.drawable.character_march7th
            "natasha" -> R.drawable.character_natasha
            "pela" -> R.drawable.character_pela
            "qingque" -> R.drawable.character_qingque
            "sampo" -> R.drawable.character_sampo
            "serval" -> R.drawable.character_serval
            "sushang" -> R.drawable.character_sushang
            "tingyun" -> R.drawable.character_tingyun
            "yukong" -> R.drawable.character_yukong
            else -> R.drawable.character_trailblazer
        }
    )
}