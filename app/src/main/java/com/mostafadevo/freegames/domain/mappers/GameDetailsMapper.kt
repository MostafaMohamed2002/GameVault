package com.mostafadevo.freegames.domain.mappers

import com.mostafadevo.freegames.data.local.FreeGameDetails.FreeGameDetailsEntity
import com.mostafadevo.freegames.data.remote.dto.GameDetailsDTO
import com.mostafadevo.freegames.data.remote.dto.MinimumSystemRequirements
import com.mostafadevo.freegames.data.remote.dto.Screenshot
import com.mostafadevo.freegames.domain.model.Game
import com.mostafadevo.freegames.domain.model.GameDetails

fun GameDetailsDTO.toDomain() = GameDetails(
    id = id,
    title = title,
    description = description,
    developer = developer,
    freetogameProfileUrl = freetogameProfileUrl,
    genre = genre,
    minimumSystemRequirements = minimumSystemRequirements.let {
        com.mostafadevo.freegames.domain.model.MinimumSystemRequirements(
            graphics = it?.graphics,
            memory = it?.memory,
            os = it?.os,
            processor = it?.processor,
            storage = it?.storage
        )
    },
    platform = platform,
    publisher = publisher,
    releaseDate = releaseDate,
    screenshots = screenshots.map { it.toDomain() },
    shortDescription = shortDescription,
    thumbnail = thumbnail
)

fun MinimumSystemRequirements.toDomain():com.mostafadevo.freegames.domain.model.MinimumSystemRequirements{
    return com.mostafadevo.freegames.domain.model.MinimumSystemRequirements(
        graphics = graphics,
        memory = memory,
        os = os,
        processor = processor,
        storage = storage
    )
}

fun Screenshot.toDomain():com.mostafadevo.freegames.domain.model.Screenshot{
    return com.mostafadevo.freegames.domain.model.Screenshot(
        image = image
    )
}
fun GameDetailsDTO.toEntity() = com.mostafadevo.freegames.data.local.FreeGameDetails.FreeGameDetailsEntity(
    id = id,
    title = title,
    description = description,
    developer = developer,
    freetogameProfileUrl = freetogameProfileUrl,
    genre = genre,
    minimumSystemRequirements = minimumSystemRequirements.let {
        com.mostafadevo.freegames.data.remote.dto.MinimumSystemRequirements(
            graphics = it?.graphics,
            memory = it?.memory,
            os = it?.os,
            processor = it?.processor,
            storage = it?.storage
        )
    },
    platform = platform,
    publisher = publisher,
    releaseDate = releaseDate,
    screenshots = screenshots,
    shortDescription = shortDescription,
    thumbnail = thumbnail
)
fun FreeGameDetailsEntity.toDomain() = GameDetails(
    id = id,
    title = title,
    description = description,
    developer = developer,
    freetogameProfileUrl = freetogameProfileUrl,
    genre = genre,
    minimumSystemRequirements = minimumSystemRequirements.toDomain(),
    platform = platform,
    publisher = publisher,
    releaseDate = releaseDate,
    screenshots = screenshots.map { it.toDomain() },
    shortDescription = shortDescription,
    thumbnail = thumbnail
)
