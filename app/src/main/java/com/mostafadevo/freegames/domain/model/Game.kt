package com.mostafadevo.freegames.domain.model

import androidx.compose.runtime.Stable
@Stable
data class Game(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val genre: String,
    val platform: String,
    val shortDescription: String
)
