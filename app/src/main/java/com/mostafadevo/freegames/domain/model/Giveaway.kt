package com.mostafadevo.freegames.domain.model

import android.support.annotation.Keep

@Keep
data class Giveaway(
    val description: String,
    val end_date: String,
    val gamerpower_url: String,
    val id: Int,
    val image: String,
    val instructions: String,
    val open_giveaway: String,
    val open_giveaway_url: String,
    val platforms: String,
    val published_date: String,
    val status: String,
    val thumbnail: String,
    val title: String,
    val type: String,
    val users: Int,
    val worth: String
)
