package com.mostafadevo.freegames.data.remote.cheapshark.dto

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class DealDetailsDTO(
    @Json(name = "cheaperStores")
    val cheaperStores: List<CheaperStore>?,
    @Json(name = "cheapestPrice")
    val cheapestPrice: CheapestPrice?,
    @Json(name = "gameInfo")
    val gameInfo: GameInfo
)
