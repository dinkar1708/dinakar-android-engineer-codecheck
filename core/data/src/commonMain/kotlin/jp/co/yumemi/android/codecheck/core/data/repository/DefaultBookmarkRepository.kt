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
 * Thread-safe implementation of [BookmarkRepository] managing starred/bookmarked repositories.
 *
 * Architecture Note (Unauthenticated Session Cache vs Production OAuth):
 * - Current Implementation: Stores starred repositories in an in-memory session cache while the app
 *   is running to provide 0ms responsiveness, 100% offline capability, and to avoid exhausting GitHub's
 *   strict unauthenticated 60 requests/hour rate limit.
 * - TODO (Production Roadmap): In an authenticated production release with GitHub user login/OAuth:
 *   1. Get/List: Fetch user's stars via GitHub API `GET /user/starred?per_page=30`
 *   2. Save/Star: Call GitHub API `PUT /user/starred/{owner}/{repo}`
 *   3. Delete/Unstar: Call GitHub API `DELETE /user/starred/{owner}/{repo}`
 *   See full API specification: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
 */
class DefaultBookmarkRepository : BookmarkRepository {

    private val mutex = Mutex()
    private val _bookmarkedIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _cachedItems = mutableMapOf<Long, RepositoryItem>()
    private val _nameToId = mutableMapOf<String, Long>()
    private val _bookmarksList = MutableStateFlow<List<RepositoryItem>>(emptyList())

    /**
     * Observe the list of starred repositories from the session cache.
     * TODO: In production with OAuth, sync with GitHub API: GET /user/starred?per_page=30
     * Reference: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
     */
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

    /**
     * Adds a repository to the session cache.
     * TODO: In production with OAuth, sync with GitHub API: PUT /user/starred/{owner}/{repo}
     * Reference: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
     */
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

    /**
     * Removes a repository from the session cache by ID.
     * TODO: In production with OAuth, sync with GitHub API: DELETE /user/starred/{owner}/{repo}
     * Reference: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
     */
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
