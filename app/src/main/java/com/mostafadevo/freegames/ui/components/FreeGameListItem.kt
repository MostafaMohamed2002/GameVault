package com.mostafadevo.freegames.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mostafadevo.freegames.domain.model.Game
import com.mostafadevo.freegames.utils.getAverageColor
import com.mostafadevo.freegames.utils.getContrastingTextColor

@Composable
fun FreeGameListItem(
    modifier: Modifier = Modifier,
    game: Game,
    onClickListener: (gameId: Int) -> Unit
) {
    var dominantColor by remember {
        mutableStateOf(Color.Transparent)
    }
    var onImageLoading by remember { mutableStateOf(true) }
    dominantColor = getAverageColor(game.thumbnail)
    val animatedTextColor by animateColorAsState(
        targetValue = getContrastingTextColor(dominantColor)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        dominantColor
                    ),
                    tileMode = TileMode.Clamp
                )
            )
            .then(
                modifier
            )
            .clickable {
                onClickListener(game.id)
            },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = game.thumbnail,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                onSuccess = {
                    onImageLoading = false
                },
                onError = {
                    onImageLoading = false
                }
            )
            if (onImageLoading) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            }
        }

        Text(
            text = game.title,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 16.sp,
            maxLines = 1,
            color = animatedTextColor,
            modifier = Modifier
                .padding(8.dp)
                .basicMarquee()
        )
        Text(
            text = game.shortDescription,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 14.sp,
            color = animatedTextColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BlurredBox(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = game.platform,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                        .basicMarquee()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            BlurredBox(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = game.genre,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun FreeGameListItemPreview() {
    FreeGameListItem(
        game = Game(
            id = 1,
            title = "Game Title",
            thumbnail = "https://www.example.com/image.jpg",
            genre = "Action",
            platform = "PC",
            shortDescription = "Short Description"
        ),
        onClickListener = {}
    )
}
