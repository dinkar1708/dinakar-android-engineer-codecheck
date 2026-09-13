package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import jp.co.yumemi.android.codecheck.core.network.GitHubApiServiceImpl
import javax.inject.Singleton

/**
 * Hilt module providing network layer dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGitHubApiService(): GitHubApiService {
        return GitHubApiServiceImpl()
    }
}
