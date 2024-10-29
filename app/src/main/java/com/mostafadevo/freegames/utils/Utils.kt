package com.mostafadevo.freegames.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

fun getContrastingTextColor(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() > 0.8) Color.Black else Color.White
}

fun getTimeRemaining(endDateString: String): String {
    // Define the date format that matches your input
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    val endDate = dateFormat.parse(endDateString) ?: return "Invalid date"
    val currentTime = Date()

    // Calculate the difference in milliseconds
    val diffInMillis = endDate.time - currentTime.time

    if (diffInMillis <= 0) {
        return "Ended"
    }

    // Convert the difference to days, hours, and minutes
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

    // Build the time remaining string
    val remainingTime = StringBuilder("Ends in ")

    if (days > 0) remainingTime.append("$days days ")
    if (hours > 0) remainingTime.append("$hours hours ")
    if (minutes > 0) remainingTime.append("$minutes minutes")

    return remainingTime.toString().trim()
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        context.startActivity(this)
    }
}
