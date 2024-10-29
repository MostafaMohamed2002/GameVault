package com.mostafadevo.freegames.ui.screens.deals_screen

sealed class DealsAndGiveawayScreenUiEffect {
    data class ShowSnackBar(val message: String) : DealsAndGiveawayScreenUiEffect()
}
