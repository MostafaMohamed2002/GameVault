package com.mostafadevo.freegames.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mostafadevo.freegames.data.local.FreeGameDetails.FreeGameDetailsDao
import com.mostafadevo.freegames.data.local.FreeGameDetails.FreeGameDetailsEntity
import com.mostafadevo.freegames.data.local.FreeGameDetails.MinimumSystemRequirementsConverter
import com.mostafadevo.freegames.data.local.FreeGameDetails.ScreenshotListConverter

@Database(
    entities = [FreeGamesEntity::class, FreeGameDetailsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MinimumSystemRequirementsConverter::class, ScreenshotListConverter::class)
abstract class FreeGamesDatabase : RoomDatabase() {
    abstract fun FreegamesDao(): FreeGamesDao
    abstract fun FreeGameDetailsDao(): FreeGameDetailsDao
}
