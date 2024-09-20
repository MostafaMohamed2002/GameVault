package com.mostafadevo.freegames.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp


@Composable
fun BlurredBox(modifier: Modifier = Modifier, content : @Composable () -> Unit) {
    Box(
        modifier = modifier
        ,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize() // Fill the entire box
                .background(Color.LightGray.copy(alpha = 0.35f)) // Background color with some transparency
                .blur(25.dp) // Apply blur to the background
        )
        content()
    }
}

@Preview(apiLevel = 34, showSystemUi = true, showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun BlurredBoxPreview() {
    MaterialTheme {
        BlurredBox(
        ) {
            Text("Hello, World!")
        }
    }
}
