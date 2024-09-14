package com.mostafadevo.freegames.domain.repository

import com.mostafadevo.freegames.domain.model.Game
import com.mostafadevo.freegames.domain.model.GameDetails
import com.mostafadevo.freegames.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface  FreeGamesRepository {
    abstract suspend fun getGames() : Flow<ResultWrapper<List<Game>>>
    abstract suspend fun getGameById(gameId: Int): Flow<ResultWrapper<GameDetails>>
    abstract suspend fun refreshGames(): Flow<ResultWrapper<List<Game>>>
    abstract suspend fun getGamesByFilters(
        platform: String?,
        category: String?,
        sortBy: String?
    ): Flow<ResultWrapper<List<Game>>>
}
