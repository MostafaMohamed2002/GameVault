package com.mostafadevo.freegames.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FreeGamesDao {
    @Upsert
    suspend fun upsertAll(games: List<FreeGamesEntity>)

    @Delete
    suspend fun deleteGame(game: FreeGamesEntity)

    @Query("SELECT * FROM games")
    fun getGames(): Flow<List<FreeGamesEntity>>

    @Query("DELETE FROM games")
    suspend fun deleteAll()
}
