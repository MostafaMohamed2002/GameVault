package com.mostafadevo.freegames.data.remote.cheapshark

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {

    @GET("deals")
    suspend fun getDeals(
        @Query("title") title: String? = null,
        @Query("storeID") storeId: String? = null,
        @Query("lowerPrice") lowerPrice: Int? = null,
        @Query("upperPrice") upperPrice: Int? = null,
        @Query("onSale") onSale: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("desc") desc: Int? = null
    ): Response<List<DealsDTOItem>>
}
