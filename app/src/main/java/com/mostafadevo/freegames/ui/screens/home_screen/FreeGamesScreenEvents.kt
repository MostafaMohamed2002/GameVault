package com.mostafadevo.freegames.ui.screens.home_screen

sealed class FreeGamesScreenEvents {
    data class onSortBySelected(val sortBy: String?) : FreeGamesScreenEvents()
    data class onPlatformSelected(val platform: String?) : FreeGamesScreenEvents()
    data class onCategorySelected(val category: String?) : FreeGamesScreenEvents()
    object onSearchWithFilters : FreeGamesScreenEvents()
    data class GameClicked(val gameId: Int, val gameTitle: String, val platform: String) : FreeGamesScreenEvents()
}
