package jp.co.yumemi.android.codecheck.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit UI tests for [DetailContent] composable using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DetailContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeItem = RepositoryItem(
        name = "compose-samples",
        owner = Owner(avatarUrl = "https://example.com/avatar.png"),
        language = "Kotlin",
        stargazersCount = 14200L,
        watchersCount = 12500L,
        forksCount = 3500L,
        openIssuesCount = 42L,
        description = "Official Jetpack Compose samples",
        htmlUrl = "https://github.com/android/compose-samples"
    )

    @Test
    fun detailContent_rendersRepositoryDetailsAndMetrics() {
        composeTestRule.setContent {
            DetailContent(
                repository = fakeItem,
                onOpenBrowser = {}
            )
        }

        composeTestRule.onNodeWithText("compose-samples").assertIsDisplayed()
        composeTestRule.onNodeWithText("Official Jetpack Compose samples").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Kotlin").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("STARS").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("14,200").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("WATCHERS").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("12,500").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("FORKS").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("3,500").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("OPEN ISSUES").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("42").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun detailContent_withHtmlUrl_rendersViewOnGithubButtonAndTriggersCallback() {
        var openedUrl: String? = null

        composeTestRule.setContent {
            DetailContent(
                repository = fakeItem,
                onOpenBrowser = { openedUrl = it }
            )
        }

        val button = composeTestRule.onNodeWithText("View on GitHub")
        button.performScrollTo().assertIsDisplayed()
        button.performClick()

        assertEquals("https://github.com/android/compose-samples", openedUrl)
    }

    @Test
    fun detailContent_withoutLanguageOrBrowserCallback_doesNotShowOptionalElements() {
        val minimalItem = RepositoryItem(
            name = "minimal-repo",
            owner = Owner(avatarUrl = "https://example.com/avatar.png"),
            language = null,
            stargazersCount = 0L,
            watchersCount = 0L,
            forksCount = 0L,
            openIssuesCount = 0L,
            htmlUrl = null
        )

        composeTestRule.setContent {
            DetailContent(
                repository = minimalItem,
                onOpenBrowser = null
            )
        }

        composeTestRule.onNodeWithText("minimal-repo").assertIsDisplayed()
        composeTestRule.onNodeWithText("View on GitHub").assertDoesNotExist()
    }

    @Test
    fun detailContent_rendersMetadataRows() {
        val detailedItem = fakeItem.copy(
            defaultBranch = "main",
            pushedAt = "2026-09-02T14:32:00Z",
            license = "Apache-2.0",
            size = 4300L
        )

        composeTestRule.setContent {
            DetailContent(
                repository = detailedItem,
                onOpenBrowser = {}
            )
        }

        composeTestRule.onNodeWithText("Default branch").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("main").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithText("Last push").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("2026-09-02").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithText("License").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Apache-2.0").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithText("Size").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("4.2 MB").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun detailContent_withDownloadButton_triggersZipDownloadCallback() {
        var openedUrl: String? = null
        val detailedItem = fakeItem.copy(
            defaultBranch = "develop"
        )

        composeTestRule.setContent {
            DetailContent(
                repository = detailedItem,
                onOpenBrowser = { openedUrl = it }
            )
        }

        val downloadButton = composeTestRule.onNodeWithContentDescription("Download")
        downloadButton.performScrollTo().assertIsDisplayed()
        downloadButton.performClick()

        assertEquals("https://github.com/android/compose-samples/archive/refs/heads/develop.zip", openedUrl)
    }

    @Test
    fun detailContent_withNullMetadata_rendersFallbackValues() {
        val itemWithFallbacks = fakeItem.copy(
            defaultBranch = null,
            pushedAt = null,
            license = null,
            size = 0L
        )

        composeTestRule.setContent {
            DetailContent(
                repository = itemWithFallbacks,
                onOpenBrowser = {}
            )
        }

        composeTestRule.onNodeWithText("Default branch").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("N/A").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithText("Last push").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("—").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithText("License").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("None").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithText("Size").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("0 KB").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun detailContent_withCompositeRepoName_displaysExtractedOwnerAndRepoName() {
        val compositeItem = RepositoryItem(
            name = "ravidsrk/kotlinextensions.com",
            owner = Owner(avatarUrl = "https://example.com/avatar.png", login = "ravidsrk")
        )

        composeTestRule.setContent {
            DetailContent(
                repository = compositeItem,
                onOpenBrowser = null
            )
        }

        composeTestRule.onNodeWithText("ravidsrk").assertIsDisplayed()
        composeTestRule.onNodeWithText("kotlinextensions.com").assertIsDisplayed()
    }
}
