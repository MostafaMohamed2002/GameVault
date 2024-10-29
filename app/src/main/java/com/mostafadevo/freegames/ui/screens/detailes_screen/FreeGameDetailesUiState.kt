package com.mostafadevo.freegames.ui.screens.detailes_screen

import com.mostafadevo.freegames.domain.model.GameDetails
import com.mostafadevo.freegames.domain.model.MinimumSystemRequirements

data class FreeGameDetailesUiState(
    val isLoading: Boolean = false,
    var isDeveloperEqualPublisher: Boolean = false,
    val game: GameDetails = GameDetails(
        id = 0,
        title = "",
        thumbnail = "",
        shortDescription = "",
        genre = "",
        platform = "",
        publisher = "",
        developer = "",
        releaseDate = "",
        freetogameProfileUrl = "",
        minimumSystemRequirements = MinimumSystemRequirements(
            graphics = "",
            memory = "",
            os = "",
            processor = "",
            storage = ""
        ),
        screenshots = emptyList(),
        description = ""
    )
)
