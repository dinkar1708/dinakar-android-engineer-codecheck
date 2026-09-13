package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.BuildConfig
import jp.co.yumemi.android.codecheck.core.domain.model.AppBuildInfo
import javax.inject.Singleton

/**
 * Hilt module providing application-level build configuration and metadata.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppBuildInfo(): AppBuildInfo {
        return AppBuildInfo(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            flavor = BuildConfig.FLAVOR,
            isDebug = BuildConfig.DEBUG
        )
    }
}
