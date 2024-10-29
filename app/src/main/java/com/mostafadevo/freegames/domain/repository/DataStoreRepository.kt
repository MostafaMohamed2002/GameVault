package com.mostafadevo.freegames.domain.repository

import com.mostafadevo.freegames.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveSearchHistory(searchQueries: List<String>)
    suspend fun getSearchHistory(): Flow<List<String>>
    suspend fun saveThemePreference(themePreference: ThemePreference)
    fun getThemePreference(): Flow<ThemePreference>
    fun getDynamicThemePereference(): Flow<Boolean>
    suspend fun saveDynamicThemePreference(dynamicTheme: Boolean)
    suspend fun clearSearchHistory()
    suspend fun setSearchHistoryLimit(limit: Int)
    fun getSearchHistoryLimit(): Flow<Int>
}
