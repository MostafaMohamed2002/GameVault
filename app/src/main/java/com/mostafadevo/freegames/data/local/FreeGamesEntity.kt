package com.mostafadevo.freegames.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "games"
)
data class FreeGamesEntity(
    @ColumnInfo(name = "genre")
    val genre: String,
    @ColumnInfo(name = "id") @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "platform")
    val platform: String,
    @ColumnInfo(name = "short_description")
    val shortDescription: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "title")
    val title: String
) {
}
