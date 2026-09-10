package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.data.cache.InMemoryCache
import jp.co.yumemi.android.codecheck.core.data.util.retryWithExponentialBackoff
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import jp.co.yumemi.android.codecheck.core.network.error.NetworkException
import jp.co.yumemi.android.codecheck.core.network.mapper.toDomain

/**
 * Default production implementation of [GitHubRepository].
 * Coordinates network requests, in-memory LRU caching, and jittered exponential retry.
 */
class DefaultGitHubRepository(
    private val apiService: GitHubApiService,
    private val searchCache: InMemoryCache<String, List<RepositoryItem>> = InMemoryCache(),
    private val detailCache: InMemoryCache<String, RepositoryItem> = InMemoryCache()
) : GitHubRepository {

    override suspend fun searchRepositories(query: String): List<RepositoryItem> {
        val cached = searchCache.get(query)
        if (cached != null) {
            return cached
        }

        val response = retryWithExponentialBackoff(shouldRetry = ::isRecoverableError) {
            apiService.searchRepositories(query)
        }

        val items = response.items.map { it.toDomain() }
        searchCache.put(query, items)
        return items
    }

    override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem {
        val cacheKey = "$owner/$repo"
        val cached = detailCache.get(cacheKey)
        if (cached != null) {
            return cached
        }

        val response = retryWithExponentialBackoff(shouldRetry = ::isRecoverableError) {
            apiService.getRepositoryDetails(owner, repo)
        }

        val item = response.toDomain()
        detailCache.put(cacheKey, item)
        return item
    }

    private fun isRecoverableError(throwable: Throwable): Boolean {
        return when (throwable) {
            is NetworkException.RateLimitExceededException -> false
            is NetworkException.NotFoundException -> false
            else -> true
        }
    }
}
