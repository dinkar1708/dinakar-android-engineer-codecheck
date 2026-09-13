package jp.co.yumemi.android.codecheck.core.domain.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract for managing starred repositories.
 * Stores repository IDs as the source of truth, with local session cache for instant offline browsing.
 */
interface StarredRepository {
    /**
     * Observe the list of starred repositories.
     */
    fun getStarredRepositories(): Flow<List<RepositoryItem>>

    /**
     * Observe the set of starred repository IDs.
     */
    fun getStarredIds(): Flow<Set<Long>>

    /**
     * Check whether a specific repository is starred by repository name.
     */
    fun isStarred(repositoryName: String): Flow<Boolean>

    /**
     * Check whether a specific repository is starred by repository ID.
     */
    fun isStarred(repositoryId: Long): Flow<Boolean>

    /**
     * Star a repository.
     */
    suspend fun starRepository(repository: RepositoryItem)

    /**
     * Unstar a repository by its repository name or full name.
     */
    suspend fun unstarRepository(repositoryName: String)

    /**
     * Unstar a repository by its repository ID.
     */
    suspend fun unstarRepository(repositoryId: Long)

    /**
     * Toggle star status for a repository.
     */
    suspend fun toggleStar(repository: RepositoryItem)

    /**
     * Clear all starred repositories.
     */
    suspend fun clearAllStars()
}
