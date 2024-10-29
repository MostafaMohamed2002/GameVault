package com.mostafadevo.freegames.data.remote.gamepower

import com.mostafadevo.freegames.data.remote.gamepower.dto.GiveawayDtoItem
import retrofit2.http.GET
import retrofit2.http.Query

interface GamePowerApi {

    @GET("giveaways/")
    suspend fun getGiveaways(
        @Query("platform") platform: String = "pc",
        @Query("sort-by") sortBy: String = "popularity",
        @Query("type") type: String
    ): List<GiveawayDtoItem>
}
