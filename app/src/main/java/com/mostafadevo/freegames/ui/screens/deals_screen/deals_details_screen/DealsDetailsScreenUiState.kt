package com.mostafadevo.freegames.ui.screens.deals_screen.deals_details_screen

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealDetailsDTO
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem

data class DealsDetailsScreenUiState(
    val isLoading: Boolean = false,
    val deal: DealDetailsDTO? = null
)
