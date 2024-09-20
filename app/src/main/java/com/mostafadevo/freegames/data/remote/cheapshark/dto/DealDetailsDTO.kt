package com.mostafadevo.freegames.data.remote.cheapshark.dto


import com.squareup.moshi.Json

data class DealDetailsDTO(
    @Json(name = "cheaperStores")
    val cheaperStores: List<CheaperStore>?,
    @Json(name = "cheapestPrice")
    val cheapestPrice: CheapestPrice?,
    @Json(name = "gameInfo")
    val gameInfo: GameInfo
)
