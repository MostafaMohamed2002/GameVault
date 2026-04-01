package com.mostafadevo.freegames.ui.screens.settings_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.freegames.domain.model.ThemePreference
import com.mostafadevo.freegames.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.distinctUntilChanged

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val analytics: com.mostafadevo.freegames.analytics.GameVaultAnalytics
) : ViewModel() {
    val themeState: StateFlow<ThemePreference> = dataStoreRepository.getThemePreference().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ThemePreference.SYSTEM
    )
    val dynamicThemeState: StateFlow<Boolean> = dataStoreRepository.getDynamicThemePereference().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )
    val searchHistoryLimitState: StateFlow<Int> = dataStoreRepository.getSearchHistoryLimit().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 5
    )
    val analyticsCollectionEnabledState: StateFlow<Boolean> = dataStoreRepository.getAnalyticsCollectionEnabled().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )
    fun onEvent(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.SaveThemePreference -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveThemePreference(event.themePreference)
                    // log theme change
                    analytics.logThemeChanged(event.themePreference.name.lowercase())
                }
            }

            is SettingsScreenEvent.SaveDynamicThemePreference -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveDynamicThemePreference(event.dynamicTheme)
                    analytics.logDynamicColorToggled(event.dynamicTheme)
                }
            }

            is SettingsScreenEvent.SaveAnalyticsCollectionPreference -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreRepository.saveAnalyticsCollectionEnabled(event.enabled)
                    analytics.setAnalyticsCollectionEnabled(event.enabled)
                }
            }

            SettingsScreenEvent.ClearSearchHistory -> clearSearchHistory()
            is SettingsScreenEvent.ChangeSearchHistoryLimit -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreRepository.setSearchHistoryLimit(event.limit)
                }
            }
        }
    }

    private fun clearSearchHistory() {
        // get search history from data store and clear it
        // then save it
        viewModelScope.launch {
            dataStoreRepository.clearSearchHistory()
        }
    }

    private suspend fun saveThemePreference(themePreference: ThemePreference) {
        dataStoreRepository.saveThemePreference(themePreference)
    }

    private suspend fun saveDynamicThemePreference(isDynamic: Boolean) {
        dataStoreRepository.saveDynamicThemePreference(isDynamic)
    }

}
