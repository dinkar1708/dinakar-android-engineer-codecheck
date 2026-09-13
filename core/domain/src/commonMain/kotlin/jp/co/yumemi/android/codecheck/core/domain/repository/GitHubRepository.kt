package jp.co.yumemi.android.codecheck.core.domain.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchResult
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort

/**
 * Domain repository contract defining operations on GitHub repository data.
 * Pure Kotlin abstraction with zero Android SDK dependencies.
 */
interface GitHubRepository {

    /**
     * Search GitHub repositories with pagination, sorting, and filtering.
     *
     * @param query Search keyword
     * @param page Page number (1-indexed)
     * @param sort Sort ordering ([SearchSort])
     * @param filter Filter criteria ([SearchFilter])
     * @return Paginated [SearchResult]
     */
    suspend fun searchRepositories(
        query: String,
        page: Int = 1,
        sort: SearchSort = SearchSort.BEST_MATCH,
        filter: SearchFilter = SearchFilter()
    ): SearchResult

    /**
     * Fetch detailed information for a specific repository.
     *
     * @param owner Repository owner login / username
     * @param repo Repository name
     * @return Detailed [RepositoryItem]
     */
    suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem

    /**
     * Fetch detailed information for a specific repository by its database ID.
     *
     * @param id Numeric repository database ID
     * @return Detailed [RepositoryItem]
     */
    suspend fun getRepositoryById(id: Long): RepositoryItem
}
