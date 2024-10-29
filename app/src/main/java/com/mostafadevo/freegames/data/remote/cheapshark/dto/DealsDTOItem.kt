package com.mostafadevo.freegames.data.remote.cheapshark.dto

import com.squareup.moshi.Json

data class DealsDTOItem(
    @Json(name = "dealID")
    val dealID: String,
    @Json(name = "dealRating")
    val dealRating: String,
    @Json(name = "gameID")
    val gameID: String,
    @Json(name = "internalName")
    val internalName: String,
    @Json(name = "isOnSale")
    val isOnSale: String,
    @Json(name = "lastChange")
    val lastChange: Int,
    @Json(name = "metacriticLink")
    val metacriticLink: String?,
    @Json(name = "metacriticScore")
    val metacriticScore: String,
    @Json(name = "normalPrice")
    val normalPrice: String,
    @Json(name = "releaseDate")
    val releaseDate: Int,
    @Json(name = "salePrice")
    val salePrice: String,
    @Json(name = "savings")
    val savings: String,
    @Json(name = "steamAppID")
    val steamAppID: String?,
    @Json(name = "steamRatingCount")
    val steamRatingCount: String,
    @Json(name = "steamRatingPercent")
    val steamRatingPercent: String,
    @Json(name = "steamRatingText")
    val steamRatingText: String?,
    @Json(name = "storeID")
    val storeID: String,
    @Json(name = "thumb")
    val thumb: String,
    @Json(name = "title")
    val title: String
)
