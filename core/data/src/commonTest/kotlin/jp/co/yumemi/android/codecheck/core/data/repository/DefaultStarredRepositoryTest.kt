package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultStarredRepositoryTest {

    private val sampleRepo1 = RepositoryItem(
        id = 101L,
        name = "compose-samples",
        owner = Owner(avatarUrl = "https://example.com/avatar.png", login = "android"),
        stargazersCount = 5000L
    )

    private val sampleRepo2 = RepositoryItem(
        id = 102L,
        name = "kotlin",
        owner = Owner(avatarUrl = "https://example.com/kotlin.png", login = "JetBrains"),
        stargazersCount = 45000L
    )

    @Test
    fun getStarredRepositories_initiallyEmpty() = runTest {
        val repository = DefaultStarredRepository()
        val starred = repository.getStarredRepositories().first()
        assertTrue(starred.isEmpty())
    }

    @Test
    fun starRepository_addsItemAndUpdatesIsStarred() = runTest {
        val repository = DefaultStarredRepository()

        repository.starRepository(sampleRepo1)

        val starred = repository.getStarredRepositories().first()
        assertEquals(1, starred.size)
        assertEquals(sampleRepo1, starred.first())
        assertTrue(repository.isStarred("compose-samples").first())
        assertFalse(repository.isStarred("kotlin").first())
    }

    @Test
    fun unstarRepository_removesItemAndUpdatesIsStarred() = runTest {
        val repository = DefaultStarredRepository()

        repository.starRepository(sampleRepo1)
        repository.starRepository(sampleRepo2)
        repository.unstarRepository("compose-samples")

        val starred = repository.getStarredRepositories().first()
        assertEquals(listOf(sampleRepo2), starred)
        assertFalse(repository.isStarred("compose-samples").first())
        assertTrue(repository.isStarred("kotlin").first())
    }

    @Test
    fun clearAllStars_clearsAllItems() = runTest {
        val repository = DefaultStarredRepository()

        repository.starRepository(sampleRepo1)
        repository.starRepository(sampleRepo2)
        repository.clearAllStars()

        val starred = repository.getStarredRepositories().first()
        assertTrue(starred.isEmpty())
        assertFalse(repository.isStarred("compose-samples").first())
    }

    @Test
    fun isStarredById_checksSetOfIds() = runTest {
        val repository = DefaultStarredRepository()

        repository.starRepository(sampleRepo1)

        assertTrue(repository.isStarred(101L).first())
        assertFalse(repository.isStarred(102L).first())
        assertTrue(repository.getStarredIds().first().contains(101L))
    }

    @Test
    fun unstarRepositoryById_removesItemAndId() = runTest {
        val repository = DefaultStarredRepository()

        repository.starRepository(sampleRepo1)
        repository.starRepository(sampleRepo2)
        repository.unstarRepository(101L)

        val starred = repository.getStarredRepositories().first()
        assertEquals(listOf(sampleRepo2), starred)
        assertFalse(repository.isStarred(101L).first())
        assertTrue(repository.isStarred(102L).first())
    }

    @Test
    fun toggleStar_addsWhenAbsent_removesWhenPresent() = runTest {
        val repository = DefaultStarredRepository()

        repository.toggleStar(sampleRepo1)
        assertTrue(repository.isStarred(101L).first())
        assertEquals(1, repository.getStarredRepositories().first().size)

        repository.toggleStar(sampleRepo1)
        assertFalse(repository.isStarred(101L).first())
        assertTrue(repository.getStarredRepositories().first().isEmpty())
    }
}
