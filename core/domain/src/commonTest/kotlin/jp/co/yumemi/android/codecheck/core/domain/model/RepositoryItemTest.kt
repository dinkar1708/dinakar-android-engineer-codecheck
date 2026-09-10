package jp.co.yumemi.android.codecheck.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RepositoryItemTest {

    @Test
    fun ownerIconUrl_delegatesToOwnerAvatarUrl() {
        val owner = Owner(avatarUrl = "https://example.com/avatar.png")
        val item = RepositoryItem(
            name = "kotlin",
            owner = owner
        )

        assertEquals("https://example.com/avatar.png", item.ownerIconUrl)
    }

    @Test
    fun defaultValues_areAppliedProperly() {
        val item = RepositoryItem(
            name = "repo",
            owner = Owner(avatarUrl = "https://example.com/avatar.png")
        )

        assertNull(item.language)
        assertEquals(0L, item.stargazersCount)
        assertEquals(0L, item.watchersCount)
        assertEquals(0L, item.forksCount)
        assertEquals(0L, item.openIssuesCount)
        assertNull(item.description)
        assertNull(item.htmlUrl)
    }

    @Test
    fun copy_updatesFieldWhilePreservingOthers() {
        val item = RepositoryItem(
            name = "original",
            owner = Owner("https://example.com/avatar.png"),
            stargazersCount = 10L
        )

        val updated = item.copy(name = "modified", stargazersCount = 20L)

        assertEquals("modified", updated.name)
        assertEquals(20L, updated.stargazersCount)
        assertEquals("https://example.com/avatar.png", updated.ownerIconUrl)
    }
}
