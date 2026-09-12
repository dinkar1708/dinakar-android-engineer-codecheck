package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import javax.inject.Singleton

/**
 * Mock flavor Hilt module providing a 100% offline [GitHubRepository].
 * Bound at compile time without any runtime flavor checks.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        @Suppress("UNUSED_PARAMETER") apiService: GitHubApiService
    ): GitHubRepository {
        return DataModule.provideMockGitHubRepository(simulatedDelayMs = 300L)
    }
}
