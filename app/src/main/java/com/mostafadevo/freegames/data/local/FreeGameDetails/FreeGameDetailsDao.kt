package com.mostafadevo.freegames.data.local.FreeGameDetails

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FreeGameDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetails(gameDetails: FreeGameDetailsEntity)

    @Query("SELECT * FROM freegamedetailsentity WHERE id = :id")
    suspend fun getGameDetails(id: Int): FreeGameDetailsEntity?

    @Delete
    suspend fun deleteGameDetails(gameDetails: FreeGameDetailsEntity)

    @Query("DELETE FROM freegamedetailsentity")
    suspend fun deleteAll()

}
