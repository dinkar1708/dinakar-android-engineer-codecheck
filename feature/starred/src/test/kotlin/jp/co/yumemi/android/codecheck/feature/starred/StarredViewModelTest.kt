package jp.co.yumemi.android.codecheck.feature.starred

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository
import jp.co.yumemi.android.codecheck.feature.starred.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StarredViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val starredRepository: StarredRepository = mockk(relaxed = true)
    private val starredFlow = MutableStateFlow<List<RepositoryItem>>(emptyList())

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

    private fun createViewModel(): StarredViewModel {
        every { starredRepository.getStarredRepositories() } returns starredFlow
        return StarredViewModel(starredRepository)
    }

    @Test
    fun uiState_whenStarredEmpty_emitsEmptyState() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is StarredUiState.Empty)
        }
    }

    @Test
    fun uiState_whenStarredExist_emitsSuccessState() = runTest {
        starredFlow.value = listOf(fakeRepoItem)
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is StarredUiState.Success)
            assertEquals(listOf(fakeRepoItem), (item as StarredUiState.Success).repositories)
        }
    }

    @Test
    fun unstar_byId_invokesRepositoryUnstarById() = runTest {
        val viewModel = createViewModel()

        viewModel.unstar(fakeRepoItem)

        coVerify(exactly = 1) { starredRepository.unstarRepository(12345L) }
    }

    @Test
    fun unstar_byNameWhenIdZero_invokesRepositoryUnstarByName() = runTest {
        val viewModel = createViewModel()
        val itemWithoutId = fakeRepoItem.copy(id = 0L)

        viewModel.unstar(itemWithoutId)

        coVerify(exactly = 1) { starredRepository.unstarRepository("octocat/Hello-World") }
    }

    @Test
    fun clearAllStars_invokesRepositoryClearAllStars() = runTest {
        val viewModel = createViewModel()

        viewModel.clearAllStars()

        coVerify(exactly = 1) { starredRepository.clearAllStars() }
    }
}
