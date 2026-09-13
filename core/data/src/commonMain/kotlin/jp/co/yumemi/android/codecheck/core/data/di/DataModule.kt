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

    /**
     * Provide a singleton or instance of [jp.co.yumemi.android.codecheck.core.domain.repository.SearchHistoryRepository].
     */
    fun provideSearchHistoryRepository(
        maxItems: Int = 7
    ): jp.co.yumemi.android.codecheck.core.domain.repository.SearchHistoryRepository {
        return jp.co.yumemi.android.codecheck.core.data.repository.DefaultSearchHistoryRepository(maxItems = maxItems)
    }

    /**
     * Provide a singleton or instance of [jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository].
     */
    fun provideStarredRepository(): jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository {
        return jp.co.yumemi.android.codecheck.core.data.repository.DefaultStarredRepository()
    }

    /**
     * Provide a singleton or instance of [jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository].
     */
    fun providePreferencesRepository(): jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository {
        return jp.co.yumemi.android.codecheck.core.data.repository.DefaultPreferencesRepository()
    }
}
