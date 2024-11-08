package com.mostafadevo.freegames.ui.screens.deals_screen

import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.domain.model.Giveaway

data class DealsAndGiveawayScreenUiState(
    var selectedTab: Int = 0, // 0 for deals , 1 for giveaways
    val isBottomSheetVisible: Boolean = false,
    val isDealsLoading: Boolean = false,
    val isGiveawaysLoading: Boolean = false,
    val deals: List<DealsDTOItem> ? = emptyList(),
    val giveaways: List<Giveaway> ? = emptyList(),
    val isDealsSearchBarActive: Boolean = false,
    val isDealsSearchBarLoading: Boolean = false,
    val dealsSearchBarText: String = "",
    val dealsSearchHistory: List<String> ? = emptyList(),
    val dealsSearchBardata: List<DealsDTOItem> ? = emptyList(),
    val filterStoreId: String ? = null,
    val filterLowerPrice: Float ? = null,
    val filterUpperPrice: Float ? = null,
    val filterOnSale: Boolean ? = false,
    var filterSortBy: String ? = null,
    val filterDesc: Boolean ? = false,
    val searchHistoryLimit: Int = 5,
    val isGiveawaysBottomSheetVisible: Boolean = false,
    val giveawaysFilterSortBy: String ? = null,
    val giveawaysFilterType: String ? = null,
    val giveawaysFilterPlatform: String ? = null
)
