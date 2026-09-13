package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.domain.repository.SearchHistoryRepository
import jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository
import javax.inject.Singleton

/**
 * Hilt module providing offline storage repositories across all build flavors.
 */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return DataModule.provideSearchHistoryRepository()
    }

    @Provides
    @Singleton
    fun provideStarredRepository(): StarredRepository {
        return DataModule.provideStarredRepository()
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(): jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository {
        return DataModule.providePreferencesRepository()
    }
}
