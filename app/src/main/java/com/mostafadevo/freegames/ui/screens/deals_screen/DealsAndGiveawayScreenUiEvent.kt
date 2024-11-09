package com.mostafadevo.freegames.ui.screens.deals_screen

sealed class DealsAndGiveawayScreenUiEvent {
    data class onTabSelected(val tabIndex: Int) : DealsAndGiveawayScreenUiEvent()
    data object OnLoadDeals : DealsAndGiveawayScreenUiEvent()
    data object OnLoadGiveaways : DealsAndGiveawayScreenUiEvent()

    data class OnStoreFilterChanged(
        val storeId: String
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnLowerPriceFilterChanged(
        val lowerPrice: Int
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnUpperPriceFilterChanged(
        val upperPrice: Int
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnOnSaleFilterChanged(
        val onSale: Boolean
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnSortByFilterChanged(
        val sortBy: String
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnDescFilterChanged(
        val desc: Boolean
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnToggleSearchBar(
        val isSearchBarVisible: Boolean
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnSearchBarTextChanged(
        val text: String
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnSearchBarTextSubmit(
        val text: String
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnToggleBottomSheet(
        val isVisible: Boolean
    ) : DealsAndGiveawayScreenUiEvent()

    data object OnClearSearchBarDeals : DealsAndGiveawayScreenUiEvent()
    data object OnApplyDealsFilters : DealsAndGiveawayScreenUiEvent()

    // giveaways bottom sheet events
    data class OnToggleGiveawaysBottomSheet(
        val isVisible: Boolean
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnGiveawaysSortByFilterChanged(
        val sortBy: String?
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnGiveawaysTypeFilterChanged(
        val type: String?
    ) : DealsAndGiveawayScreenUiEvent()

    data class OnGiveawaysPlatformFilterChanged(
        val platform: String?
    ) : DealsAndGiveawayScreenUiEvent()

    data object OnApplyGiveawaysFilters : DealsAndGiveawayScreenUiEvent()
}
