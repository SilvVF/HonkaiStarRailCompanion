package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.silv.hsrdmgcalc.R
import io.silv.hsrdmgcalc.data.HonkaiConstants
import io.silv.hsrdmgcalc.ui.theme.HsrDmgCalcTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemiCompactCharacterCard(
    modifier: Modifier,
    character: String,
    type: Type,
    path: Path,
    fiveStar: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(10)
    ) {
        Box(Modifier.fillMaxSize()) {
            BackGroundGradient(
                fiveStar = fiveStar,
                modifier = Modifier.matchParentSize()
            )
            CharacterIcon(
                character = character,
                contentDescription = character,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.TopStart)
            ) {
                PathIcon(
                    path = path,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(24.dp)
                )
                TypeIcon(
                    type = type.name,
                    contentDescription = type.name,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(
                        id = if (fiveStar) R.drawable.honkai_5_star
                        else R.drawable.honkai_4_star
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 6.dp),
                    contentScale = ContentScale.FillWidth
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray.copy(alpha = 0.7f))
                    .padding(8.dp)
                ) {
                    Text(
                        text = formatText(character),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge.copy(
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        ),
                        maxLines = 2,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactCharacterCard(
    modifier: Modifier,
    character: String,
    type: Type,
    fiveStar: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20)
    ) {
        Box(Modifier.fillMaxSize()) {
            BackGroundGradient(
                fiveStar = fiveStar,
                modifier = Modifier.matchParentSize()
            )
            CharacterIcon(
                character = character,
                contentDescription = character,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
            TypeIcon(
                type = type.name,
                contentDescription = type.name,
                modifier = Modifier
                    .padding(4.dp)
                    .size(26.dp)
                    .align(Alignment.TopStart)
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray.copy(alpha = 0.75f))
                .padding(8.dp)
                .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = formatText(character),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge.copy(
                        textAlign = TextAlign.Center,
                        color = Color.LightGray
                    ),
                    maxLines = 2,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtraCompactCharacterCard(
    modifier: Modifier,
    character: String,
    type: Type,
    fiveStar: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20)
    ) {
        Box(Modifier.fillMaxSize()) {
            BackGroundGradient(
                fiveStar = fiveStar,
                modifier = Modifier.matchParentSize()
            )
            CharacterIcon(
                character = character,
                contentDescription = character,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
            TypeIcon(
                type = type.name,
                contentDescription = type.name,
                modifier = Modifier
                    .padding(4.dp)
                    .size(26.dp)
                    .align(Alignment.BottomEnd)
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

@Composable
fun BackGroundGradient(
    fiveStar: Boolean,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(
            if (fiveStar) {
                Brush.verticalGradient(
                    listOf(Color(0xff695453), Color(0xffe6ac54))
                )
            } else {
                Brush.verticalGradient(
                    listOf(Color(0xff424369), Color(0xff855DAF))
                )
            }
        )
    )
}

@Preview
@Composable
private fun CharacterCardPreview() {
    HsrDmgCalcTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(HonkaiConstants.characters) { i, character ->
                CompactCharacterCard(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(),
                    character = character,
                    type = Type.values().random(),
                    fiveStar = i < 17
                ){}
            }
        }
    }
}