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

private val DarkColorScheme = darkColorScheme(
    primary = Primary200,
    onPrimary = Color(0xFF1E1B4B), // Deep indigo for contrast
    primaryContainer = Primary600,
    onPrimaryContainer = Primary200,
    secondary = Secondary200,
    onSecondary = Color(0xFF0C4A6E), // Deep cyan for contrast
    secondaryContainer = Secondary600,
    onSecondaryContainer = Secondary200,
    tertiary = Tertiary300,
    onTertiary = Color(0xFF4C1D95), // Deep purple for contrast
    tertiaryContainer = Color(0xFF5B21B6),
    onTertiaryContainer = Tertiary300,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark,
    error = ErrorDark,
    onError = Color(0xFF7F1D1D)
)

private val LightColorScheme = lightColorScheme(
    primary = Primary500,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEEF2FF), // Indigo 50
    onPrimaryContainer = Primary700,
    secondary = Secondary500,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFECFEFF), // Cyan 50
    onSecondaryContainer = Secondary600,
    tertiary = Tertiary500,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF5F3FF), // Purple 50
    onTertiaryContainer = Color(0xFF5B21B6), // Purple 800
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    error = ErrorLight,
    onError = Color.White
)

/**
 * Global Material 3 theme for Yumemi Android Engineer CodeCheck.
 *
 * @param darkTheme Whether dark color scheme should be used. Defaults to system setting.
 * @param dynamicColor Whether dynamic color (Android 12+) should be enabled. Defaults to true.
 * @param content Composable tree to be styled.
 */
@Composable
fun CodeCheckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled to use our custom pure white theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
