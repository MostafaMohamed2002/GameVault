package com.mostafadevo.freegames.domain.repository

import com.mostafadevo.freegames.domain.model.Giveaway
import com.mostafadevo.freegames.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface GamePowerRepository {
    suspend fun getGiveaways(): Flow<ResultWrapper<List<Giveaway>>>
}
