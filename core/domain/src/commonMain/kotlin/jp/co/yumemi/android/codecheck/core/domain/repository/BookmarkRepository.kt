package jp.co.yumemi.android.codecheck.core.domain.repository

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract for managing offline bookmarked repositories.
 */
interface BookmarkRepository {
    /**
     * Observe the list of bookmarked repositories.
     */
    fun getBookmarks(): Flow<List<RepositoryItem>>

    /**
     * Check whether a specific repository is bookmarked.
     */
    fun isBookmarked(repositoryName: String): Flow<Boolean>

    /**
     * Add a repository to bookmarks.
     */
    suspend fun addBookmark(repository: RepositoryItem)

    /**
     * Remove a repository from bookmarks by its repository name or full name.
     */
    suspend fun removeBookmark(repositoryName: String)

    /**
     * Clear all bookmarked repositories.
     */
    suspend fun clearBookmarks()
}
