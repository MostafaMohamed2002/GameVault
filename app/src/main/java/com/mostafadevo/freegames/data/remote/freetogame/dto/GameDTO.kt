package com.mostafadevo.freegames.data.remote.freetogame.dto


import com.mostafadevo.freegames.data.local.FreeGamesEntity
import com.mostafadevo.freegames.domain.model.Game
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameDTO(
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
    @Json(name = "platform")
    val platform: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "short_description")
    val shortDescription: String,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "title")
    val title: String
){


}
