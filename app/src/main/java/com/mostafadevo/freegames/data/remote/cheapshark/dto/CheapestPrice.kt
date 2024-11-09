package com.mostafadevo.freegames.data.remote.cheapshark.dto

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class CheapestPrice(
    @Json(name = "date")
    val date: Int?,
    @Json(name = "price")
    val price: String?
)
