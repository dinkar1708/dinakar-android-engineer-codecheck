package jp.co.yumemi.android.codecheck.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.data.repository.DefaultPreferencesRepository
import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode
import jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository
import jp.co.yumemi.android.codecheck.core.domain.repository.SearchHistoryRepository
import jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository
import javax.inject.Singleton

/**
 * Hilt module providing offline storage repositories across all build flavors.
 */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_THEME_MODE = "pref_theme_mode"
    private const val KEY_LANGUAGE = "pref_language"

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
    fun providePreferencesRepository(
        @ApplicationContext context: Context
    ): PreferencesRepository {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val initialTheme = try {
            prefs.getString(KEY_THEME_MODE, null)?.let { ThemeMode.valueOf(it) } ?: ThemeMode.SYSTEM
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
        val initialLanguage = try {
            prefs.getString(KEY_LANGUAGE, null)?.let { AppLanguagePreference.valueOf(it) } ?: AppLanguagePreference.SYSTEM
        } catch (_: Exception) {
            AppLanguagePreference.SYSTEM
        }

        return DefaultPreferencesRepository(
            initialThemeMode = initialTheme,
            initialLanguage = initialLanguage,
            onThemeModeChanged = { mode ->
                prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
            },
            onLanguageChanged = { lang ->
                prefs.edit().putString(KEY_LANGUAGE, lang.name).apply()
            }
        )
    }

    /**
     * Fallback for unit testing environments without Android Context.
     */
    fun providePreferencesRepository(): PreferencesRepository {
        return DataModule.providePreferencesRepository()
    }
}
