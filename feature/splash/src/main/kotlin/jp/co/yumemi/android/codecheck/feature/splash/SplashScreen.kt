package jp.co.yumemi.android.codecheck.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.splash.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppNavy
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.DarkAppBackground
import jp.co.yumemi.android.codecheck.feature.splash.component.SplashBackground
import jp.co.yumemi.android.codecheck.feature.splash.component.SplashFooter
import jp.co.yumemi.android.codecheck.feature.splash.component.SplashLogoMark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Procedural geometric Splash Screen following the "Screen redesign system palette / 01 Splash"
 * specification (Light theme palette).
 *
 * Orchestrates entrance keyframes and continuous loops, delegating visual rendering to:
 * - [SplashBackground]: Ambient corner glow rings.
 * - [SplashLogoMark]: Magnifying glass lens, Git commit tree, pulse ring, and amber orbit.
 * - [SplashFooter]: Indeterminate progress bar and API label.
 *
 * @param onSplashFinished Callback invoked when the splash duration elapses.
 * @param modifier Modifier for root container.
 * @param splashDurationMs Duration in milliseconds before navigating away (default: 2000ms).
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier,
    splashDurationMs: Long = 2000L
) {
    // Retain latest callback reference safely without capturing stale or leaked outer scopes
    val currentOnSplashFinished by rememberUpdatedState(onSplashFinished)

    // ── Entrance Animation Drivers ──────────────────────────────────────────
    val ringRotation = remember { Animatable(-120f) }
    val ringScale = remember { Animatable(0.2f) }
    val handleScale = remember { Animatable(0f) }
    val spineScale = remember { Animatable(0f) }
    val nodeTopScale = remember { Animatable(0f) }
    val nodeLeftScale = remember { Animatable(0f) }
    val nodeBottomScale = remember { Animatable(0f) }
    val pulseScale = remember { Animatable(0.55f) }
    val pulseAlpha = remember { Animatable(0f) }
    val wordmarkAlpha = remember { Animatable(0f) }
    val wordmarkOffsetY = remember { Animatable(10f) }

    // ── Continuous Loop Animation Drivers ───────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "SplashInfinite")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbitAngle"
    )
    val progressOffset by infiniteTransition.animateFloat(
        initialValue = -40f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ProgressBarOffset"
    )

    // ── Entrance Sequence & Timeout Handoff ──────────────────────────────────
    LaunchedEffect(Unit) {
        // Phase 1 (0–600ms): Ring rotates from -120° with +8° overshoot and scales from 0.2 to 1.06
        launch {
            ringRotation.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 600
                    -120f at 0
                    8f at 380
                    0f at 600
                }
            )
        }
        launch {
            ringScale.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = 600
                    0.2f at 0
                    1.06f at 380
                    1.0f at 600
                }
            )
        }

        // Phase 2 (350–750ms): Handle extends outward at 45° with 1.08 overshoot
        launch {
            delay(350L)
            handleScale.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    1.08f at 240
                    1f at 400
                }
            )
        }

        // Phase 3 (500–1100ms): Git spines grow, then 3 nodes pop in 100ms apart with 1.35 overshoot
        launch {
            delay(500L)
            spineScale.animateTo(1f, animationSpec = tween(350, easing = FastOutSlowInEasing))
        }

        val nodePopSpec = keyframes<Float> {
            durationMillis = 350
            0f at 0
            1.35f at 200
            1f at 350
        }

        // Top node pops first (white)
        launch {
            delay(650L)
            nodeTopScale.animateTo(1f, animationSpec = nodePopSpec)
        }
        // Left branch node pops 100ms later (white)
        launch {
            delay(750L)
            nodeLeftScale.animateTo(1f, animationSpec = nodePopSpec)
        }
        // Bottom commit node pops 100ms later (green #2DB36C) - landing on the beat
        launch {
            delay(850L)
            nodeBottomScale.animateTo(1f, animationSpec = nodePopSpec)
        }

        // Phase 4 (900–1500ms): Pulse ring expands & Wordmark rises 10px into place
        launch {
            delay(900L)
            launch {
                pulseAlpha.animateTo(0.5f, animationSpec = tween(250))
                pulseAlpha.animateTo(0f, animationSpec = tween(450))
            }
            launch {
                pulseScale.animateTo(1.9f, animationSpec = tween(700, easing = FastOutSlowInEasing))
            }
        }
        launch {
            delay(950L)
            launch {
                wordmarkAlpha.animateTo(1f, animationSpec = tween(450, easing = FastOutSlowInEasing))
            }
            launch {
                wordmarkOffsetY.animateTo(0f, animationSpec = tween(450, easing = FastOutSlowInEasing))
            }
        }

        // Wait for configured duration (default: 2000ms), then trigger handoff callback
        delay(splashDurationMs)
        currentOnSplashFinished()
    }

    val splashBg = if (MaterialTheme.colorScheme.surface != AppWhite) DarkAppBackground else AppNavy

    // ── Root Canvas ─────────────────────────────────────────────────────────
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(splashBg)
            .testTag("SplashScreenRoot")
    ) {
        // Ambient background glow rings
        SplashBackground()

        // Center Construction: Animated Logo Mark + Wordmark
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SplashLogoMark(
                ringRotation = ringRotation.value,
                ringScale = ringScale.value,
                handleScale = handleScale.value,
                spineScale = spineScale.value,
                nodeTopScale = nodeTopScale.value,
                nodeLeftScale = nodeLeftScale.value,
                nodeBottomScale = nodeBottomScale.value,
                pulseScale = pulseScale.value,
                pulseAlpha = pulseAlpha.value,
                orbitAngle = orbitAngle
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Brand Typography
            Column(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = wordmarkAlpha.value
                        translationY = wordmarkOffsetY.value.dp.toPx()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.splash_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.01).sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.splash_subtitle),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFA9B4C4),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Bottom Footer (Progress bar + API label)
        SplashFooter(
            progressOffset = progressOffset,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp)
        )
    }
}

@Preview(name = "Splash Screen - Phone (412x892)", showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun SplashScreenPreview() {
    CodeCheckTheme {
        SplashScreen(onSplashFinished = {})
    }
}

@Preview(name = "Splash Screen - Tablet (800x1280)", showBackground = true, widthDp = 800, heightDp = 1280)
@Composable
private fun SplashScreenTabletPreview() {
    CodeCheckTheme {
        SplashScreen(onSplashFinished = {})
    }
}
