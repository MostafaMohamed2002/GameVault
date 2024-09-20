package com.mostafadevo.freegames.data.repository

import com.mostafadevo.freegames.data.remote.cheapshark.CheapSharkApi
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealDetailsDTO
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.domain.repository.CheapSharkRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheapSharkRepositoryImpl @Inject constructor(
    private val cheapSharkApi: CheapSharkApi
) : CheapSharkRepository {
    override suspend fun getDeals(title: String?): Flow<ResultWrapper<List<DealsDTOItem>>> {
        return flow {
            emit(ResultWrapper.Loading())
            try {
                val response = cheapSharkApi.getDeal(title)
                if (response.isSuccessful)
                    emit(ResultWrapper.Success(response.body()!!))
                else
                    emit(ResultWrapper.Error(response.message()))
            } catch (e: Exception) {
                emit(ResultWrapper.Error(e.message))
            }
        }
    }

    override suspend fun getDealDetails(dealId: String): Flow<ResultWrapper<DealDetailsDTO>> {
        return flow {
            emit(ResultWrapper.Loading())
            try {
                val response = cheapSharkApi.getDealById(dealId)
                if (response.isSuccessful)
                    emit(ResultWrapper.Success(data = response.body()!!))
                else
                    emit(ResultWrapper.Error(response.message()))
            } catch (e: Exception) {
                emit(ResultWrapper.Error(e.message))
            }
        }
    }
}
