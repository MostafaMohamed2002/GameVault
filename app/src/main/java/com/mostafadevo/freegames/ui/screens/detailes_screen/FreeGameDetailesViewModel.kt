package com.mostafadevo.freegames.ui.screens.detailes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.freegames.domain.repository.FreeGamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreeGameDetailesViewModel @Inject constructor(
    private val repo : FreeGamesRepository
) :ViewModel(){
    private val _uiState = MutableStateFlow<FreeGameDetailesUiState>(FreeGameDetailesUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<FreeGameDetailesUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        _uiState.value.isDeveloperEqualPublisher = _uiState.value.game.developer == _uiState.value.game.publisher
    }
    fun getGameById(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getGameById(gameId).collect {
                when(it){
                    is com.mostafadevo.freegames.utils.ResultWrapper.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            game = it.data!!
                        )
                    }
                    is com.mostafadevo.freegames.utils.ResultWrapper.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )
                        _uiEffect.send(FreeGameDetailesUiEffect.ShowSnackBar(it.message!!))
                    }
                    is com.mostafadevo.freegames.utils.ResultWrapper.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}
