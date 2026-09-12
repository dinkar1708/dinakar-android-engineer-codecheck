package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.domain.usecase.GetRepositoryDetailsUseCase
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase

/**
 * Hilt module providing domain use cases across all build flavors.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

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
