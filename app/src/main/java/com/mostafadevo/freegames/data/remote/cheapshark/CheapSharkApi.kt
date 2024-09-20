package com.mostafadevo.freegames.data.remote.cheapshark

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealDetailsDTO
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface CheapSharkApi {
    @GET("deals")
    suspend fun getDeal(
        @Query("title") title:String?=null,
    ):Response<List<DealsDTOItem>>

    @GET("deals")
    suspend fun getDealById(
        @Query("id") id:String,
    ):Response<DealDetailsDTO>

}
