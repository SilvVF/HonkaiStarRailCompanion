package io.silv.hsrdmgcalc.ui.screens.character.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.R
import io.silv.hsrdmgcalc.ui.UiCharacter
import io.silv.hsrdmgcalc.ui.composables.CharacterIcon
import io.silv.hsrdmgcalc.ui.composables.PathIcon
import io.silv.hsrdmgcalc.ui.composables.TypeIcon
import io.silv.hsrdmgcalc.ui.conditional

@Composable
fun CharacterListItem(
    modifier: Modifier,
    character: UiCharacter,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(Modifier.clip(RoundedCornerShape(20))) {
            BackGroundGradient(
                fiveStar = character.is5star,
                modifier = Modifier.matchParentSize()
            )
            CharacterIcon(
                character = character.name,
                contentDescription = character.name,
                contentScale = ContentScale.FillHeight
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxHeight()
                .padding(6.dp)
                .fillMaxWidth(0.3f)
        ) {
            Text(
                text = formatText(character.name),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Image(
                painter = painterResource(
                    id = if (character.is5star) R.drawable.honkai_5_star
                    else R.drawable.honkai_4_star
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.Start)
                    .conditional(!character.is5star) {
                        offset(x = -(10).dp)
                    }
            )
        }
        TypeIcon(
            type = character.type.name,
            contentDescription = character.type.name,
            modifier = Modifier
                .sizeIn(32.dp, 32.dp, 40.dp, 40.dp)
                .padding(end = 12.dp)
        )
        PathIcon(
            path = character.path,
            contentDescription = character.path.name,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = rememberLevelAnnotatedString(character = character),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun rememberLevelAnnotatedString(character: UiCharacter): AnnotatedString {
    val s1 = MaterialTheme.typography.titleMedium.toSpanStyle()
    return remember(character.level, character, s1) {
        val lvl = "Lvl. ${character.level}"
        val cap = "/${
            when(character.ascension) {
                0 -> 20
                1 -> 40
                2 -> 50
                3 -> 60
                4 -> 70
                5 -> 80
                6 -> 90
                else -> ""
            }
        }"
        buildAnnotatedString {
            if (!character.owned) {
                append("unowned")
                return@buildAnnotatedString
            }
            append(lvl)
            addStyle(style = s1, 0, lvl.length)
            append(cap)
            addStyle(
                style = s1,
                lvl.length,
                lvl.length + cap.length
            )
        }
    }
}

@Composable
private fun formatText(text: String) = remember(text) {
    buildString {
        for((i, c) in text.withIndex()) {
            if (i != 0 && c.isUpperCase() || c.isDigit()) {
                append(' ')
            }
            append(c)
        }
    }
}