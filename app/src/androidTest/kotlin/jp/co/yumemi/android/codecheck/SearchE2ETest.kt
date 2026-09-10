package jp.co.yumemi.android.codecheck

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented End-to-End (E2E) test for the GitHub repository search user journey.
 * Executes live on an Android device or emulator.
 */
@RunWith(AndroidJUnit4::class)
class SearchE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun initialLaunch_displaysSearchScreenWithEmptyState() {
        // Allow user to visibly view initial screen on emulator
        Thread.sleep(2000)

        // Verify TopAppBar
        composeTestRule.onNodeWithText("GitHub Repository Search").assertIsDisplayed()

        // Verify Search input field
        composeTestRule.onNodeWithText("Search repositories...").assertIsDisplayed()

        // Verify initial empty state guidance
        composeTestRule.onNodeWithText("Search GitHub Repositories").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type a search query above and press Enter.").assertIsDisplayed()

        Thread.sleep(1000)
    }

    @Test
    fun typeSearchQuery_updatesInputAndAllowsClearing() {
        Thread.sleep(1500)

        val searchPlaceholder = composeTestRule.onNodeWithText("Search repositories...")
        searchPlaceholder.assertIsDisplayed()

        // Type query visibly on simulator
        searchPlaceholder.performTextInput("kotlin")
        composeTestRule.onNodeWithText("kotlin").assertIsDisplayed()

        // Pause so user sees typed text and clear button
        Thread.sleep(2000)

        // Clear button should be visible
        val clearButton = composeTestRule.onNodeWithContentDescription("Clear search")
        clearButton.assertIsDisplayed()
        clearButton.performClick()

        // Query cleared, returns to placeholder
        composeTestRule.onNodeWithText("Search repositories...").assertIsDisplayed()

        Thread.sleep(2000)
    }
}
