package com.mostafadevo.freegames.data.repository

import com.mostafadevo.freegames.data.remote.gamepower.GamePowerApi
import com.mostafadevo.freegames.domain.mappers.toGiveaway
import com.mostafadevo.freegames.domain.model.Giveaway
import com.mostafadevo.freegames.domain.repository.GamePowerRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GamePowerRepositoryImpl @Inject constructor(
    private val gamePowerApi: GamePowerApi
) : GamePowerRepository {
    override suspend fun getGiveaways(
        platform: String?,
        sortBy: String?,
        type: String?
    ): Flow<ResultWrapper<List<Giveaway>>> {
        return flow {
            emit(ResultWrapper.Loading())
            try {
                val giveaways = gamePowerApi.getGiveaways(
                    platform = platform,
                    sortBy = sortBy,
                    type = type
                )
                emit(ResultWrapper.Success(giveaways.map { it.toGiveaway() }))
            } catch (e: Exception) {
                emit(ResultWrapper.Error(message = e.message))
            }
        }
    }
}
