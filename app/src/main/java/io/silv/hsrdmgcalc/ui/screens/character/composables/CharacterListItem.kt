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