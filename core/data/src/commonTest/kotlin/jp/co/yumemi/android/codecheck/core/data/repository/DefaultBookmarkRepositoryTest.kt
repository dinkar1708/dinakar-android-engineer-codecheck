package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultBookmarkRepositoryTest {

    private val sampleRepo1 = RepositoryItem(
        name = "compose-samples",
        owner = Owner(avatarUrl = "https://example.com/avatar.png", login = "android"),
        stargazersCount = 5000L
    )

    private val sampleRepo2 = RepositoryItem(
        name = "kotlin",
        owner = Owner(avatarUrl = "https://example.com/kotlin.png", login = "JetBrains"),
        stargazersCount = 45000L
    )

    @Test
    fun getBookmarks_initiallyEmpty() = runTest {
        val repository = DefaultBookmarkRepository()
        val bookmarks = repository.getBookmarks().first()
        assertTrue(bookmarks.isEmpty())
    }

    @Test
    fun addBookmark_addsItemAndUpdatesIsBookmarked() = runTest {
        val repository = DefaultBookmarkRepository()

        repository.addBookmark(sampleRepo1)

        val bookmarks = repository.getBookmarks().first()
        assertEquals(1, bookmarks.size)
        assertEquals(sampleRepo1, bookmarks.first())
        assertTrue(repository.isBookmarked("compose-samples").first())
        assertFalse(repository.isBookmarked("kotlin").first())
    }

    @Test
    fun removeBookmark_removesItemAndUpdatesIsBookmarked() = runTest {
        val repository = DefaultBookmarkRepository()

        repository.addBookmark(sampleRepo1)
        repository.addBookmark(sampleRepo2)
        repository.removeBookmark("compose-samples")

        val bookmarks = repository.getBookmarks().first()
        assertEquals(listOf(sampleRepo2), bookmarks)
        assertFalse(repository.isBookmarked("compose-samples").first())
        assertTrue(repository.isBookmarked("kotlin").first())
    }

    @Test
    fun clearBookmarks_clearsAllItems() = runTest {
        val repository = DefaultBookmarkRepository()

        repository.addBookmark(sampleRepo1)
        repository.addBookmark(sampleRepo2)
        repository.clearBookmarks()

        val bookmarks = repository.getBookmarks().first()
        assertTrue(bookmarks.isEmpty())
        assertFalse(repository.isBookmarked("compose-samples").first())
    }
}
