package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe implementation of [SearchHistoryRepository] maintaining up to [maxItems]
 * unique search queries in Most Recently Used (MRU) order.
 */
class DefaultSearchHistoryRepository(
    private val maxItems: Int = 7
) : SearchHistoryRepository {

    private val mutex = Mutex()
    private val _history = MutableStateFlow<List<String>>(emptyList())

    override fun getSearchHistory(): Flow<List<String>> = _history.asStateFlow()

    override suspend fun addSearchQuery(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return

        mutex.withLock {
            val current = _history.value.toMutableList()
            current.removeAll { it.equals(trimmed, ignoreCase = true) }
            current.add(0, trimmed)
            _history.value = if (current.size > maxItems) current.take(maxItems) else current
        }
    }

    override suspend fun removeSearchQuery(query: String) {
        val trimmed = query.trim()
        mutex.withLock {
            val current = _history.value.toMutableList()
            current.removeAll { it.equals(trimmed, ignoreCase = true) }
            _history.value = current
        }
    }

    override suspend fun clearSearchHistory() {
        mutex.withLock {
            _history.value = emptyList()
        }
    }
}
