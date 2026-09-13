package jp.co.yumemi.android.codecheck

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented End-to-End (E2E) integration test covering the complete user journey:
 * 1. Splash screen transition
 * 2. Search query and results list
 * 3. List item click to open Details screen
 * 4. Star / bookmark repository on Details screen
 * 5. Navigation back to Search
 * 6. Bottom navigation to Starred tab & verifying the saved repository
 * 7. Bottom navigation to Settings tab & toggling theme
 * 8. Return navigation to Search tab
 */
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class SearchE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullUserJourney_search_details_star_settings() {
        // 1. Wait for splash screen to complete and search input to appear
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()
        }
        val inputField = composeTestRule.onNode(hasSetTextAction())
        inputField.assertIsDisplayed()
        Thread.sleep(1500)

        // 2. Type query into search field
        inputField.performTextInput("kotlin")
        Thread.sleep(1000)

        // 3. Click search submit icon
        val searchButton = composeTestRule.onNodeWithContentDescription("Submit search")
        searchButton.assertIsDisplayed()
        searchButton.performClick()

        // 4. Wait for results to arrive from repository (up to 15 seconds)
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule.onAllNodes(hasContentDescription("avatar", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(3000)

        // 5. Click the first repository card in the list
        composeTestRule.onAllNodes(hasContentDescription("avatar", substring = true))
            .onFirst()
            .performClick()

        // 6. Verify repository detail screen is displayed
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasContentDescription("Navigate back"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        val backButton = composeTestRule.onNodeWithContentDescription("Navigate back")
        backButton.assertIsDisplayed()

        // 7. Wait for repository details to finish loading and star button to appear
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule.onAllNodes(
                hasContentDescription("Star repository") or hasContentDescription("Unstar repository")
            ).fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(2500)

        // Toggle Star
        val starNode = composeTestRule.onAllNodes(
            hasContentDescription("Star repository") or hasContentDescription("Unstar repository")
        ).onFirst()
        starNode.assertIsDisplayed()
        starNode.performClick()
        Thread.sleep(2500)

        // 8. Click back navigation button to return to search
        backButton.performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasContentDescription("Starred"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(2000)

        // 9. Navigate to Starred tab via bottom bar
        val starredTab = composeTestRule.onNodeWithContentDescription("Starred")
        starredTab.assertIsDisplayed()
        starredTab.performClick()

        // Verify the starred repository appears in the Starred screen list
        composeTestRule.waitUntil(timeoutMillis = 8000) {
            composeTestRule.onAllNodes(
                hasContentDescription("starred", substring = true) or hasText("REPOSITORY", substring = true)
            ).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodes(
            hasContentDescription("starred", substring = true) or hasText("REPOSITORY", substring = true)
        ).onFirst().assertIsDisplayed()
        Thread.sleep(3000)

        // 10. Navigate to Settings tab via bottom bar
        val settingsTab = composeTestRule.onNodeWithContentDescription("Settings")
        settingsTab.assertIsDisplayed()
        settingsTab.performClick()

        // Verify settings options are displayed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("Dark Mode", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(2500)

        val darkModeNode = composeTestRule.onAllNodes(hasText("Dark Mode", substring = true)).onFirst()
        darkModeNode.assertIsDisplayed()
        darkModeNode.performClick()
        Thread.sleep(3000)

        val lightModeNode = composeTestRule.onAllNodes(hasText("Light Mode", substring = true)).onFirst()
        lightModeNode.assertIsDisplayed()
        lightModeNode.performClick()
        Thread.sleep(3000)

        // 11. Navigate back to Search tab
        val searchTab = composeTestRule.onNodeWithContentDescription("Search")
        searchTab.assertIsDisplayed()
        searchTab.performClick()

        // Verify back on Search screen
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNode(hasSetTextAction()).assertIsDisplayed()
        Thread.sleep(2000)
    }
}

