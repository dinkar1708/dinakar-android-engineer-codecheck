package jp.co.yumemi.android.codecheck.core.data.di

import jp.co.yumemi.android.codecheck.core.data.repository.DefaultGitHubRepository
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import jp.co.yumemi.android.codecheck.core.network.GitHubApiServiceImpl

/**
 * Dependency factory providing data layer implementations.
 * Can be leveraged directly by KMP consumers (iOS / testing) or bound via Hilt in `:app`.
 */
object DataModule {

    /**
     * Provide a singleton or instance of [GitHubRepository].
     */
    fun provideGitHubRepository(
        apiService: GitHubApiService = GitHubApiServiceImpl()
    ): GitHubRepository {
        return DefaultGitHubRepository(apiService = apiService)
    }

    /**
     * Provide a 100% offline [MockGitHubRepository].
     */
    fun provideMockGitHubRepository(
        simulatedDelayMs: Long = 300L
    ): GitHubRepository {
        return jp.co.yumemi.android.codecheck.core.data.repository.MockGitHubRepository(
            simulatedDelayMs = simulatedDelayMs
        )
    }
}
