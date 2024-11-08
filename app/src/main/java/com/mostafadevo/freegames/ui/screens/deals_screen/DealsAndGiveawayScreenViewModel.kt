package com.mostafadevo.freegames.ui.screens.deals_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.freegames.domain.repository.CheapSharkRepository
import com.mostafadevo.freegames.domain.repository.DataStoreRepository
import com.mostafadevo.freegames.domain.repository.GamePowerRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DealsAndGiveawayScreenViewModel @Inject constructor(
    private val cheapSharkRepository: CheapSharkRepository,
    private val datastore: DataStoreRepository,
    private val gamePowerRepository: GamePowerRepository,
    private val datastoreRepo: DataStoreRepository
) : ViewModel() {

    private val _dealsAndGiveawayScreenUiState = MutableStateFlow(DealsAndGiveawayScreenUiState())
    val dealsAndGiveawayScreenUiState = _dealsAndGiveawayScreenUiState.asStateFlow()

    private val _uiEffect = Channel<DealsAndGiveawayScreenUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadDeals()
        loadSearchHistory()
        loadSearchHistoryLimit()
    }

    private fun loadSearchHistoryLimit() {
        viewModelScope.launch {
            datastoreRepo.getSearchHistoryLimit().collectLatest {
                _dealsAndGiveawayScreenUiState.value =
                    _dealsAndGiveawayScreenUiState.value.copy(searchHistoryLimit = it)
            }
        }
    }

    private fun loadSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getSearchHistory().collectLatest { searchHistory ->
                withContext(Dispatchers.Main) {
                    _dealsAndGiveawayScreenUiState.value =
                        _dealsAndGiveawayScreenUiState.value.copy(
                            dealsSearchHistory = searchHistory.reversed()
                        )
                }
            }
        }
    }

    fun onEvent(event: DealsAndGiveawayScreenUiEvent) {
        when (event) {
            DealsAndGiveawayScreenUiEvent.OnLoadDeals -> loadDeals()
            DealsAndGiveawayScreenUiEvent.OnLoadGiveaways -> loadGiveaways()
            is DealsAndGiveawayScreenUiEvent.OnSearchBarTextChanged -> searchBarTextChanged(
                event.text
            )
            is DealsAndGiveawayScreenUiEvent.OnSearchBarTextSubmit -> onSearch()
            is DealsAndGiveawayScreenUiEvent.OnToggleSearchBar -> toggleSearchBar(
                event.isSearchBarVisible
            )
            is DealsAndGiveawayScreenUiEvent.onTabSelected -> {
                changeSelectedTab(event.tabIndex)
                if (event.tabIndex == 1 && _dealsAndGiveawayScreenUiState.value.giveaways.isNullOrEmpty()) {
                    loadGiveaways()
                }
            }

            is DealsAndGiveawayScreenUiEvent.OnToggleBottomSheet -> toggleBottomSheetVisibility(
                event.isVisible
            )
            DealsAndGiveawayScreenUiEvent.OnClearSearchBarDeals -> clearSearchBarListOfDeals()
            DealsAndGiveawayScreenUiEvent.OnApplyDealsFilters -> loadDeals()
            is DealsAndGiveawayScreenUiEvent.OnDescFilterChanged -> descendingFilterChnaged(
                event.desc
            )
            is DealsAndGiveawayScreenUiEvent.OnLowerPriceFilterChanged -> lowerPriceFilterChanged(
                event.lowerPrice
            )
            is DealsAndGiveawayScreenUiEvent.OnOnSaleFilterChanged -> saleFilterChanged(
                event.onSale
            )
            is DealsAndGiveawayScreenUiEvent.OnSortByFilterChanged -> sortbyFilterChanged(
                event.sortBy
            )
            is DealsAndGiveawayScreenUiEvent.OnStoreFilterChanged -> storeFilterChanged(
                event.storeId
            )
            is DealsAndGiveawayScreenUiEvent.OnUpperPriceFilterChanged -> upperPriceFilterChanged(
                event.upperPrice
            )

            is DealsAndGiveawayScreenUiEvent.OnGiveawaysPlatformFilterChanged -> {
                _dealsAndGiveawayScreenUiState.value =
                    _dealsAndGiveawayScreenUiState.value.copy(
                        giveawaysFilterPlatform = event.platform
                    )
            }
            is DealsAndGiveawayScreenUiEvent.OnGiveawaysSortByFilterChanged -> {
                _dealsAndGiveawayScreenUiState.value =
                    _dealsAndGiveawayScreenUiState.value.copy(giveawaysFilterSortBy = event.sortBy)
            }
            is DealsAndGiveawayScreenUiEvent.OnGiveawaysTypeFilterChanged -> {
                _dealsAndGiveawayScreenUiState.value =
                    _dealsAndGiveawayScreenUiState.value.copy(giveawaysFilterType = event.type)
            }
            is DealsAndGiveawayScreenUiEvent.OnToggleGiveawaysBottomSheet -> {
                _dealsAndGiveawayScreenUiState.value =
                    _dealsAndGiveawayScreenUiState.value.copy(
                        isGiveawaysBottomSheetVisible = event.isVisible
                    )
            }

            DealsAndGiveawayScreenUiEvent.OnApplyGiveawaysFilters -> {
                loadGiveaways()
            }
        }
    }

    private fun upperPriceFilterChanged(upperPrice: Int) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(filterUpperPrice = upperPrice.toFloat())
    }

    private fun storeFilterChanged(storeId: String) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(filterStoreId = storeId)
    }

    private fun sortbyFilterChanged(sortBy: String) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(filterSortBy = sortBy)
    }

    private fun saleFilterChanged(onSale: Boolean) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(filterOnSale = onSale)
    }

    private fun lowerPriceFilterChanged(lowerPrice: Int) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(filterLowerPrice = lowerPrice.toFloat())
    }

    private fun descendingFilterChnaged(desc: Boolean) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(filterDesc = desc)
    }

    private fun onSearch() {
        saveSearchHistory()
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(isDealsSearchBarLoading = true)
        viewModelScope.launch {
            cheapSharkRepository.getDeals(
                title = _dealsAndGiveawayScreenUiState.value.dealsSearchBarText,
                storeId = null,
                lowerPrice = null,
                upperPrice = null,
                onSale = null,
                sortBy = null,
                desc = null
            ).collectLatest { result ->
                when (result) {
                    is ResultWrapper.Error -> {
                        _uiEffect.send(
                            DealsAndGiveawayScreenUiEffect.ShowSnackBar(result.message!!)
                        )
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(
                                isDealsSearchBarLoading = false
                            )
                    }

                    is ResultWrapper.Loading -> {
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(
                                isDealsSearchBarLoading = true
                            )
                    }

                    is ResultWrapper.Success -> {
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(
                                isDealsSearchBarLoading = false,
                                dealsSearchBardata = result.data
                            )
                    }
                }
            }
        }
    }

    private fun searchBarTextChanged(text: String) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(dealsSearchBarText = text)
    }

    private fun toggleBottomSheetVisibility(visible: Boolean) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(isBottomSheetVisible = visible)
    }

    private fun clearSearchBarListOfDeals() {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(dealsSearchBardata = emptyList())
    }

    private fun loadGiveaways() {
        viewModelScope.launch {
            gamePowerRepository.getGiveaways(
                platform = _dealsAndGiveawayScreenUiState.value.giveawaysFilterPlatform,
                sortBy = _dealsAndGiveawayScreenUiState.value.giveawaysFilterSortBy,
                type = _dealsAndGiveawayScreenUiState.value.giveawaysFilterType
            ).collectLatest { result ->
                when (result) {
                    is ResultWrapper.Error -> {
                        _uiEffect.send(
                            DealsAndGiveawayScreenUiEffect.ShowSnackBar(result.message!!)
                        )
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(isGiveawaysLoading = false)
                    }

                    is ResultWrapper.Loading -> {
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(isGiveawaysLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(
                                isGiveawaysLoading = false,
                                giveaways = result.data
                            )
                    }
                }
            }
        }
    }

    private fun toggleSearchBar(searchBarVisible: Boolean) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(isDealsSearchBarActive = searchBarVisible)
    }

    private fun loadDeals() {
        viewModelScope.launch {
            cheapSharkRepository.getDeals(
                storeId = _dealsAndGiveawayScreenUiState.value.filterStoreId,
                lowerPrice = _dealsAndGiveawayScreenUiState.value.filterLowerPrice?.toInt(),
                upperPrice = _dealsAndGiveawayScreenUiState.value.filterUpperPrice?.toInt(),
                onSale = _dealsAndGiveawayScreenUiState.value.filterOnSale,
                sortBy = _dealsAndGiveawayScreenUiState.value.filterSortBy,
                desc = _dealsAndGiveawayScreenUiState.value.filterDesc,
                title = null
            ).collectLatest { result ->
                when (result) {
                    is ResultWrapper.Error -> {
                        _uiEffect.send(
                            DealsAndGiveawayScreenUiEffect.ShowSnackBar(result.message!!)
                        )
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(isDealsLoading = false)
                    }

                    is ResultWrapper.Loading -> {
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(isDealsLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        _dealsAndGiveawayScreenUiState.value =
                            _dealsAndGiveawayScreenUiState.value.copy(
                                isDealsLoading = false,
                                deals = result.data
                            )
                    }
                }
            }
        }
    }

    private fun changeSelectedTab(tabIndex: Int) {
        _dealsAndGiveawayScreenUiState.value =
            _dealsAndGiveawayScreenUiState.value.copy(selectedTab = tabIndex)
    }

    private fun saveSearchHistory() {
        viewModelScope.launch {
            val searchHistory = datastore.getSearchHistory().first().take(
                _dealsAndGiveawayScreenUiState.value.searchHistoryLimit
            )
            val newSearchHistory = searchHistory.toMutableList()
            newSearchHistory.add(_dealsAndGiveawayScreenUiState.value.dealsSearchBarText)
            datastore.saveSearchHistory(newSearchHistory)
        }
    }
}
