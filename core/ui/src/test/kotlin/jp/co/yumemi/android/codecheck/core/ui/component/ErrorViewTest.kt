package jp.co.yumemi.android.codecheck.core.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [ErrorView] declarative compose component.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ErrorViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorView_displaysMessageAndRetryButton() {
        composeTestRule.setContent {
            ErrorView(
                message = "Network connection lost. Please try again.",
                onRetry = {},
                retryLabel = "Retry"
            )
        }

        composeTestRule.onNodeWithText("Network connection lost. Please try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun errorView_retryButtonClick_triggersCallback() {
        var retryTriggered = false
        composeTestRule.setContent {
            ErrorView(
                message = "Rate limit exceeded",
                onRetry = { retryTriggered = true },
                retryLabel = "Try Again"
            )
        }

        composeTestRule.onNodeWithText("Try Again").performClick()
        assertTrue(retryTriggered)
    }
}
