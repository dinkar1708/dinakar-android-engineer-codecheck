package jp.co.yumemi.android.codecheck.feature.settings

/**
 * Supported Theme appearance modes.
 */
enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

/**
 * Supported UI language preferences.
 */
enum class AppLanguage {
    SYSTEM,
    EN,
    JA
}

/**
 * Immutable UI State for Settings & Preferences Screen.
 */
data class SettingsUiState(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.SYSTEM,
    val appVersion: String = "1.0.0 (Build 1)",
    val environment: String = "PRODUCTION",
    val apiSource: String = "GitHub REST API v3"
)
