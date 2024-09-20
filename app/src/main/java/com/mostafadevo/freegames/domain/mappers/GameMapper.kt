package com.mostafadevo.freegames.domain.mappers

import com.mostafadevo.freegames.data.local.FreeGamesEntity
import com.mostafadevo.freegames.data.remote.freetogame.dto.GameDTO
import com.mostafadevo.freegames.domain.model.Game

fun GameDTO.toEntity(): FreeGamesEntity {
    return FreeGamesEntity(
        genre = genre,
        id = id,
        platform = platform,
        shortDescription = shortDescription,
        thumbnail = thumbnail,
        title = title
    )
}
fun GameDTO.toDomain(): Game {
    return Game(
        genre = genre,
        id = id,
        platform = platform,
        shortDescription = shortDescription,
        thumbnail = thumbnail,
        title = title
    )
}

fun FreeGamesEntity.toDomain(): Game{
    return Game(
        genre = genre,
        id = id,
        platform = platform,
        shortDescription = shortDescription,
        thumbnail = thumbnail,
        title = title
    )
}
fun Game.toEntity(): FreeGamesEntity {
    return FreeGamesEntity(
        genre = genre,
        id = id,
        platform = platform,
        shortDescription = shortDescription,
        thumbnail = thumbnail,
        title = title
    )
}
