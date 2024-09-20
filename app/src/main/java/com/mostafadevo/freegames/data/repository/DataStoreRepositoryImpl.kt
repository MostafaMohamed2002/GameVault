package com.mostafadevo.freegames.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mostafadevo.freegames.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DataStoreRepository {

    private val dataStore = context.dataStore

    // Save search history as a comma-separated string
    override suspend fun saveSearchHistory(searchQueries: List<String>) {
        val searchHistoryString = searchQueries.joinToString(",")
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("search_history_key")] = searchHistoryString
        }
    }

    // Get search history as a list of strings
    override suspend fun getSearchHistory(): Flow<List<String>> {
        return dataStore.data.catch { exception ->
            if (exception is Exception) {
                Timber.e(exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val searchHistoryString = preferences[stringPreferencesKey("search_history_key")] ?: ""
            if (searchHistoryString.isNotEmpty()) {
                searchHistoryString.split(",")
            } else {
                emptyList()
            }
        }
    }
}
