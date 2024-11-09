package com.mostafadevo.freegames.data.remote.cheapshark.dto

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class GameInfo(
    @Json(name = "gameID")
    val gameID: String,
    @Json(name = "metacriticLink")
    val metacriticLink: String?,
    @Json(name = "metacriticScore")
    val metacriticScore: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "releaseDate")
    val releaseDate: Int,
    @Json(name = "retailPrice")
    val retailPrice: String,
    @Json(name = "salePrice")
    val salePrice: String,
    @Json(name = "steamAppID")
    val steamAppID: String?,
    @Json(name = "steamRatingCount")
    val steamRatingCount: String,
    @Json(name = "steamRatingPercent")
    val steamRatingPercent: String,
    @Json(name = "steamRatingText")
    val steamRatingText: String?,
    @Json(name = "steamworks")
    val steamworks: String?,
    @Json(name = "storeID")
    val storeID: String,
    @Json(name = "thumb")
    val thumb: String
)
