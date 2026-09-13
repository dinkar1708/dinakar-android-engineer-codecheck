package jp.co.yumemi.android.codecheck.feature.splash.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppGreen
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.material3.MaterialTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.DarkActionBorder

/**
 * Procedural central geometric mark comprising:
 * 1. Expanding and fading pulse ring.
 * 2. Continuously orbiting amber satellite dot.
 * 3. Magnifying glass lens ring and 45° angled handle.
 * 4. Internal Git commit graph (trunk spine, branch spine, and 3 popping commit nodes).
 */
@Composable
fun SplashLogoMark(
    ringRotation: Float,
    ringScale: Float,
    handleScale: Float,
    spineScale: Float,
    nodeTopScale: Float,
    nodeLeftScale: Float,
    nodeBottomScale: Float,
    pulseScale: Float,
    pulseAlpha: Float,
    orbitAngle: Float,
    modifier: Modifier = Modifier,
    accentColor: Color = if (MaterialTheme.colorScheme.surface != AppWhite) DarkActionBorder else AppBlue
) {
    Box(
        modifier = modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        // Expanding Pulse Ring (150dp)
        Box(
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                    alpha = pulseAlpha
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = accentColor,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        // Amber Satellite Orbit Track (170dp diameter)
        Box(
            modifier = Modifier.size(170.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radiusPx = size.width / 2f
                val angleRad = Math.toRadians(orbitAngle.toDouble())
                val dotX = (radiusPx + radiusPx * cos(angleRad)).toFloat()
                val dotY = (radiusPx + radiusPx * sin(angleRad)).toFloat()

                drawCircle(
                    color = AppAmber,
                    radius = 3.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }
        }

        // Geometric Magnifying Glass & Git Commit Graph (132dp x 132dp)
        Canvas(
            modifier = Modifier.size(132.dp)
        ) {
            val lensCenter = Offset(52.dp.toPx(), 52.dp.toPx())
            val lensRadius = 46.5.dp.toPx()
            val strokeWidth = 11.dp.toPx()

            // 1. Lens Ring (Magnifying Glass)
            rotate(degrees = ringRotation, pivot = lensCenter) {
                drawCircle(
                    color = accentColor,
                    radius = lensRadius * ringScale,
                    center = lensCenter,
                    style = Stroke(width = strokeWidth * ringScale)
                )
            }

            // 2. Handle (extends at 45° from (84dp, 84dp))
            if (handleScale > 0f) {
                val handleOrigin = Offset(84.dp.toPx(), 84.dp.toPx())
                val handlePivot = Offset(84.dp.toPx(), 84.dp.toPx() + (strokeWidth / 2f))
                rotate(degrees = 45f, pivot = handlePivot) {
                    drawRoundRect(
                        color = accentColor,
                        topLeft = handleOrigin,
                        size = Size(52.dp.toPx() * handleScale, strokeWidth),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                }
            }

            // 3. Git Spines (Vertical trunk & horizontal branch)
            if (spineScale > 0f) {
                // Vertical trunk from (52dp, 28dp) down to (52dp, 64dp)
                val spineTop = Offset(52.dp.toPx(), 28.dp.toPx())
                val spineBottomY = 28.dp.toPx() + (36.dp.toPx() * spineScale)
                drawLine(
                    color = Color.White,
                    start = spineTop,
                    end = Offset(52.dp.toPx(), spineBottomY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Horizontal branch spine from (26dp, 58dp) across to (52dp, 58dp)
                val branchStartX = 52.dp.toPx() - (26.dp.toPx() * spineScale)
                drawLine(
                    color = Color.White,
                    start = Offset(branchStartX, 58.dp.toPx()),
                    end = Offset(52.dp.toPx(), 58.dp.toPx()),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 4. Git Nodes
            // Top Node (White)
            if (nodeTopScale > 0f) {
                drawCircle(
                    color = Color.White,
                    radius = 8.dp.toPx() * nodeTopScale,
                    center = Offset(52.dp.toPx(), 28.dp.toPx())
                )
            }

            // Left Branch Node (White)
            if (nodeLeftScale > 0f) {
                drawCircle(
                    color = Color.White,
                    radius = 8.dp.toPx() * nodeLeftScale,
                    center = Offset(26.dp.toPx(), 58.dp.toPx())
                )
            }

            // Bottom Commit Node (Green #2DB36C)
            if (nodeBottomScale > 0f) {
                drawCircle(
                    color = AppGreen,
                    radius = 8.dp.toPx() * nodeBottomScale,
                    center = Offset(52.dp.toPx(), 64.dp.toPx())
                )
            }
        }
    }
}

@Preview(name = "Splash Logo Mark", showBackground = true, backgroundColor = 0xFF2D3545)
@Composable
private fun SplashLogoMarkPreview() {
    CodeCheckTheme {
        SplashLogoMark(
            ringRotation = 0f,
            ringScale = 1f,
            handleScale = 1f,
            spineScale = 1f,
            nodeTopScale = 1f,
            nodeLeftScale = 1f,
            nodeBottomScale = 1f,
            pulseScale = 1f,
            pulseAlpha = 0.3f,
            orbitAngle = 45f
        )
    }
}
