package com.mostafadevo.freegames.data.remote.cheapshark.dto


import com.squareup.moshi.Json

data class CheaperStore(
    @Json(name = "dealID")
    val dealID: String?,
    @Json(name = "retailPrice")
    val retailPrice: String?,
    @Json(name = "salePrice")
    val salePrice: String?,
    @Json(name = "storeID")
    val storeID: String?
)
