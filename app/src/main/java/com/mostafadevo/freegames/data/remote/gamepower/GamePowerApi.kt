package com.mostafadevo.freegames.data.remote.gamepower

import com.mostafadevo.freegames.data.remote.gamepower.dto.GiveawayDtoItem
import retrofit2.http.GET
import retrofit2.http.Query

interface GamePowerApi {

    @GET("giveaways/")
    suspend fun getGiveaways(
        @Query("platform") platform: String? = null,
        @Query("sort-by") sortBy: String? = null,
        @Query("type") type: String? = null
    ): List<GiveawayDtoItem>
}
