package jp.co.yumemi.android.codecheck.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [DetailScreen] across Loading, Error, and Success states.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeItem = RepositoryItem(
        name = "compose-samples",
        owner = Owner(avatarUrl = "https://example.com/avatar.png"),
        language = "Kotlin",
        stargazersCount = 100L,
        watchersCount = 100L,
        forksCount = 50L,
        openIssuesCount = 5L
    )

    @Test
    fun detailScreen_loadingState_displaysLoadingView() {
        composeTestRule.setContent {
            DetailScreen(
                uiState = DetailUiState.Loading,
                onBackClick = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Repository").assertIsDisplayed()
        composeTestRule.onNodeWithText("Loading repository details...").assertIsDisplayed()
    }

    @Test
    fun detailScreen_errorState_displaysErrorAndTriggersRetry() {
        var retried = false

        composeTestRule.setContent {
            DetailScreen(
                uiState = DetailUiState.Error("Failed to fetch repository"),
                onBackClick = {},
                onRetry = { retried = true }
            )
        }

        composeTestRule.onNodeWithText("Failed to fetch repository").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed().performClick()
        assertTrue(retried)
    }

    @Test
    fun detailScreen_successState_displaysContentAndHandlesBackClick() {
        var backClicked = false

        composeTestRule.setContent {
            DetailScreen(
                uiState = DetailUiState.Success(fakeItem),
                onBackClick = { backClicked = true },
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Repository").assertIsDisplayed()
        composeTestRule.onNodeWithText("compose-samples").assertIsDisplayed()

        val backButton = composeTestRule.onNodeWithContentDescription("Navigate back")
        backButton.assertIsDisplayed().performClick()
        assertTrue(backClicked)
    }

    @Test
    @Config(qualifiers = "ja")
    fun detailScreen_japaneseLocale_displaysJapaneseStrings() {
        composeTestRule.setContent {
            DetailScreen(
                uiState = DetailUiState.Loading,
                onBackClick = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("リポジトリ詳細").assertIsDisplayed()
        composeTestRule.onNodeWithText("リポジトリ詳細を読み込み中…").assertIsDisplayed()
    }
}
