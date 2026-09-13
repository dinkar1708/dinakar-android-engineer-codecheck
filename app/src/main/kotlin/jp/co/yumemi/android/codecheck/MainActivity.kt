package jp.co.yumemi.android.codecheck

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.feature.settings.AppLanguage
import jp.co.yumemi.android.codecheck.feature.settings.AppThemeMode
import jp.co.yumemi.android.codecheck.feature.settings.SettingsViewModel
import jp.co.yumemi.android.codecheck.navigation.AppNavHost
import java.util.Locale

/**
 * Single-Activity application shell hosting Compose Navigation and Hilt injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            val isDarkTheme = when (settingsState.themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }

            val targetLocale = when (settingsState.language) {
                AppLanguage.SYSTEM -> null
                AppLanguage.EN -> Locale.ENGLISH
                AppLanguage.JA -> Locale.JAPANESE
            }

            val baseContext = LocalContext.current
            val currentConfig = LocalConfiguration.current

            val localizedContext = remember(targetLocale, baseContext) {
                if (targetLocale == null) {
                    baseContext
                } else {
                    val config = Configuration(baseContext.resources.configuration).apply {
                        setLocale(targetLocale)
                    }
                    baseContext.createConfigurationContext(config)
                }
            }

            val localizedConfiguration = remember(targetLocale, currentConfig) {
                if (targetLocale == null) {
                    currentConfig
                } else {
                    Configuration(currentConfig).apply {
                        setLocale(targetLocale)
                    }
                }
            }

            CompositionLocalProvider(
                LocalConfiguration provides localizedConfiguration,
                LocalContext provides localizedContext
            ) {
                CodeCheckTheme(darkTheme = isDarkTheme) {
                    AppNavHost(
                        settingsViewModel = settingsViewModel,
                        onOpenBrowser = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
