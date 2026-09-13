package jp.co.yumemi.android.codecheck.feature.settings

import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode

typealias AppThemeMode = ThemeMode
typealias AppLanguage = AppLanguagePreference

/**
 * Immutable UI State for Settings & Preferences Screen.
 */
data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguagePreference = AppLanguagePreference.SYSTEM,
    val appVersion: String = "1.0",
    val environment: String = "DEVELOPMENT",
    val apiSource: String = "GitHub REST API v3"
)
