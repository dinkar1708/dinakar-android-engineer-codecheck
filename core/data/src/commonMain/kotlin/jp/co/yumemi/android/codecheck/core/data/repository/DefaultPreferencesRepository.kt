package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode
import jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Thread-safe preferences repository implementation with optional persistent change hooks.
 */
class DefaultPreferencesRepository(
    initialThemeMode: ThemeMode = ThemeMode.SYSTEM,
    initialLanguage: AppLanguagePreference = AppLanguagePreference.SYSTEM,
    private val onThemeModeChanged: ((ThemeMode) -> Unit)? = null,
    private val onLanguageChanged: ((AppLanguagePreference) -> Unit)? = null
) : PreferencesRepository {

    private val _themeMode = MutableStateFlow(initialThemeMode)
    private val _language = MutableStateFlow(initialLanguage)

    override fun getThemeMode(): Flow<ThemeMode> = _themeMode.asStateFlow()

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        _themeMode.value = themeMode
        onThemeModeChanged?.invoke(themeMode)
    }

    override fun getLanguagePreference(): Flow<AppLanguagePreference> = _language.asStateFlow()

    override suspend fun setLanguagePreference(language: AppLanguagePreference) {
        _language.value = language
        onLanguageChanged?.invoke(language)
    }
}
