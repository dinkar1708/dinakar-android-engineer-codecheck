package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.domain.usecase.GetRepositoryDetailsUseCase
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import javax.inject.Singleton

/**
 * Hilt module providing repository and domain use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        apiService: GitHubApiService
    ): GitHubRepository {
        return DataModule.provideGitHubRepository(apiService = apiService)
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
