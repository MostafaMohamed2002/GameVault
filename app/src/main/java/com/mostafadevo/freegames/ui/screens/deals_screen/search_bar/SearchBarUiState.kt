package com.mostafadevo.freegames.ui.screens.deals_screen.search_bar

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem

data class SearchBarUiState (
    val isLoading : Boolean = false,
    val isSearchBarActive : Boolean= false,
    val SearchBarText : String = "",
    val searchHistory : List<String> ?= emptyList(),
    val deals : List<DealsDTOItem> ?= emptyList()
)
