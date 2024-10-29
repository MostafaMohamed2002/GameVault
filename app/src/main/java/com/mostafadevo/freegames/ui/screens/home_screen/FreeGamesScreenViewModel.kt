package com.mostafadevo.freegames.ui.screens.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.freegames.domain.repository.FreeGamesRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FreeGamesScreenViewModel @Inject constructor(
    private val repository: FreeGamesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FreeGamesScreenUiState())
    val uiState: StateFlow<FreeGamesScreenUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<FreeGamesScreenUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            repository.getGames().collectLatest { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        _uiState.value =
                            _uiState.value.copy(games = result.data!!, isLoading = false)
                    }

                    is ResultWrapper.Error -> {
                        _uiEffect.send(
                            FreeGamesScreenUiEffect.ShowSnackbar(
                                result.message ?: "An error occurred"
                            )
                        )
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun getGamesByFilters(platform: String?, category: String?, sortBy: String?) {
        viewModelScope.launch {
            repository.getGamesByFilters(platform, category, sortBy).collectLatest { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        _uiState.value =
                            _uiState.value.copy(games = result.data!!, isLoading = false)
                    }

                    is ResultWrapper.Error -> {
                        _uiEffect.send(
                            FreeGamesScreenUiEffect.ShowSnackbar(
                                result.message ?: "An error occurred"
                            )
                        )
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
    fun onEvent(event: FreeGamesScreenEvents) {
        when (event) {
            is FreeGamesScreenEvents.onCategorySelected -> {
                _uiState.value = _uiState.value.copy(category = event.category)
            }
            is FreeGamesScreenEvents.onPlatformSelected -> {
                _uiState.value = _uiState.value.copy(platform = event.platform)
            }
            is FreeGamesScreenEvents.onSortBySelected -> {
                _uiState.value = _uiState.value.copy(sortBy = event.sortBy)
            }

            FreeGamesScreenEvents.onSearchWithFilters -> {
                getGamesByFilters(
                    _uiState.value.platform,
                    _uiState.value.category,
                    _uiState.value.sortBy
                )
            }
        }
    }
}
