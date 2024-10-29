package com.mostafadevo.freegames.domain.repository

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface CheapSharkRepository {
    suspend fun getDeals(
        title: String?,
        storeId: String?,
        lowerPrice: Int?,
        upperPrice: Int?,
        onSale: Boolean?,
        sortBy: String?,
        desc: Boolean?
    ): Flow<ResultWrapper<List<DealsDTOItem>>>
}
