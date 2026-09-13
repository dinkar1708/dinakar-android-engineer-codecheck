package jp.co.yumemi.android.codecheck.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Unified ColorScheme using the definitive 6-color palette (Common_Palette.dc.html)
private val AppColorScheme = lightColorScheme(
    primary = AppBlue,
    onPrimary = AppWhite,
    primaryContainer = Color(0xFFEEF1FD),
    onPrimaryContainer = AppBlue,
    secondary = AppGreen,
    onSecondary = AppWhite,
    secondaryContainer = MonogramGreenBg,
    onSecondaryContainer = AppGreen,
    tertiary = AppAmber,
    onTertiary = AppWhite,
    tertiaryContainer = Color(0xFFFFFBEB),
    onTertiaryContainer = Color(0xFFB45309),
    background = Slate50,
    onBackground = Slate900,
    surface = AppWhite,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Color(0xFF5B6879),
    outline = Slate200,
    outlineVariant = Slate100,
    error = Color(0xFFDC2626),
    onError = AppWhite
)

private val DarkAppColorScheme = darkColorScheme(
    primary = Color(0xFF8F9DF5),
    onPrimary = Color(0xFF1F2634),
    primaryContainer = Color(0xFF2A3350),
    onPrimaryContainer = Color(0xFF8F9DF5),
    secondary = AppGreen,
    onSecondary = AppWhite,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF1F2634),
    tertiaryContainer = Color(0xFF2E2716),
    onTertiaryContainer = Color(0xFFFBBF24),
    background = Color(0xFF12161F),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1F2634),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF2B3344),
    onSurfaceVariant = Color(0xFF8C99AB),
    outline = Color(0xFF333C4E),
    outlineVariant = Color(0xFF2B3344),
    error = Color(0xFFEF4444),
    onError = AppWhite
)

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
    darkTheme: Boolean = isSystemInDarkTheme(),
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkAppColorScheme else AppColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
