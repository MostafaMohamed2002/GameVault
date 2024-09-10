package com.mostafadevo.freegames.domain.model

import androidx.compose.runtime.Stable
import com.mostafadevo.freegames.data.local.FreeGamesEntity
@Stable
data class Game(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val genre: String,
    val platform: String,
    val shortDescription: String
)
