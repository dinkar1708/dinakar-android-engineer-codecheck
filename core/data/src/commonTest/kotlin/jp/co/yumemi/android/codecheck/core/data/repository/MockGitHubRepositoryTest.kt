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
        val results = repository.searchRepositories("kotlin")
        assertTrue(results.isNotEmpty())
        assertTrue(results.all { it.name.contains("kotlin", ignoreCase = true) || it.language == "Kotlin" || it.description?.contains("kotlin", ignoreCase = true) == true })
    }

    @Test
    fun searchRepositories_withBlankQuery_returnsEmptyList() = runTest {
        val results = repository.searchRepositories("   ")
        assertTrue(results.isEmpty())
    }

    @Test
    fun searchRepositories_withEmptyKeyword_returnsEmptyList() = runTest {
        val results = repository.searchRepositories("empty")
        assertTrue(results.isEmpty())
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
