package com.mostafadevo.freegames.data.repository

import coil.network.HttpException
import com.mostafadevo.freegames.data.local.FreeGameDetails.FreeGameDetailsDao
import com.mostafadevo.freegames.data.local.FreeGamesDao
import com.mostafadevo.freegames.data.remote.freetogame.FreeGamesApi
import com.mostafadevo.freegames.domain.mappers.toDomain
import com.mostafadevo.freegames.domain.mappers.toEntity
import com.mostafadevo.freegames.domain.model.Game
import com.mostafadevo.freegames.domain.model.GameDetails
import com.mostafadevo.freegames.domain.repository.FreeGamesRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FreeGamesRepositoryImpl @Inject constructor(
    private val api: FreeGamesApi,
    private val db: FreeGamesDao,
    private val gameDetailsDao: FreeGameDetailsDao
) : FreeGamesRepository {
    override suspend fun getGames(): Flow<ResultWrapper<List<Game>>> {
        return flow {
            emit(ResultWrapper.Loading())
            val cachedGames = db.getGames().first()
            if (cachedGames.isNotEmpty()) {
                emit(ResultWrapper.Success(cachedGames.map { it.toDomain() }))
                return@flow
            } else {
                try {
                    val newtworkGames = api.getGames()
                    if (newtworkGames.isSuccessful) {
                        val games = newtworkGames.body()!!.map { it.toEntity() }
                        db.upsertAll(games)
                        emit(ResultWrapper.Success(games.map { it.toDomain() }))
                    }
                } catch (e: HttpException) {
                    emit(ResultWrapper.Error(message = e.message))
                } catch (e: Exception) {
                    emit(ResultWrapper.Error(message = e.message))
                }
            }
        }
    }

    override suspend fun getGameById(gameId: Int): Flow<ResultWrapper<GameDetails>> {
        return flow {
            emit(ResultWrapper.Loading())
            val cachedGame = gameDetailsDao.getGameDetails(gameId)
            if (cachedGame != null) {
                emit(ResultWrapper.Success(cachedGame.toDomain()))
                return@flow
            }
            try {
                val gameDetails = api.getGameById(gameId)
                if (gameDetails.isSuccessful) {
                    val gameDetailsDTO = gameDetails.body()!!
                    gameDetailsDao.insertGameDetails(gameDetailsDTO.toEntity())
                    emit(ResultWrapper.Success(gameDetailsDTO.toDomain()))
                }
            } catch (e: HttpException) {
                emit(ResultWrapper.Error(message = e.message))
            } catch (e: Exception) {
                emit(ResultWrapper.Error(message = e.message))
            }
        }
    }

    override suspend fun refreshGames(): Flow<ResultWrapper<List<Game>>> = flow {
        emit(ResultWrapper.Loading()) // Emit the loading state

        try {
            // Fetch from the network
            val networkGames = api.getGames()
            if (networkGames.isSuccessful) {
                // Map the network games to domain models
                val games = networkGames.body()!!.map { it.toEntity() }
                // Clear old cache in Room and insert the latest data
                db.deleteAll()
                db.upsertAll(games)

                // Emit the refreshed games
                emit(ResultWrapper.Success(games.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            // Handle the error and emit an error state
            emit(ResultWrapper.Error("Failed to refresh games $e"))
        }
    }

    override suspend fun getGamesByFilters(
        platform: String?,
        category: String?,
        sortBy: String?
    ): Flow<ResultWrapper<List<Game>>> {
        return flow {
            emit(ResultWrapper.Loading())
            try {
                val games = api.getGamesByFilters(platform, category, sortBy)
                if (games.isSuccessful) {
                    emit(
                        ResultWrapper.Success(
                            games.body()!!.map { it.toEntity() }.map { it.toDomain() }
                        )
                    )
                }
            } catch (e: HttpException) {
                emit(ResultWrapper.Error(message = e.message))
            } catch (e: Exception) {
                emit(ResultWrapper.Error(message = e.message))
            }
        }
    }
}
