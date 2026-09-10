/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.RepositoryItem

/**
 * Repository interface for GitHub API operations
 * Provides abstraction layer between data sources and business logic
 */
interface GitHubRepository {

    /**
     * Search GitHub repositories by keyword
     * @param query Search keyword
     * @return List of RepositoryItem matching the query
     * @throws Exception if the operation fails
     */
    suspend fun searchRepositories(query: String): List<RepositoryItem>
}
