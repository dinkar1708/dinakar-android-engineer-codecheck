package jp.co.yumemi.android.codecheck.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Unified ColorScheme using the definitive 6-color palette
// (Light and dark theme use the same color palette for now; distinct dark mode will be activated later)
private val AppColorScheme = lightColorScheme(
    primary = AppBlue,
    onPrimary = AppWhite,
    primaryContainer = MonogramBlueBg,
    onPrimaryContainer = AppBlue,
    secondary = AppGreen,
    onSecondary = AppWhite,
    secondaryContainer = MonogramGreenBg,
    onSecondaryContainer = AppGreen,
    tertiary = AppAmber,
    onTertiary = AppWhite,
    tertiaryContainer = AppAmber.copy(alpha = 0.12f),
    onTertiaryContainer = Slate900,
    background = Slate50,
    onBackground = Slate900,
    surface = AppWhite,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,
    outline = Slate200,
    outlineVariant = Slate300,
    error = Color(0xFFDC2626),
    onError = AppWhite
)

// For now, both modes share AppColorScheme per design requirements
private val DarkAppColorScheme = AppColorScheme

/**
 * Global Material 3 theme for Yumemi Android Engineer CodeCheck.
 * Implements the 6-color system (Navy, Blue, Green, Amber, Slate ramp, White).
 *
 * @param darkTheme Whether dark color scheme should be used.
 * @param dynamicColor Whether dynamic color should be enabled. Defaults to false.
 * @param content Composable tree to be styled.
 */
@Composable
fun CodeCheckTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Both light and dark modes share AppColorScheme for now; dark mode will be activated in a future update
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
