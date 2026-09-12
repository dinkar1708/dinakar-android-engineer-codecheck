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
 * Stores repository IDs as the source of truth, with local in-memory cache for instant offline access.
 */
class DefaultBookmarkRepository : BookmarkRepository {

    private val mutex = Mutex()
    private val _bookmarkedIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _cachedItems = mutableMapOf<Long, RepositoryItem>()
    private val _nameToId = mutableMapOf<String, Long>()
    private val _bookmarksList = MutableStateFlow<List<RepositoryItem>>(emptyList())

    override fun getBookmarks(): Flow<List<RepositoryItem>> = _bookmarksList.asStateFlow()

    override fun getBookmarkedIds(): Flow<Set<Long>> = _bookmarkedIds.asStateFlow()

    override fun isBookmarked(repositoryName: String): Flow<Boolean> {
        val trimmed = repositoryName.trim()
        return _bookmarksList.map { list ->
            list.any { it.name.equals(trimmed, ignoreCase = true) }
        }
    }

    override fun isBookmarked(repositoryId: Long): Flow<Boolean> {
        return _bookmarkedIds.map { it.contains(repositoryId) }
    }

    override suspend fun addBookmark(repository: RepositoryItem) {
        mutex.withLock {
            val id = repository.id
            if (id != 0L) {
                _cachedItems[id] = repository
                _nameToId[repository.name.lowercase()] = id
                _bookmarkedIds.value = _bookmarkedIds.value + id
            }

            val current = _bookmarksList.value.toMutableList()
            current.removeAll {
                (id != 0L && it.id == id) || it.name.equals(repository.name, ignoreCase = true)
            }
            current.add(0, repository)
            _bookmarksList.value = current
        }
    }

    override suspend fun removeBookmark(repositoryId: Long) {
        mutex.withLock {
            _bookmarkedIds.value = _bookmarkedIds.value - repositoryId
            _cachedItems.remove(repositoryId)
            _bookmarksList.value = _bookmarksList.value.filter { it.id != repositoryId }
        }
    }

    override suspend fun removeBookmark(repositoryName: String) {
        val trimmed = repositoryName.trim()
        mutex.withLock {
            val id = _nameToId[trimmed.lowercase()]
            if (id != null) {
                _bookmarkedIds.value = _bookmarkedIds.value - id
                _cachedItems.remove(id)
            }
            _bookmarksList.value = _bookmarksList.value.filter { !it.name.equals(trimmed, ignoreCase = true) }
        }
    }

    override suspend fun toggleBookmark(repository: RepositoryItem) {
        mutex.withLock {
            val isSaved = if (repository.id != 0L) {
                _bookmarkedIds.value.contains(repository.id)
            } else {
                _bookmarksList.value.any { it.name.equals(repository.name, ignoreCase = true) }
            }

            if (isSaved) {
                if (repository.id != 0L) {
                    _bookmarkedIds.value = _bookmarkedIds.value - repository.id
                    _cachedItems.remove(repository.id)
                }
                _bookmarksList.value = _bookmarksList.value.filter {
                    (repository.id != 0L && it.id != repository.id) ||
                        (repository.id == 0L && !it.name.equals(repository.name, ignoreCase = true))
                }
            } else {
                val id = repository.id
                if (id != 0L) {
                    _cachedItems[id] = repository
                    _nameToId[repository.name.lowercase()] = id
                    _bookmarkedIds.value = _bookmarkedIds.value + id
                }
                val current = _bookmarksList.value.toMutableList()
                current.removeAll {
                    (id != 0L && it.id == id) || it.name.equals(repository.name, ignoreCase = true)
                }
                current.add(0, repository)
                _bookmarksList.value = current
            }
        }
    }

    override suspend fun clearBookmarks() {
        mutex.withLock {
            _bookmarkedIds.value = emptySet()
            _cachedItems.clear()
            _nameToId.clear()
            _bookmarksList.value = emptyList()
        }
    }
}
