package jp.co.yumemi.android.codecheck.di

import io.mockk.mockk
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Unit tests for Hilt dependency injection modules in the app shell.
 */
class DependencyInjectionTest {

    @Test
    fun networkModule_providesGitHubApiService() {
        val service = NetworkModule.provideGitHubApiService()
        assertNotNull(service)
    }

    @Test
    fun repositoryModule_providesGitHubRepository() {
        val fakeApiService = mockk<GitHubApiService>(relaxed = true)
        val repo = RepositoryModule.provideGitHubRepository(fakeApiService)
        assertNotNull(repo)
    }

    @Test
    fun useCaseModule_providesUseCases() {
        val fakeRepo = mockk<GitHubRepository>(relaxed = true)
        val searchUseCase = UseCaseModule.provideSearchRepositoriesUseCase(fakeRepo)
        val detailsUseCase = UseCaseModule.provideGetRepositoryDetailsUseCase(fakeRepo)

        assertNotNull(searchUseCase)
        assertNotNull(detailsUseCase)
    }

    @Test
    fun storageModule_providesStorageRepositories() {
        val searchHistoryRepo = StorageModule.provideSearchHistoryRepository()
        val starredRepo = StorageModule.provideStarredRepository()

        assertNotNull(searchHistoryRepo)
        assertNotNull(starredRepo)
    }
}

