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
 * UI tests for [EmptyView] declarative compose component.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class EmptyViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyView_displaysTitleAndDescription() {
        composeTestRule.setContent {
            EmptyView(
                title = "No Repositories Found",
                description = "Try different keywords."
            )
        }

        composeTestRule.onNodeWithText("No Repositories Found").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try different keywords.").assertIsDisplayed()
    }

    @Test
    fun emptyView_withoutDescription_onlyDisplaysTitle() {
        composeTestRule.setContent {
            EmptyView(
                title = "Search GitHub Repositories"
            )
        }

        composeTestRule.onNodeWithText("Search GitHub Repositories").assertIsDisplayed()
    }
}
