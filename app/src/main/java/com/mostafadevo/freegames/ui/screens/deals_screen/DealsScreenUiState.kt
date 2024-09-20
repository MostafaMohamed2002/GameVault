package com.mostafadevo.freegames.ui.screens.deals_screen

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem

data class DealsScreenUiState (
    val isLoading : Boolean = false,
    val deals : List<DealsDTOItem> ?= emptyList()
)
