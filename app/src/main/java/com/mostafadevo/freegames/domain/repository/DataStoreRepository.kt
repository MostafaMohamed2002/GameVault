package com.mostafadevo.freegames.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveSearchHistory(searchQueries: List<String>)
    suspend fun getSearchHistory(): Flow<List<String>>
}
