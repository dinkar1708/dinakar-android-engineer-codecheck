package jp.co.yumemi.android.codecheck.feature.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.usecase.GetRepositoryDetailsUseCase
import jp.co.yumemi.android.codecheck.feature.detail.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase = mockk()

    private val fakeRepoItem = RepositoryItem(
        name = "android/compose-samples",
        owner = Owner(avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4"),
        language = "Kotlin",
        stargazersCount = 15000L,
        watchersCount = 15000L,
        forksCount = 3500L,
        openIssuesCount = 50L,
        description = "Official Jetpack Compose samples",
        htmlUrl = "https://github.com/android/compose-samples"
    )

    @Test
    fun initialUiState_whenSavedStateContainsOwnerAndRepo_loadsDetails() = runTest {
        coEvery { getRepositoryDetailsUseCase("android", "compose-samples") } returns fakeRepoItem

        val savedStateHandle = SavedStateHandle(mapOf("owner" to "android", "repo" to "compose-samples"))
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is DetailUiState.Success)
            assertEquals(fakeRepoItem, (item as DetailUiState.Success).repository)
        }

        coVerify(exactly = 1) { getRepositoryDetailsUseCase("android", "compose-samples") }
    }

    @Test
    fun initialUiState_whenNoSavedState_remainsLoading() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(DetailUiState.Loading, awaitItem())
        }

        coVerify(exactly = 0) { getRepositoryDetailsUseCase(any(), any()) }
    }

    @Test
    fun loadDetails_success_updatesStateAndSavedStateHandle() = runTest {
        coEvery { getRepositoryDetailsUseCase("JetBrains", "kotlin") } returns fakeRepoItem

        val savedStateHandle = SavedStateHandle()
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(DetailUiState.Loading, awaitItem())

            viewModel.loadDetails("JetBrains", "kotlin")

            assertEquals(DetailUiState.Success(fakeRepoItem), awaitItem())
        }

        assertEquals("JetBrains", savedStateHandle.get<String>("owner"))
        assertEquals("kotlin", savedStateHandle.get<String>("repo"))
        coVerify(exactly = 1) { getRepositoryDetailsUseCase("JetBrains", "kotlin") }
    }

    @Test
    fun loadDetails_error_emitsErrorState() = runTest {
        coEvery { getRepositoryDetailsUseCase("owner", "error_repo") } throws IOException("Repository not found")

        val savedStateHandle = SavedStateHandle()
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(DetailUiState.Loading, awaitItem())

            viewModel.loadDetails("owner", "error_repo")

            val state = awaitItem()
            assertTrue(state is DetailUiState.Error)
            assertEquals("Repository not found", (state as DetailUiState.Error).message)
        }
    }

    @Test
    fun setRepository_directlySetsSuccessState() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.setRepository(fakeRepoItem)

        assertEquals(DetailUiState.Success(fakeRepoItem), viewModel.uiState.value)
        coVerify(exactly = 0) { getRepositoryDetailsUseCase(any(), any()) }
    }

    @Test
    fun retry_whenOwnerAndRepoPresent_reloadsDetails() = runTest {
        coEvery { getRepositoryDetailsUseCase("android", "compose-samples") } returns fakeRepoItem

        val savedStateHandle = SavedStateHandle(mapOf("owner" to "android", "repo" to "compose-samples"))
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.retry()

        assertEquals(DetailUiState.Success(fakeRepoItem), viewModel.uiState.value)
        coVerify(exactly = 2) { getRepositoryDetailsUseCase("android", "compose-samples") }
    }

    @Test
    fun retry_whenOwnerOrRepoBlank_doesNothing() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = DetailViewModel(getRepositoryDetailsUseCase, savedStateHandle)

        viewModel.retry()

        assertEquals(DetailUiState.Loading, viewModel.uiState.value)
        coVerify(exactly = 0) { getRepositoryDetailsUseCase(any(), any()) }
    }
}
