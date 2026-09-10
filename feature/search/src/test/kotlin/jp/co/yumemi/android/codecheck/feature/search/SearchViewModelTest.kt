package jp.co.yumemi.android.codecheck.feature.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase
import jp.co.yumemi.android.codecheck.feature.search.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchRepositoriesUseCase: SearchRepositoriesUseCase = mockk()

    private val fakeRepoItem = RepositoryItem(
        name = "android/compose-samples",
        owner = Owner(avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4"),
        language = "Kotlin",
        stargazersCount = 15000L,
        watchersCount = 15000L,
        forksCount = 3500L,
        openIssuesCount = 50L
    )

    @Test
    fun initialUiState_whenNoSavedState_isIdle() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())
        }
        assertEquals("", viewModel.query.value)
    }

    @Test
    fun initialUiState_whenSavedStateContainsQuery_triggersSearch() = runTest {
        coEvery { searchRepositoriesUseCase("kotlin") } returns listOf(fakeRepoItem)

        val savedStateHandle = SavedStateHandle(mapOf("last_search_query" to "kotlin"))
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is SearchUiState.Success)
            assertEquals(listOf(fakeRepoItem), (item as SearchUiState.Success).repositories)
        }
        assertEquals("kotlin", viewModel.query.value)
    }

    @Test
    fun onQueryChanged_updatesQueryAndSavedState() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.onQueryChanged("android")

        assertEquals("android", viewModel.query.value)
        assertEquals("android", savedStateHandle.get<String>("last_search_query"))
    }

    @Test
    fun searchRepositories_blankQuery_resetsToIdle() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.searchRepositories("   ")

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun searchRepositories_successNonEmpty_emitsSuccess() = runTest {
        coEvery { searchRepositoriesUseCase("android") } returns listOf(fakeRepoItem)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.searchRepositories("android")

            assertEquals(SearchUiState.Success(listOf(fakeRepoItem)), awaitItem())
        }

        coVerify(exactly = 1) { searchRepositoriesUseCase("android") }
    }

    @Test
    fun searchRepositories_successEmpty_emitsEmpty() = runTest {
        coEvery { searchRepositoriesUseCase("unknown_xyz") } returns emptyList()

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.searchRepositories("unknown_xyz")

            assertEquals(SearchUiState.Empty, awaitItem())
        }

        coVerify(exactly = 1) { searchRepositoriesUseCase("unknown_xyz") }
    }

    @Test
    fun searchRepositories_error_emitsError() = runTest {
        coEvery { searchRepositoriesUseCase("error_query") } throws IOException("Network timeout")

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Idle, awaitItem())

            viewModel.searchRepositories("error_query")

            val state = awaitItem()
            assertTrue(state is SearchUiState.Error)
            assertEquals("Network timeout", (state as SearchUiState.Error).message)
        }
    }

    @Test
    fun retry_whenQueryPresent_reExecutesSearch() = runTest {
        coEvery { searchRepositoriesUseCase("retry_query") } returns listOf(fakeRepoItem)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)
        viewModel.onQueryChanged("retry_query")

        viewModel.retry()

        assertEquals(SearchUiState.Success(listOf(fakeRepoItem)), viewModel.uiState.value)
        coVerify(exactly = 1) { searchRepositoriesUseCase("retry_query") }
    }

    @Test
    fun retry_whenQueryBlank_doesNothing() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)

        viewModel.retry()

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun clearQuery_resetsQueryAndStateToIdle() = runTest {
        coEvery { searchRepositoriesUseCase("query") } returns listOf(fakeRepoItem)

        val savedStateHandle = SavedStateHandle()
        val viewModel = SearchViewModel(searchRepositoriesUseCase, savedStateHandle)
        viewModel.onQueryChanged("query")
        viewModel.searchRepositories("query")

        viewModel.clearQuery()

        assertEquals("", viewModel.query.value)
        assertEquals("", savedStateHandle.get<String>("last_search_query"))
        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }
}
