package jp.co.yumemi.android.codecheck.core.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [LoadingView] declarative compose component.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class LoadingViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingView_withMessage_displaysMessage() {
        composeTestRule.setContent {
            LoadingView(message = "Searching GitHub repositories...")
        }

        composeTestRule.onNodeWithText("Searching GitHub repositories...").assertIsDisplayed()
    }

    @Test
    fun loadingView_withoutMessage_rendersWithoutCrash() {
        composeTestRule.setContent {
            LoadingView()
        }
    }
}
