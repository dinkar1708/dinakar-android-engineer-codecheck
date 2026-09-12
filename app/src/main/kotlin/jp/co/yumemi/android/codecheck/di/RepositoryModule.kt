package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.BuildConfig
import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.domain.usecase.GetRepositoryDetailsUseCase
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import javax.inject.Singleton

/**
 * Hilt module providing repository and domain use cases.
 * Dynamically switches between production network repository and 100% offline mock repository
 * based on the active product flavor ([BuildConfig.FLAVOR_MODE]).
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        apiService: GitHubApiService
    ): GitHubRepository {
        return if (BuildConfig.FLAVOR_MODE == "mock") {
            DataModule.provideMockGitHubRepository(simulatedDelayMs = 300L)
        } else {
            DataModule.provideGitHubRepository(apiService = apiService)
        }
    }

    @Provides
    fun provideSearchRepositoriesUseCase(
        repository: GitHubRepository
    ): SearchRepositoriesUseCase {
        return SearchRepositoriesUseCase(repository = repository)
    }

    @Provides
    fun provideGetRepositoryDetailsUseCase(
        repository: GitHubRepository
    ): GetRepositoryDetailsUseCase {
        return GetRepositoryDetailsUseCase(repository = repository)
    }
}
