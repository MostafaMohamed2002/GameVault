package com.mostafadevo.freegames.data.remote.dto


import com.squareup.moshi.Json

data class GameDetailsDTO(
    @Json(name = "description")
    val description: String,
    @Json(name = "developer")
    val developer: String,
    @Json(name = "freetogame_profile_url")
    val freetogameProfileUrl: String,
    @Json(name = "game_url")
    val gameUrl: String,
    @Json(name = "genre")
    val genre: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "minimum_system_requirements")
    val minimumSystemRequirements: MinimumSystemRequirements?,
    @Json(name = "platform")
    val platform: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "screenshots")
    val screenshots: List<Screenshot>,
    @Json(name = "short_description")
    val shortDescription: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "title")
    val title: String
)
data class Screenshot(
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String
)
data class MinimumSystemRequirements(
    @Json(name = "graphics")
    val graphics: String?,
    @Json(name = "memory")
    val memory: String?,
    @Json(name = "os")
    val os: String?,
    @Json(name = "processor")
    val processor: String?,
    @Json(name = "storage")
    val storage: String?
)
