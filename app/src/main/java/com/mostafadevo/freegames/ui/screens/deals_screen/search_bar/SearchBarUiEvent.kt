package com.mostafadevo.freegames.ui.screens.deals_screen.search_bar
sealed class SearchBarUiEvent {
    data class onSearchBarActive(val isActive: Boolean) : SearchBarUiEvent()
    data class onSearchBarTextChange(val text: String) : SearchBarUiEvent()
    data object onSearch : SearchBarUiEvent()
    data object clearDeals : SearchBarUiEvent()
}
