package com.mostafadevo.freegames.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mostafadevo.freegames.utils.getAverageColor

@Composable
fun DealsListItem (
    modifier: Modifier = Modifier,// add animateItem modifier
    onClickListener: () -> Unit ,
    imageLink: String,
    string1: String,
    string2: String ,
    string3LineThrough: String ,
    string4: String ,
) {
    val averageColor = remember {
        mutableStateOf(Color.Gray)
    }
    averageColor.value = getAverageColor(imageLink)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(modifier)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        averageColor.value
                    )
                )
            )
            .clickable {
                onClickListener()
            },
        verticalAlignment = Alignment.CenterVertically,

        ) {
        AsyncImage(
            model = imageLink,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(8.dp)
                .size(150.dp, 100.dp)
                .clip(RoundedCornerShape(8.dp)),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = string1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(4.dp)
                    .basicMarquee()
            )
            //blurred boxes for price and savings percentage
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlurredBox(
                    Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text =string3LineThrough,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = TextDecoration.LineThrough,
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text =string2,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                BlurredBox(
                    Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = string4,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(4.dp)
                    )
                }
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
        string4 = "50% off",
    )
}
