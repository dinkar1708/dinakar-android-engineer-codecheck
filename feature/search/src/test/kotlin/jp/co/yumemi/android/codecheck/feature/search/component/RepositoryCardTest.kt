package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.ui.util.getMonogramInitials
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
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

    @Test
    fun repositoryCard_stressItem_layoutResilience() {
        val stressItem = RepositoryItem(
            name = "extremely-long-repository-name-that-stresses-ui-layout-wrapping-and-overflow-resilience-without-truncation-or-breakage",
            owner = Owner(
                login = "super-verbose-organization-with-extremely-lengthy-username-account-name",
                avatarUrl = "https://avatars.githubusercontent.com/u/9919"
            ),
            language = "Visual Basic for Applications (.NET Framework Core Edition)",
            stargazersCount = 9_999_999_999L,
            watchersCount = 8_888_888_888L,
            forksCount = 7_777_777_777L,
            openIssuesCount = 6_666_666_666L,
            description = "This is an exceptionally verbose, extensive, and multi-paragraph repository description crafted specifically to test edge-case rendering, text wrapping, and UI overflow behavior in Jetpack Compose and View hierarchies. It ensures that cards expand appropriately, text does not clip unexpectedly, and typography scales gracefully under high font scale settings.",
            htmlUrl = "https://github.com/super-verbose-organization-with-extremely-lengthy-username-account-name/extremely-long-repository-name"
        )

        composeTestRule.setContent {
            androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.width(360.dp)) {
                RepositoryCard(
                    item = stressItem,
                    onClick = {}
                )
            }
        }

        val cardNode = composeTestRule.onNodeWithText("extremely-long-repository-name-that-s", substring = true, useUnmergedTree = true).fetchSemanticsNode()
        val descNode = composeTestRule.onNodeWithText("verbose, extensive", substring = true, useUnmergedTree = true).fetchSemanticsNode()
        val langNode = composeTestRule.onNodeWithText("Visual Basic", substring = true, useUnmergedTree = true).fetchSemanticsNode()
        val starNode = composeTestRule.onNodeWithText("10000.0M", useUnmergedTree = true).fetchSemanticsNode()

        println("CARD NODE BOUNDS: ${cardNode.boundsInRoot}")
        println("DESC NODE BOUNDS: ${descNode.boundsInRoot}")
        println("LANG NODE BOUNDS: ${langNode.boundsInRoot}")
        println("STAR NODE BOUNDS: ${starNode.boundsInRoot}")
    }
}
