package com.mostafadevo.freegames.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

suspend fun extractDominantColor(drawable: Drawable): Color {
    val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
    return withContext(Dispatchers.Default) {
        val palette = Palette.from(bitmap).generate()
        val dominantColor = palette.getDarkVibrantColor(Color.Gray.toArgb()) // Default to gray if no color is found
        Color(dominantColor)
    }
}
suspend fun extractDominantColor2(drawable: Drawable): Palette.Swatch? {
    val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
    return withContext(Dispatchers.Default) {
        val palette = Palette.from(bitmap).generate()
        palette.lightVibrantSwatch // Return the light vibrant swatch
    }
}

suspend fun loadImage(context: Context, imageUrl: String): Drawable? {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .build()

    val result = imageLoader.execute(request)
    return if (result is SuccessResult) {
        result.drawable
    } else {
        null
    }
}

fun Int.toFormattedDate(): String {
    val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochSecond(this.toLong())
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}
