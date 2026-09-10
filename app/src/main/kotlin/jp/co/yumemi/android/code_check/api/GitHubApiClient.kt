/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.api

/**
 * Interface for GitHub API client
 * Provides methods to interact with GitHub API endpoints
 */
interface GitHubApiClient {
    /**
     * Search repositories by query
     * @param query Search keyword
     * @return JSON response as String
     * @throws Exception if network request fails
     */
    suspend fun searchRepositories(query: String): String
}
