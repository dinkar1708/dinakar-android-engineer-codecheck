package jp.co.yumemi.android.codecheck.core.domain.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract for managing offline bookmarked repositories.
 * Stores repository IDs as the source of truth, with local cache for offline browsing.
 */
interface BookmarkRepository {
    /**
     * Observe the list of bookmarked repositories.
     */
    fun getBookmarks(): Flow<List<RepositoryItem>>

    /**
     * Observe the set of bookmarked repository IDs.
     */
    fun getBookmarkedIds(): Flow<Set<Long>>

    /**
     * Check whether a specific repository is bookmarked by repository name.
     */
    fun isBookmarked(repositoryName: String): Flow<Boolean>

    /**
     * Check whether a specific repository is bookmarked by repository ID.
     */
    fun isBookmarked(repositoryId: Long): Flow<Boolean>

    /**
     * Add a repository to bookmarks.
     */
    suspend fun addBookmark(repository: RepositoryItem)

    /**
     * Remove a repository from bookmarks by its repository name or full name.
     */
    suspend fun removeBookmark(repositoryName: String)

    /**
     * Remove a repository from bookmarks by its repository ID.
     */
    suspend fun removeBookmark(repositoryId: Long)

    /**
     * Toggle bookmark status for a repository.
     */
    suspend fun toggleBookmark(repository: RepositoryItem)

    /**
     * Clear all bookmarked repositories.
     */
    suspend fun clearBookmarks()
}
