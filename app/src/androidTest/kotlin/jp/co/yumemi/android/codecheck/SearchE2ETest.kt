package jp.co.yumemi.android.codecheck

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
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
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class SearchE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullUserJourney_search_clickItem_openDetails_andNavigateBack() {
        // 1. Initial screen visible
        Thread.sleep(1500)
        composeTestRule.onNode(hasContentDescription("GitHub Repository Search")).assertIsDisplayed()
        composeTestRule.onNodeWithText("Search GitHub Repositories").assertIsDisplayed()

        // 2. Type query into search field
        val inputField = composeTestRule.onNode(hasSetTextAction())
        inputField.assertIsDisplayed()
        inputField.performTextInput("kotlin")
        Thread.sleep(1500)

        // 3. Click search submit icon
        val searchButton = composeTestRule.onNodeWithContentDescription("Submit search")
        searchButton.assertIsDisplayed()
        searchButton.performClick()

        // 4. Wait for results to arrive from GitHub API (up to 15 seconds)
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule.onAllNodes(hasContentDescription("avatar", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }

        // 5. Pause so user visibly sees the repository cards appear on the simulator
        Thread.sleep(3000)

        // 6. Click the first repository card in the list
        composeTestRule.onAllNodes(hasContentDescription("avatar", substring = true))
            .onFirst()
            .performClick()

        // 7. Pause so user visibly sees the repository details screen
        Thread.sleep(3000)

        // 8. Verify "Repository Details" top bar is displayed
        composeTestRule.onNodeWithText("Repository Details").assertIsDisplayed()

        // 9. Click the back navigation button
        val backButton = composeTestRule.onNodeWithContentDescription("Navigate back")
        backButton.assertIsDisplayed()
        backButton.performClick()

        // 10. Pause so user visibly sees the return back to the search results
        Thread.sleep(2000)
        composeTestRule.onNode(hasContentDescription("GitHub Repository Search")).assertIsDisplayed()
        Thread.sleep(1500)
    }
}
