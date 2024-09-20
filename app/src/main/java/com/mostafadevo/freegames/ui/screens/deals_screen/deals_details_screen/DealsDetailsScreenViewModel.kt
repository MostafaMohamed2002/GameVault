package com.mostafadevo.freegames.ui.screens.deals_screen.deals_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.freegames.domain.repository.CheapSharkRepository
import com.mostafadevo.freegames.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DealsDetailsScreenViewModel @Inject constructor(
    private val repo : CheapSharkRepository
) :ViewModel() {
    private val _uiState = MutableStateFlow(DealsDetailsScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun getDealDetails(dealId: String) {
        viewModelScope.launch {
            repo.getDealDetails(dealId).collectLatest {
                when(it){
                    is ResultWrapper.Error -> {
                        Timber.e(it.message)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                    is ResultWrapper.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is ResultWrapper.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, deal = it.data)
                    }
                }
            }
        }
    }
}
