package jp.co.yumemi.android.codecheck.core.domain.repository

import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

/**
 * Interface managing user preferences including theme appearance and application language.
 */
interface PreferencesRepository {
    /**
     * Observes current user theme preference.
     */
    fun getThemeMode(): Flow<ThemeMode>

    /**
     * Updates theme preference.
     */
    suspend fun setThemeMode(themeMode: ThemeMode)

    /**
     * Observes current user language preference.
     */
    fun getLanguagePreference(): Flow<AppLanguagePreference>

    /**
     * Updates language preference.
     */
    suspend fun setLanguagePreference(language: AppLanguagePreference)
}
