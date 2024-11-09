package com.mostafadevo.freegames.data.remote.cheapshark.dto

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
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
