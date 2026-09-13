package jp.co.yumemi.android.codecheck.feature.bookmarks

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.BookmarkRepository
import jp.co.yumemi.android.codecheck.feature.bookmarks.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarksViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
    private val bookmarksFlow = MutableStateFlow<List<RepositoryItem>>(emptyList())

    private val fakeRepoItem = RepositoryItem(
        id = 12345L,
        name = "octocat/Hello-World",
        owner = Owner(login = "octocat", avatarUrl = "https://avatars.githubusercontent.com/u/583231"),
        language = "Kotlin",
        stargazersCount = 9999L,
        watchersCount = 9999L,
        forksCount = 1234L,
        openIssuesCount = 5L,
        description = "My first repo",
        htmlUrl = "https://github.com/octocat/Hello-World"
    )

    private fun createViewModel(): BookmarksViewModel {
        every { bookmarkRepository.getBookmarks() } returns bookmarksFlow
        return BookmarksViewModel(bookmarkRepository)
    }

    @Test
    fun uiState_whenBookmarksEmpty_emitsEmptyState() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is BookmarksUiState.Empty)
        }
    }

    @Test
    fun uiState_whenBookmarksExist_emitsSuccessState() = runTest {
        bookmarksFlow.value = listOf(fakeRepoItem)
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is BookmarksUiState.Success)
            assertEquals(listOf(fakeRepoItem), (item as BookmarksUiState.Success).repositories)
        }
    }

    @Test
    fun removeBookmark_byId_invokesRepositoryRemoveBookmarkById() = runTest {
        val viewModel = createViewModel()

        viewModel.removeBookmark(fakeRepoItem)

        coVerify(exactly = 1) { bookmarkRepository.removeBookmark(12345L) }
    }

    @Test
    fun removeBookmark_byNameWhenIdZero_invokesRepositoryRemoveBookmarkByName() = runTest {
        val viewModel = createViewModel()
        val itemWithoutId = fakeRepoItem.copy(id = 0L)

        viewModel.removeBookmark(itemWithoutId)

        coVerify(exactly = 1) { bookmarkRepository.removeBookmark("octocat/Hello-World") }
    }

    @Test
    fun clearAllBookmarks_invokesRepositoryClearBookmarks() = runTest {
        val viewModel = createViewModel()

        viewModel.clearAllBookmarks()

        coVerify(exactly = 1) { bookmarkRepository.clearBookmarks() }
    }
}
