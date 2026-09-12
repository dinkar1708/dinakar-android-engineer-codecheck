package jp.co.yumemi.android.codecheck.feature.splash.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme

/**
 * Ambient background concentric circles drawn in the corners of the splash canvas
 * matching the 01 Splash specification.
 */
@Composable
fun SplashBackground(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val subtleBorderColor = Color(0x0DFFFFFF)
        // Top-left ambient circle: 620px diameter (310dp radius)
        drawCircle(
            color = subtleBorderColor,
            radius = 310.dp.toPx(),
            center = Offset(x = -65.dp.toPx(), y = -95.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )
        // Bottom-right ambient circle: 460px diameter (230dp radius)
        drawCircle(
            color = subtleBorderColor,
            radius = 230.dp.toPx(),
            center = Offset(x = size.width + 80.dp.toPx(), y = size.height + 75.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Preview(name = "Splash Background", showBackground = true, backgroundColor = 0xFF2D3545, widthDp = 412, heightDp = 892)
@Composable
private fun SplashBackgroundPreview() {
    CodeCheckTheme {
        SplashBackground()
    }
}
