package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe implementation of [BookmarkRepository] for managing offline bookmarked repositories.
 */
class DefaultBookmarkRepository : BookmarkRepository {

    private val mutex = Mutex()
    private val _bookmarks = MutableStateFlow<List<RepositoryItem>>(emptyList())

    override fun getBookmarks(): Flow<List<RepositoryItem>> = _bookmarks.asStateFlow()

    override fun isBookmarked(repositoryName: String): Flow<Boolean> {
        val trimmed = repositoryName.trim()
        return _bookmarks.map { list ->
            list.any { it.name.equals(trimmed, ignoreCase = true) }
        }
    }

    override suspend fun addBookmark(repository: RepositoryItem) {
        mutex.withLock {
            val current = _bookmarks.value.toMutableList()
            current.removeAll { it.name.equals(repository.name, ignoreCase = true) }
            current.add(0, repository)
            _bookmarks.value = current
        }
    }

    override suspend fun removeBookmark(repositoryName: String) {
        val trimmed = repositoryName.trim()
        mutex.withLock {
            val current = _bookmarks.value.toMutableList()
            current.removeAll { it.name.equals(trimmed, ignoreCase = true) }
            _bookmarks.value = current
        }
    }

    override suspend fun clearBookmarks() {
        mutex.withLock {
            _bookmarks.value = emptyList()
        }
    }
}
