package jp.co.yumemi.android.codecheck.feature.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppNavy
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.SelectedBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate100
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate50
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate900
import jp.co.yumemi.android.codecheck.feature.settings.component.SettingsInfoTile
import jp.co.yumemi.android.codecheck.feature.settings.component.SettingsOptionTile

/**
 * Stateful entry point for the Settings & Preferences screen.
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsContent(
        uiState = uiState,
        onLanguageSelected = viewModel::setLanguage,
        onThemeSelected = viewModel::setThemeMode,
        modifier = modifier
    )
}

/**
 * Stateless content of the Settings & Preferences screen.
 */
@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    onThemeSelected: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Dark Navy Anchor Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppNavy)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppWhite
                )
            }

            // Scrollable Settings Sections
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // Section 1: Language Settings
                SettingsSectionHeader(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.language_section)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
                ) {
                    SettingsOptionTile(
                        title = stringResource(R.string.follow_system),
                        subtitle = stringResource(R.string.follow_system_language_subtitle),
                        selected = uiState.language == AppLanguage.SYSTEM,
                        onClick = { onLanguageSelected(AppLanguage.SYSTEM) }
                    )
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsOptionTile(
                        title = stringResource(R.string.english),
                        subtitle = "English",
                        selected = uiState.language == AppLanguage.EN,
                        onClick = { onLanguageSelected(AppLanguage.EN) }
                    )
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsOptionTile(
                        title = stringResource(R.string.japanese),
                        subtitle = "日本語",
                        selected = uiState.language == AppLanguage.JA,
                        onClick = { onLanguageSelected(AppLanguage.JA) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 2: Theme & Appearance
                SettingsSectionHeader(
                    icon = Icons.Default.Palette,
                    title = stringResource(R.string.theme_section)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
                ) {
                    SettingsOptionTile(
                        title = stringResource(R.string.follow_system),
                        subtitle = stringResource(R.string.follow_system_theme_subtitle),
                        selected = uiState.themeMode == AppThemeMode.SYSTEM,
                        onClick = { onThemeSelected(AppThemeMode.SYSTEM) }
                    )
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsOptionTile(
                        title = stringResource(R.string.theme_light),
                        subtitle = "Light mode appearance",
                        selected = uiState.themeMode == AppThemeMode.LIGHT,
                        onClick = { onThemeSelected(AppThemeMode.LIGHT) }
                    )
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsOptionTile(
                        title = stringResource(R.string.theme_dark),
                        subtitle = "Dark mode appearance",
                        selected = uiState.themeMode == AppThemeMode.DARK,
                        onClick = { onThemeSelected(AppThemeMode.DARK) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 3: Application Information
                SettingsSectionHeader(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.app_info_section)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SettingsInfoTile(
                        label = stringResource(R.string.app_version),
                        value = uiState.appVersion
                    )
                    SettingsInfoTile(
                        label = stringResource(R.string.app_environment),
                        value = uiState.environment
                    )
                    SettingsInfoTile(
                        label = stringResource(R.string.api_source),
                        value = uiState.apiSource
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(SelectedBlueBg, shape = RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = AppBlue
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(name = "Settings - Light", showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    CodeCheckTheme {
        SettingsContent(
            uiState = SettingsUiState(),
            onLanguageSelected = {},
            onThemeSelected = {}
        )
    }
}
