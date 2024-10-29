package com.mostafadevo.freegames.data.repository

import com.mostafadevo.freegames.data.remote.cheapshark.CheapSharkApi
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.domain.repository.CheapSharkRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CheapSharkRepositoryImpl @Inject constructor(
    private val cheapSharkApi: CheapSharkApi
) : CheapSharkRepository {

    override suspend fun getDeals(
        title: String?,
        storeId: String?,
        lowerPrice: Int?,
        upperPrice: Int?,
        onSale: Boolean?,
        sortBy: String?,
        desc: Boolean?

    ): Flow<ResultWrapper<List<DealsDTOItem>>> {
        return flow {
            emit(ResultWrapper.Loading())
            try {
                val response = cheapSharkApi.getDeals(
                    title = title,
                    storeId = storeId,
                    lowerPrice = lowerPrice,
                    upperPrice = upperPrice,
                    onSale = onSale,
                    sortBy = sortBy,
                    desc = desc
                )
                if (response.isSuccessful) {
                    emit(ResultWrapper.Success(response.body()!!))
                } else {
                    emit(ResultWrapper.Error(response.message()))
                }
                Timber.e("getDeals: ${response.body()}")
                Timber.e("getDeals: ${response.message()}")
                Timber.e("getDeals: ${response.code()}")
            } catch (e: Exception) {
                emit(ResultWrapper.Error(e.message))
                e.printStackTrace()
            }
        }
    }
}
