package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.ui.util.getMonogramInitials
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [RepositoryCard] compose component.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class RepositoryCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeItem = RepositoryItem(
        name = "compose-samples",
        owner = Owner(avatarUrl = "https://example.com/avatar.png", login = "android"),
        language = "Kotlin",
        stargazersCount = 14200L,
        watchersCount = 14200L,
        forksCount = 3500L,
        openIssuesCount = 42L,
        description = "Official Jetpack Compose samples."
    )

    @Test
    fun repositoryCard_rendersRepositoryMetadata() {
        composeTestRule.setContent {
            RepositoryCard(
                item = fakeItem,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("compose-samples").assertIsDisplayed()
        composeTestRule.onNodeWithText("android").assertIsDisplayed()
        composeTestRule.onNodeWithText("Official Jetpack Compose samples.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kotlin").assertIsDisplayed()
        composeTestRule.onNodeWithText("14.2K").assertIsDisplayed()
        composeTestRule.onNodeWithText("3.5K forks").assertIsDisplayed()
    }

    @Test
    fun repositoryCard_click_triggersCallbackWithItem() {
        var clickedItem: RepositoryItem? = null

        composeTestRule.setContent {
            RepositoryCard(
                item = fakeItem,
                onClick = { clickedItem = it }
            )
        }

        composeTestRule.onNodeWithText("compose-samples").performClick()
        assertEquals(fakeItem, clickedItem)
    }

    @Test
    fun monogramInitials_computation() {
        assertEquals("DK", getMonogramInitials("dmtrKovalenko"))
        assertEquals("DA", getMonogramInitials("dylan-araps"))
        assertEquals("ME", getMonogramInitials("meekrosoft"))
        assertEquals("TN", getMonogramInitials("tom_nom_nom"))
        assertEquals("FF", getMonogramInitials("ffftp"))
    }
}
