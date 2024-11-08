package com.mostafadevo.freegames.data.local.FreeGameDetails

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mostafadevo.freegames.data.remote.freetogame.dto.MinimumSystemRequirements
import com.mostafadevo.freegames.data.remote.freetogame.dto.Screenshot

@Entity
data class FreeGameDetailsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val developer: String,
    val freetogameProfileUrl: String,
    val genre: String,
    @TypeConverters(MinimumSystemRequirementsConverter::class)
    val minimumSystemRequirements: MinimumSystemRequirements,
    val platform: String,
    val publisher: String,
    val releaseDate: String,
    @TypeConverters(Screenshot::class)
    val screenshots: List<Screenshot>,
    val shortDescription: String,
    val thumbnail: String
)
