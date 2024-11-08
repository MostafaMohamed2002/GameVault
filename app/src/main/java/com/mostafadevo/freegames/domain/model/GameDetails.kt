package com.mostafadevo.freegames.domain.model

data class GameDetails(
    val id: Int,
    val title: String,
    val description: String,
    val developer: String,
    val freetogameProfileUrl: String,
    val genre: String,
    val minimumSystemRequirements: MinimumSystemRequirements,
    val platform: String,
    val publisher: String,
    val releaseDate: String,
    val screenshots: List<Screenshot>,
    val shortDescription: String,
    val thumbnail: String
)
data class Screenshot(
    val image: String
)
data class MinimumSystemRequirements(
    val graphics: String? = null,
    val memory: String? = null,
    val os: String? = null,
    val processor: String? = null,
    val storage: String? = null
)
