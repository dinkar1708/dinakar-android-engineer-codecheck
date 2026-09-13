package jp.co.yumemi.android.codecheck.feature.splash.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Robolectric unit tests for modular splash screen components.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SplashComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashFooter_rendersApiLabel() {
        composeTestRule.setContent {
            SplashFooter(progressOffset = 20f)
        }

        composeTestRule.onNodeWithText("GITHUB REST API V3").assertIsDisplayed()
    }

    @Test
    fun splashLogoMark_rendersWithoutCrash() {
        composeTestRule.setContent {
            SplashLogoMark(
                ringRotation = 0f,
                ringScale = 1f,
                handleScale = 1f,
                spineScale = 1f,
                nodeTopScale = 1f,
                nodeLeftScale = 1f,
                nodeBottomScale = 1f,
                pulseScale = 1f,
                pulseAlpha = 0.5f,
                orbitAngle = 90f
            )
        }
    }
}
