package jp.co.yumemi.android.codecheck.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract defining operations for recent search queries.
 * Maintains up to a maximum number of recent queries (e.g., 7 items) in MRU order.
 */
interface SearchHistoryRepository {
    /**
     * Observe the list of recent search queries in MRU order.
     */
    fun getSearchHistory(): Flow<List<String>>

    /**
     * Add a search query to the history, promoting it to the top.
     */
    suspend fun addSearchQuery(query: String)

    /**
     * Remove an individual search query from history.
     */
    suspend fun removeSearchQuery(query: String)

    /**
     * Clear all search history.
     */
    suspend fun clearSearchHistory()
}
