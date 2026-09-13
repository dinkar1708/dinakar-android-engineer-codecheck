package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe implementation of [StarredRepository] managing starred repositories.
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
class DefaultStarredRepository : StarredRepository {

    private val mutex = Mutex()
    private val _starredIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _cachedItems = mutableMapOf<Long, RepositoryItem>()
    private val _nameToId = mutableMapOf<String, Long>()
    private val _starredList = MutableStateFlow<List<RepositoryItem>>(emptyList())

    /**
     * Observe the list of starred repositories from the session cache.
     * TODO: In production with OAuth, sync with GitHub API: GET /user/starred?per_page=30
     * Reference: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
     */
    override fun getStarredRepositories(): Flow<List<RepositoryItem>> = _starredList.asStateFlow()

    override fun getStarredIds(): Flow<Set<Long>> = _starredIds.asStateFlow()

    override fun isStarred(repositoryName: String): Flow<Boolean> {
        val trimmed = repositoryName.trim()
        return _starredList.map { list ->
            list.any { it.name.equals(trimmed, ignoreCase = true) }
        }
    }

    override fun isStarred(repositoryId: Long): Flow<Boolean> {
        return _starredIds.map { it.contains(repositoryId) }
    }

    /**
     * Stars a repository and stores it in the session cache.
     * TODO: In production with OAuth, sync with GitHub API: PUT /user/starred/{owner}/{repo}
     * Reference: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
     */
    override suspend fun starRepository(repository: RepositoryItem) {
        mutex.withLock {
            val id = repository.id
            if (id != 0L) {
                _cachedItems[id] = repository
                _nameToId[repository.name.lowercase()] = id
                _starredIds.value = _starredIds.value + id
            }

            val current = _starredList.value.toMutableList()
            current.removeAll {
                (id != 0L && it.id == id) || it.name.equals(repository.name, ignoreCase = true)
            }
            current.add(0, repository)
            _starredList.value = current
        }
    }

    /**
     * Unstars a repository by its repository ID.
     * TODO: In production with OAuth, sync with GitHub API: DELETE /user/starred/{owner}/{repo}
     * Reference: docs/02_project_architecture/api_spec/03_starred_repositories_api.md
     */
    override suspend fun unstarRepository(repositoryId: Long) {
        mutex.withLock {
            _starredIds.value = _starredIds.value - repositoryId
            _cachedItems.remove(repositoryId)
            _starredList.value = _starredList.value.filter { it.id != repositoryId }
        }
    }

    override suspend fun unstarRepository(repositoryName: String) {
        val trimmed = repositoryName.trim()
        mutex.withLock {
            val id = _nameToId[trimmed.lowercase()]
            if (id != null) {
                _starredIds.value = _starredIds.value - id
                _cachedItems.remove(id)
            }
            _starredList.value = _starredList.value.filter { !it.name.equals(trimmed, ignoreCase = true) }
        }
    }

    override suspend fun toggleStar(repository: RepositoryItem) {
        mutex.withLock {
            val isSaved = if (repository.id != 0L) {
                _starredIds.value.contains(repository.id)
            } else {
                _starredList.value.any { it.name.equals(repository.name, ignoreCase = true) }
            }

            if (isSaved) {
                if (repository.id != 0L) {
                    _starredIds.value = _starredIds.value - repository.id
                    _cachedItems.remove(repository.id)
                }
                _starredList.value = _starredList.value.filter {
                    (repository.id != 0L && it.id != repository.id) ||
                        (repository.id == 0L && !it.name.equals(repository.name, ignoreCase = true))
                }
            } else {
                val id = repository.id
                if (id != 0L) {
                    _cachedItems[id] = repository
                    _nameToId[repository.name.lowercase()] = id
                    _starredIds.value = _starredIds.value + id
                }
                val current = _starredList.value.toMutableList()
                current.removeAll {
                    (id != 0L && it.id == id) || it.name.equals(repository.name, ignoreCase = true)
                }
                current.add(0, repository)
                _starredList.value = current
            }
        }
    }

    override suspend fun clearAllStars() {
        mutex.withLock {
            _starredIds.value = emptySet()
            _cachedItems.clear()
            _nameToId.clear()
            _starredList.value = emptyList()
        }
    }
}
