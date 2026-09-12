package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.data.cache.InMemoryCache
import jp.co.yumemi.android.codecheck.core.data.util.retryWithExponentialBackoff
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchResult
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
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
    private val searchCache: InMemoryCache<String, SearchResult> = InMemoryCache(),
    private val detailCache: InMemoryCache<String, RepositoryItem> = InMemoryCache()
) : GitHubRepository {

    override suspend fun searchRepositories(
        query: String,
        page: Int,
        sort: SearchSort,
        filter: SearchFilter
    ): SearchResult {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return SearchResult(emptyList(), 0, false)
        }

        // Construct search qualifiers for GitHub REST API v3
        val queryBuilder = StringBuilder(trimmed)
        if (!filter.language.isNullOrBlank()) {
            queryBuilder.append(" language:${filter.language}")
        }
        val minStars = filter.minStars
        if (minStars != null && minStars > 0) {
            queryBuilder.append(" stars:>=$minStars")
        }
        val updatedAfter = filter.updatedAfter ?: when (filter.updatedPeriod) {
            "year" -> "2026-01-01"
            "month" -> "2026-09-01"
            else -> null
        }
        if (!updatedAfter.isNullOrBlank()) {
            queryBuilder.append(" pushed:>=$updatedAfter")
        }
        val effectiveQuery = queryBuilder.toString()
        val cacheKey = "$effectiveQuery:$page:${sort.name}"

        val cached = searchCache.get(cacheKey)
        if (cached != null) {
            return cached
        }

        val response = retryWithExponentialBackoff(shouldRetry = ::isRecoverableError) {
            apiService.searchRepositories(
                query = effectiveQuery,
                page = page,
                sort = sort.apiValue,
                order = sort.order
            )
        }

        val items = response.items.map { it.toDomain() }
        val result = SearchResult(
            items = items,
            totalCount = response.totalCount,
            hasNextPage = (page * 30) < response.totalCount
        )
        searchCache.put(cacheKey, result)
        return result
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
