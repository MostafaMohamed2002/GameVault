package com.mostafadevo.freegames.domain.repository

import com.mostafadevo.freegames.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveSearchHistory(searchQueries: List<String>)
    suspend fun getSearchHistory(): Flow<List<String>>
    suspend fun saveThemePreference(themePreference: ThemePreference)
    fun getThemePreference(): Flow<ThemePreference>
}
