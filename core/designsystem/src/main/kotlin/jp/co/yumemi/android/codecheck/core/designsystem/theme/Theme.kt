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
    primary = Purple200,
    onPrimary = Color.Black,
    primaryContainer = Purple700,
    onPrimaryContainer = Color.White,
    secondary = Teal200,
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    primaryContainer = Purple700,
    onPrimaryContainer = Color.White,
    secondary = Teal700,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,
    error = ErrorRed,
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
    dynamicColor: Boolean = true,
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
