package com.mostafadevo.freegames.domain.repository

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealDetailsDTO
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface CheapSharkRepository {
    suspend fun getDeals(title: String?=null):Flow<ResultWrapper<List<DealsDTOItem>>>
    suspend fun getDealDetails(dealId: String):Flow<ResultWrapper<DealDetailsDTO>>
}
