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
 * Executes on an Android device or emulator to verify full Activity lifecycle,
 * Compose navigation, and UI interactions.
 */
@RunWith(AndroidJUnit4::class)
class SearchE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun initialLaunch_displaysSearchScreenWithEmptyState() {
        // Verify TopAppBar
        composeTestRule.onNodeWithText("GitHub Repository Search").assertIsDisplayed()

        // Verify Search input field
        composeTestRule.onNodeWithText("Search repositories...").assertIsDisplayed()

        // Verify initial empty state guidance
        composeTestRule.onNodeWithText("Search GitHub Repositories").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type a search query above and press Enter.").assertIsDisplayed()
    }

    @Test
    fun typeSearchQuery_updatesInputAndAllowsClearing() {
        val searchPlaceholder = composeTestRule.onNodeWithText("Search repositories...")
        searchPlaceholder.assertIsDisplayed()

        // Input a search query
        searchPlaceholder.performTextInput("kotlin")
        composeTestRule.onNodeWithText("kotlin").assertIsDisplayed()

        // Clear button should be visible when text is present
        val clearButton = composeTestRule.onNodeWithContentDescription("Clear search")
        clearButton.assertIsDisplayed()
        clearButton.performClick()

        // Query cleared, returns to placeholder
        composeTestRule.onNodeWithText("Search repositories...").assertIsDisplayed()
    }
}
