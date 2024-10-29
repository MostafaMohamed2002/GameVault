package com.mostafadevo.freegames.data.remote.cheapshark.dto

import com.squareup.moshi.Json

data class CheapestPrice(
    @Json(name = "date")
    val date: Int?,
    @Json(name = "price")
    val price: String?
)
