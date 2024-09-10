package com.mostafadevo.freegames.ui.screens.detailes_screen

sealed class FreeGameDetailesUiEffect {
    data class ShowSnackBar(val message: String) : FreeGameDetailesUiEffect()
}
