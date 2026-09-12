package jp.co.yumemi.android.codecheck.feature.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [SearchScreen] composable across all UI states using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeRepo = RepositoryItem(
        name = "compose-samples",
        owner = Owner(avatarUrl = "https://example.com/avatar.png", login = "android"),
        language = "Kotlin",
        stargazersCount = 45000L,
        watchersCount = 45000L,
        forksCount = 5500L,
        openIssuesCount = 120L
    )

    @Test
    fun searchScreen_idleState_displaysGuidance() {
        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Idle,
                query = "",
                onQueryChanged = {},
                onSearch = {},
                onClearQuery = {},
                onRetry = {},
                onRepositoryClick = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("GitHub Repository Search").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search GitHub Repositories").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type a search query above and press Enter.").assertIsDisplayed()
    }

    @Test
    fun searchScreen_loadingState_rendersWithoutCrash() {
        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Loading,
                query = "android",
                onQueryChanged = {},
                onSearch = {},
                onClearQuery = {},
                onRetry = {},
                onRepositoryClick = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("GitHub Repository Search").assertIsDisplayed()
    }

    @Test
    fun searchScreen_emptyState_displaysNoResultsMessage() {
        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Empty,
                query = "nonexistent-xyz",
                onQueryChanged = {},
                onSearch = {},
                onClearQuery = {},
                onRetry = {},
                onRepositoryClick = {}
            )
        }

        composeTestRule.onNodeWithText("No repositories found").assertIsDisplayed()
        composeTestRule.onNodeWithText("No results for “nonexistent-xyz”. Check the spelling or search a shorter term.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear search").assertIsDisplayed()
    }

    @Test
    fun searchScreen_errorState_displaysErrorMessageAndTriggersRetry() {
        var retried = false

        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Error("Network failure"),
                query = "kotlin",
                onQueryChanged = {},
                onSearch = {},
                onClearQuery = {},
                onRetry = { retried = true },
                onRepositoryClick = {}
            )
        }

        composeTestRule.onNodeWithText("Network failure").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed().performClick()
        assertTrue(retried)
    }

    @Test
    fun searchScreen_successState_displaysRepositoriesAndHandlesClick() {
        var clickedItem: RepositoryItem? = null

        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Success(listOf(fakeRepo)),
                query = "kotlin",
                onQueryChanged = {},
                onSearch = {},
                onClearQuery = {},
                onRetry = {},
                onRepositoryClick = { clickedItem = it }
            )
        }

        // Verify repository item
        composeTestRule.onNodeWithText("compose-samples").assertIsDisplayed().performClick()
        assertEquals(fakeRepo, clickedItem)
    }

    @Test
    fun searchScreen_queryField_typingAndClearing() {
        var cleared = false

        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Idle,
                query = "jetpack",
                onQueryChanged = {},
                onSearch = {},
                onClearQuery = { cleared = true },
                onRetry = {},
                onRepositoryClick = {}
            )
        }

        val clearButton = composeTestRule.onNodeWithContentDescription("Clear search")
        clearButton.assertIsDisplayed().performClick()
        assertTrue(cleared)
    }

    @Test
    fun searchScreen_successState_displaysSortTabsAndHandlesClick() {
        var selectedSort: jp.co.yumemi.android.codecheck.core.domain.model.SearchSort? = null

        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Success(listOf(fakeRepo)),
                query = "kotlin",
                selectedSort = jp.co.yumemi.android.codecheck.core.domain.model.SearchSort.BEST_MATCH,
                onQueryChanged = {},
                onSearch = {},
                onSortSelected = { selectedSort = it },
                onClearQuery = {},
                onRetry = {},
                onRepositoryClick = {}
            )
        }

        composeTestRule.onNodeWithText("Best match").assertIsDisplayed()
        composeTestRule.onNodeWithText("Most stars").assertIsDisplayed().performClick()
        assertEquals(jp.co.yumemi.android.codecheck.core.domain.model.SearchSort.STARS, selectedSort)
    }

    @Test
    fun searchScreen_successState_displaysResultsCountAndLoadMore() {
        var loadMoreClicked = false

        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState.Success(
                    repositories = listOf(fakeRepo),
                    totalCount = 42,
                    hasNextPage = true
                ),
                query = "kotlin",
                onQueryChanged = {},
                onSearch = {},
                onLoadNextPage = { loadMoreClicked = true },
                onClearQuery = {},
                onRetry = {},
                onRepositoryClick = {}
            )
        }

        composeTestRule.onNodeWithText("1–1 OF 42").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear all").assertIsDisplayed()
        composeTestRule.onNodeWithText("Load more").assertIsDisplayed().performClick()
        assertTrue(loadMoreClicked)
    }
}
