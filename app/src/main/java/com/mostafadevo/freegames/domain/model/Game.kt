package com.mostafadevo.freegames.domain.model

import android.support.annotation.Keep
import androidx.compose.runtime.Stable

@Keep
@Stable
data class Game(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val genre: String,
    val platform: String,
    val shortDescription: String
)
