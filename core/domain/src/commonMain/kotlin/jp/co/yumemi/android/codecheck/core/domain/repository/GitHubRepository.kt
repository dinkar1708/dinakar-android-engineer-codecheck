package jp.co.yumemi.android.codecheck.core.domain.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem

/**
 * Domain repository contract defining operations on GitHub repository data.
 * Pure Kotlin abstraction with zero Android SDK dependencies.
 */
interface GitHubRepository {

    /**
     * Search GitHub repositories by query keyword.
     *
     * @param query Search keyword
     * @return List of matching [RepositoryItem]
     */
    suspend fun searchRepositories(query: String): List<RepositoryItem>

    /**
     * Fetch detailed information for a specific repository.
     *
     * @param owner Repository owner login / username
     * @param repo Repository name
     * @return Detailed [RepositoryItem]
     */
    suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem
}
