package jp.co.yumemi.android.codecheck.feature.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Robolectric Compose UI unit tests for [SplashScreen].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_rendersBrandTextElements() {
        composeTestRule.setContent {
            SplashScreen(
                onSplashFinished = {}
            )
        }

        composeTestRule.onNodeWithTag("SplashScreenRoot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Repository Search").assertIsDisplayed()
        composeTestRule.onNodeWithText("Find any repo on GitHub").assertIsDisplayed()
        composeTestRule.onNodeWithText("GITHUB REST API V3").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "ja")
    fun splashScreen_japaneseLocale_displaysJapaneseStrings() {
        composeTestRule.setContent {
            SplashScreen(
                onSplashFinished = {}
            )
        }

        composeTestRule.onNodeWithTag("SplashScreenRoot").assertIsDisplayed()
        composeTestRule.onNodeWithText("リポジトリ検索").assertIsDisplayed()
        composeTestRule.onNodeWithText("GitHub 上のリポジトリを検索").assertIsDisplayed()
        composeTestRule.onNodeWithText("GITHUB REST API V3").assertIsDisplayed()
    }

    @Test
    fun splashScreen_invokesFinishedCallback_whenDurationElapsed() {
        var callbackInvoked = false

        composeTestRule.setContent {
            SplashScreen(
                onSplashFinished = { callbackInvoked = true },
                splashDurationMs = 100L
            )
        }

        // Wait for coroutine delay to complete
        composeTestRule.waitUntil(timeoutMillis = 2000L) {
            callbackInvoked
        }

        assertTrue("Callback should be invoked after duration", callbackInvoked)
    }

    @Test
    fun splashScreen_disposedBeforeTimeout_doesNotInvokeCallback() {
        var callbackInvoked = false
        var showSplash by androidx.compose.runtime.mutableStateOf(true)

        composeTestRule.setContent {
            if (showSplash) {
                SplashScreen(
                    onSplashFinished = { callbackInvoked = true },
                    splashDurationMs = 2000L
                )
            }
        }

        // Simulate leaving composition (e.g. user backpress / early navigation)
        showSplash = false
        composeTestRule.waitForIdle()

        org.junit.Assert.assertFalse("Callback must not be invoked after disposal", callbackInvoked)
    }
}
