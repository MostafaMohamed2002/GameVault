package com.mostafadevo.freegames.ui.screens.home_screen

import com.mostafadevo.freegames.domain.model.Game

data class FreeGamesScreenUiState (
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = true,
)