package com.mostafadevo.freegames.ui.screens.deals_screen

import android.util.LruCache
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.freegames.domain.repository.CheapSharkRepository
import com.mostafadevo.freegames.domain.repository.DataStoreRepository
import com.mostafadevo.freegames.ui.screens.deals_screen.search_bar.SearchBarUiEvent
import com.mostafadevo.freegames.ui.screens.deals_screen.search_bar.SearchBarUiState
import com.mostafadevo.freegames.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DealsScreenViewModel @Inject constructor(
    private val cheapSharkRepository: CheapSharkRepository,
    private val datastore: DataStoreRepository
) : ViewModel() {
    private val _searchBarUiState = MutableStateFlow(SearchBarUiState())
    val searchBarUiState = _searchBarUiState.asStateFlow()

    private val _dealsScreenUiState = MutableStateFlow(DealsScreenUiState())
    val dealsScreenUiState = _dealsScreenUiState.asStateFlow()

    private val _uiEffect = Channel<DealsScreenUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val colorCache = object : LruCache<String, Color>(100) {
        override fun sizeOf(key: String, value: Color): Int {
            return 1 // Every cache entry counts as size 1
        }
    }

    init {
        viewModelScope.launch {
            datastore.getSearchHistory().collect {
                _searchBarUiState.value =
                    _searchBarUiState.value.copy(searchHistory = it.reversed())
            }
        }
        getInitialDeals()
    }

    private fun getInitialDeals() {
        viewModelScope.launch {
            cheapSharkRepository.getDeals().collect {
                when (it) {
                    is ResultWrapper.Error -> {
                        Timber.e(it.message)
                        _dealsScreenUiState.value =
                            _dealsScreenUiState.value.copy(isLoading = false)
                        _uiEffect.send(
                            DealsScreenUiEffect.ShowSnackBar(
                                it.message ?: "An error occurred"
                            )
                        )
                    }

                    is ResultWrapper.Loading -> {
                        Timber.d("Loading")
                        _dealsScreenUiState.value =
                            _dealsScreenUiState.value.copy(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        Timber.d("Success")
                        _dealsScreenUiState.value = _dealsScreenUiState.value.copy(
                            isLoading = false, deals = it.data
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DealsScreenUiEvent) {
        when (event) {
            else -> {}
        }
    }

    fun onEvent(event: SearchBarUiEvent) {
        when (event) {
            SearchBarUiEvent.onSearch -> {
                viewModelScope.launch {
                    cheapSharkRepository.getDeals(_searchBarUiState.value.SearchBarText)
                        .collectLatest {
                            when (it) {
                                is ResultWrapper.Error -> {
                                    _searchBarUiState.value =
                                        _searchBarUiState.value.copy(isLoading = false)
                                }

                                is ResultWrapper.Loading -> {
                                    _searchBarUiState.value =
                                        _searchBarUiState.value.copy(isLoading = true)
                                }

                                is ResultWrapper.Success -> {
                                    _searchBarUiState.value = _searchBarUiState.value.copy(
                                        isLoading = false, deals = it.data
                                    )
                                    saveSearchHistory()
                                }
                            }
                        }
                }
            }

            is SearchBarUiEvent.onSearchBarActive -> {
                _searchBarUiState.value =
                    _searchBarUiState.value.copy(isSearchBarActive = event.isActive)
            }

            is SearchBarUiEvent.onSearchBarTextChange -> {
                _searchBarUiState.value = _searchBarUiState.value.copy(SearchBarText = event.text)
            }

            SearchBarUiEvent.clearDeals -> {
                _searchBarUiState.value = _searchBarUiState.value.copy(deals = emptyList())
            }
        }

    }

    private fun saveSearchHistory() {
        viewModelScope.launch {
            val searchHistory = datastore.getSearchHistory().first().take(5)
            val newSearchHistory = searchHistory.toMutableList()
            newSearchHistory.add(_searchBarUiState.value.SearchBarText ?: "")
            datastore.saveSearchHistory(newSearchHistory)
        }
    }

}
