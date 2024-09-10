package com.mostafadevo.freegames.ui.screens.home_screen

sealed class FreeGamesScreenUiEffect {
    data class ShowSnackbar(val message: String) : FreeGamesScreenUiEffect()
}