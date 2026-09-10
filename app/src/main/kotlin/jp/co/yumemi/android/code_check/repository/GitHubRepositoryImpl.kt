/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.repository

import android.content.Context
import jp.co.yumemi.android.code_check.RepositoryItem
import jp.co.yumemi.android.code_check.api.GitHubApiClient
import jp.co.yumemi.android.code_check.api.RepositoryMapper

/**
 * Default implementation of GitHubRepository
 * Coordinates API client and mapper to fetch and parse repository data
 *
 * @param apiClient API client for network operations
 * @param context Android context for resources
 */
class GitHubRepositoryImpl(
    private val apiClient: GitHubApiClient,
    private val context: Context
) : GitHubRepository {

    /**
     * Search GitHub repositories by keyword
     * Fetches data from API and maps to domain models
     *
     * @param query Search keyword
     * @return List of RepositoryItem
     * @throws Exception if network or parsing fails
     */
    override suspend fun searchRepositories(query: String): List<RepositoryItem> {
        val responseBody = apiClient.searchRepositories(query)
        return RepositoryMapper.parseSearchResponse(responseBody, context)
    }

    /**
     * Close the repository and release all resources
     * Cascades close to the API client
     */
    override fun close() {
        apiClient.close()
    }
}
