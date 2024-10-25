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
        @Query("sortBy") sortBy:String="DealRating",
        @Query("desc") desc:Int =1,
        @Query("onSale") onSale:Int =1,
    ):Response<List<DealsDTOItem>>

    @GET("deals")
    suspend fun getDealById(
        @Query("id") id:String,
    ):Response<DealDetailsDTO>

}
