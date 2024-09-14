package com.mostafadevo.freegames.data.remote

import com.mostafadevo.freegames.data.remote.dto.GameDTO
import com.mostafadevo.freegames.data.remote.dto.GameDetailsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface FreeGamesApi {
    @GET("games/")
    suspend fun getGames(): Response<List<GameDTO>>

    @GET("game/")
    suspend fun getGameById(@Query("id") id: Int): Response<GameDetailsDTO>

    @GET("games/")
    suspend fun getGamesByFilters(
        @Query("platform") platform: String?=null,
        @Query("category") category: String?=null,
        @Query("sort-by") sortBy: String?=null
    ): Response<List<GameDTO>>
}
