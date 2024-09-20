package com.mostafadevo.freegames.ui.screens.deals_screen

sealed class DealsScreenUiEffect {
    data class ShowSnackBar(val message: String) : DealsScreenUiEffect()
}
