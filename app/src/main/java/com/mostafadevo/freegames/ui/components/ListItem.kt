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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mostafadevo.freegames.utils.getAverageColor
import com.mostafadevo.freegames.utils.getContrastingTextColor

@Composable
fun DealsListItem(
    modifier: Modifier = Modifier, // add animateItem modifier
    onClickListener: () -> Unit,
    imageLink: String,
    string1: String, // title
    string2: String, // price now
    string3LineThrough: String, // price before sale
    string4: String // sale percentage discount
) {
    var dominantColor by remember {
        mutableStateOf(Color.Transparent)
    }
    var onImageLoading by remember { mutableStateOf(true) }
    dominantColor = getAverageColor(imageLink)
    val animatedTextColor by animateColorAsState(
        targetValue = getContrastingTextColor(dominantColor)
    )
    Column(
        modifier = Modifier
            .testTag("DealsListItem")
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
                onClickListener()
            },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = imageLink,
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
            text = string1,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 16.sp,
            maxLines = 1,
            color = animatedTextColor,
            modifier = Modifier
                .padding(8.dp)
                .basicMarquee()
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
                Row {
                    Text(
                        text = string3LineThrough,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = animatedTextColor,
                        maxLines = 1,
                        textDecoration = TextDecoration.LineThrough,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Text(
                        text = string2,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = animatedTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            BlurredBox(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = string4,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = animatedTextColor,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun ListItemPreview() {
    DealsListItem(
        onClickListener = {},
        imageLink = "https://www.example.com/image.jpg",
        string1 = "Title",
        string2 = "$10",
        string3LineThrough = "$20",
        string4 = "50% off"
    )
}
