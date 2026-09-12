package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.network.error.NetworkException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MockGitHubRepositoryTest {

    private val repository = MockGitHubRepository(simulatedDelayMs = 0L)

    @Test
    fun searchRepositories_withMatchingQuery_returnsFilteredResults() = runTest {
        val result = repository.searchRepositories("kotlin")
        assertTrue(result.items.isNotEmpty())
        assertTrue(result.totalCount > 0)
        assertTrue(result.items.all { 
            it.name.contains("kotlin", ignoreCase = true) || 
                it.owner.login.contains("kotlin", ignoreCase = true) ||
                it.language?.contains("kotlin", ignoreCase = true) == true || 
                it.description?.contains("kotlin", ignoreCase = true) == true 
        })
    }

    @Test
    fun searchRepositories_withSortByStars_returnsDescendingStargazersCount() = runTest {
        val result = repository.searchRepositories(
            query = "kotlin",
            sort = jp.co.yumemi.android.codecheck.core.domain.model.SearchSort.STARS
        )
        assertTrue(result.items.isNotEmpty())
        val stars = result.items.map { it.stargazersCount }
        assertEquals(stars.sortedDescending(), stars)
    }

    @Test
    fun searchRepositories_withPagination_returnsSlices() = runTest {
        val page1 = repository.searchRepositories("a", page = 1)
        assertTrue(page1.items.isNotEmpty())
        assertEquals(10, page1.items.size)
        assertTrue(page1.hasNextPage)

        val page2 = repository.searchRepositories("a", page = 2)
        assertTrue(page2.items.isNotEmpty())
    }

    @Test
    fun searchRepositories_withLanguageFilter_filtersAccurately() = runTest {
        val result = repository.searchRepositories(
            query = "a",
            filter = jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter(language = "Rust")
        )
        assertTrue(result.items.isNotEmpty())
        assertTrue(result.items.all { it.language?.equals("Rust", ignoreCase = true) == true })
    }

    @Test
    fun searchRepositories_withMinStarsFilter_filtersAccurately() = runTest {
        val result = repository.searchRepositories(
            query = "a",
            filter = jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter(minStars = 50_000)
        )
        assertTrue(result.items.isNotEmpty())
        assertTrue(result.items.all { it.stargazersCount >= 50_000 })
    }

    @Test
    fun searchRepositories_withBlankQuery_returnsEmptyList() = runTest {
        val result = repository.searchRepositories("   ")
        assertTrue(result.items.isEmpty())
        assertEquals(0, result.totalCount)
        assertEquals(false, result.hasNextPage)
    }

    @Test
    fun searchRepositories_withEmptyKeyword_returnsEmptyList() = runTest {
        val result = repository.searchRepositories("empty")
        assertTrue(result.items.isEmpty())
        assertEquals(0, result.totalCount)
    }

    @Test
    fun searchRepositories_withErrorKeyword_throwsSimulatedException() = runTest {
        assertFailsWith<NetworkException.UnknownNetworkException> {
            repository.searchRepositories("error")
        }
    }

    @Test
    fun getRepositoryDetails_withExistingItem_returnsRepositoryItem() = runTest {
        val item = repository.getRepositoryDetails(owner = "google", repo = "android")
        assertNotNull(item)
        assertEquals("google/android", item.name)
        assertEquals("Kotlin", item.language)
    }

    @Test
    fun getRepositoryDetails_withNonExistingItem_returnsGeneratedFallbackItem() = runTest {
        val item = repository.getRepositoryDetails(owner = "custom-owner", repo = "custom-repo")
        assertNotNull(item)
        assertEquals("custom-repo", item.name)
        assertEquals("custom-owner", item.owner.login)
    }

    @Test
    fun dataModule_providesMockGitHubRepository_successfully() {
        val mockRepo = DataModule.provideMockGitHubRepository(simulatedDelayMs = 0L)
        assertNotNull(mockRepo)
        assertTrue(mockRepo is MockGitHubRepository)
    }
}
