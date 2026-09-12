package jp.co.yumemi.android.codecheck.feature.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchResult
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
import jp.co.yumemi.android.codecheck.core.domain.repository.SearchHistoryRepository
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase
import jp.co.yumemi.android.codecheck.feature.search.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchRepositoriesUseCase: SearchRepositoriesUseCase = mockk()
    private val searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)

    private val fakeRepoItem = RepositoryItem(
        name = "android/compose-samples",
        owner = Owner(avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4"),
        language = "Kotlin",
        stargazersCount = 15000L,
        watchersCount = 15000L,
        forksCount = 3500L,
        openIssuesCount = 50L
    )

    private val fakeRepoItem2 = RepositoryItem(
        name = "android/nowinandroid",
        owner = Owner(avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4"),
        language = "Kotlin",
        stargazersCount = 18000L,
        watchersCount = 18000L,
        forksCount = 4000L,
        openIssuesCount = 60L
    )

    @Test
    fun initialUiState_whenNoSavedState_isIdle() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())
        }
        assertEquals("", viewModel.query.value)
        assertEquals(SearchSort.BEST_MATCH, viewModel.selectedSort.value)
        assertFalse(viewModel.filter.value.isActive)
    }

    @Test
    fun initialUiState_whenSavedStateContainsQuery_triggersSearch() = runTest {
        coEvery {
            searchRepositoriesUseCase("kotlin", page = 1, sort = SearchSort.BEST_MATCH, filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val savedStateHandle = SavedStateHandle(mapOf("last_search_query" to "kotlin"))
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is SearchUiState.Success)
            val success = item as SearchUiState.Success
            assertEquals(listOf(fakeRepoItem), success.repositories)
            assertEquals(1, success.totalCount)
            assertFalse(success.hasNextPage)
        }
        assertEquals("kotlin", viewModel.query.value)
    }

    @Test
    fun onQueryChanged_updatesQueryAndDebouncesSearch() = runTest {
        coEvery {
            searchRepositoriesUseCase("android", page = 1, sort = any(), filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.onQueryChanged("android")
        assertEquals("android", viewModel.query.value)
        assertEquals("android", savedStateHandle.get<String>("last_search_query"))

        // Advance debounce time (past 500ms delay)
        advanceTimeBy(600L)

        val currentState = viewModel.uiState.value
        assertTrue(currentState is SearchUiState.Success)
        assertEquals(listOf(fakeRepoItem), (currentState as SearchUiState.Success).repositories)
    }

    @Test
    fun searchRepositories_blankQuery_resetsToIdle() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.searchRepositories("   ")

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun searchRepositories_successNonEmpty_emitsSuccess() = runTest {
        coEvery {
            searchRepositoriesUseCase("android", page = 1, sort = any(), filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.searchRepositories("android")

            assertEquals(
                SearchUiState.Success(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false),
                awaitItem()
            )
        }

        coVerify(exactly = 1) { searchRepositoriesUseCase("android", 1, any(), any()) }
    }

    @Test
    fun searchRepositories_successEmpty_emitsEmpty() = runTest {
        coEvery {
            searchRepositoriesUseCase("unknown_xyz", page = 1, sort = any(), filter = any())
        } returns SearchResult(emptyList(), totalCount = 0, hasNextPage = false)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.searchRepositories("unknown_xyz")

            assertEquals(SearchUiState.Empty, awaitItem())
        }

        coVerify(exactly = 1) { searchRepositoriesUseCase("unknown_xyz", 1, any(), any()) }
    }

    @Test
    fun searchRepositories_error_emitsError() = runTest {
        coEvery {
            searchRepositoriesUseCase("error_query", page = 1, sort = any(), filter = any())
        } throws IOException("Network timeout")

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.searchRepositories("error_query")

            val state = awaitItem()
            assertTrue(state is SearchUiState.Error)
            assertEquals("Network timeout", (state as SearchUiState.Error).message)
        }
    }

    @Test
    fun onSortChanged_triggersNewSearchWithUpdatedSort() = runTest {
        coEvery {
            searchRepositoriesUseCase("kotlin", page = 1, sort = SearchSort.STARS, filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val savedStateHandle = SavedStateHandle(mapOf("last_search_query" to "kotlin"))
        coEvery {
            searchRepositoriesUseCase("kotlin", page = 1, sort = SearchSort.BEST_MATCH, filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)
        viewModel.onSortChanged(SearchSort.STARS)

        assertEquals(SearchSort.STARS, viewModel.selectedSort.value)
        coVerify(exactly = 1) { searchRepositoriesUseCase("kotlin", page = 1, sort = SearchSort.STARS, filter = any()) }
    }

    @Test
    fun loadNextPage_appendsItemsAndUpdatesPagination() = runTest {
        coEvery {
            searchRepositoriesUseCase("android", page = 1, sort = any(), filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 2, hasNextPage = true)

        coEvery {
            searchRepositoriesUseCase("android", page = 2, sort = any(), filter = any())
        } returns SearchResult(listOf(fakeRepoItem2), totalCount = 2, hasNextPage = false)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)
        viewModel.onQueryChanged("android")
        viewModel.searchRepositories("android")

        val page1State = viewModel.uiState.value as SearchUiState.Success
        assertEquals(1, page1State.repositories.size)
        assertTrue(page1State.hasNextPage)

        viewModel.loadNextPage()

        val page2State = viewModel.uiState.value as SearchUiState.Success
        assertEquals(2, page2State.repositories.size)
        assertEquals(listOf(fakeRepoItem, fakeRepoItem2), page2State.repositories)
        assertFalse(page2State.hasNextPage)
        assertFalse(page2State.isLoadingMore)
    }

    @Test
    fun clearQuery_resetsQueryAndStateToIdle() = runTest {
        coEvery {
            searchRepositoriesUseCase("query", page = 1, sort = any(), filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)
        viewModel.onQueryChanged("query")
        viewModel.searchRepositories("query")

        viewModel.clearQuery()

        assertEquals("", viewModel.query.value)
        assertEquals("", savedStateHandle.get<String>("last_search_query"))
        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun successfulSearch_recordsQueryInSearchHistory() = runTest {
        coEvery {
            searchRepositoriesUseCase("kotlin", page = 1, sort = any(), filter = any())
        } returns SearchResult(listOf(fakeRepoItem), totalCount = 1, hasNextPage = false)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)
        viewModel.searchRepositories("kotlin")

        coVerify { searchHistoryRepository.addSearchQuery("kotlin") }
    }

    @Test
    fun removeSearchHistory_delegatesToRepository() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)
        viewModel.removeSearchHistory("old_query")

        coVerify { searchHistoryRepository.removeSearchQuery("old_query") }
    }

    @Test
    fun clearSearchHistory_delegatesToRepository() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, searchHistoryRepository, savedStateHandle)
        viewModel.clearSearchHistory()

        coVerify { searchHistoryRepository.clearSearchHistory() }
    }
}
