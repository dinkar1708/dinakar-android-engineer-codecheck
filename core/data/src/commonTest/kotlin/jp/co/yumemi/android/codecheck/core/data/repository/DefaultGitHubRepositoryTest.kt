package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.data.cache.InMemoryCache
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.network.GitHubApiService
import jp.co.yumemi.android.codecheck.core.network.error.NetworkException
import jp.co.yumemi.android.codecheck.core.network.model.OwnerDto
import jp.co.yumemi.android.codecheck.core.network.model.RepositoryItemDto
import jp.co.yumemi.android.codecheck.core.network.model.SearchResponseDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DefaultGitHubRepositoryTest {

    private class FakeGitHubApiService : GitHubApiService {
        var searchCallCount: Int = 0
        var detailCallCount: Int = 0
        var searchResponse: SearchResponseDto = SearchResponseDto(items = emptyList())
        var detailResponse: RepositoryItemDto? = null
        var throwOnSearch: Throwable? = null
        var throwOnDetail: Throwable? = null

        override suspend fun searchRepositories(
            query: String,
            page: Int,
            perPage: Int,
            sort: String?,
            order: String?
        ): SearchResponseDto {
            searchCallCount++
            throwOnSearch?.let { throw it }
            return searchResponse
        }

        override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItemDto {
            detailCallCount++
            throwOnDetail?.let { throw it }
            return detailResponse ?: throw NoSuchElementException()
        }
    }

    private fun createDto(
        name: String = "repo",
        fullName: String? = "owner/repo",
        avatarUrl: String = "https://example.com/avatar.png"
    ): RepositoryItemDto {
        return RepositoryItemDto(
            name = name,
            fullName = fullName,
            owner = OwnerDto(avatarUrl = avatarUrl),
            language = "Kotlin",
            stargazersCount = 100L,
            watchersCount = 50L,
            forksCount = 10L,
            openIssuesCount = 2L,
            description = "A sample repo",
            htmlUrl = "https://github.com/$fullName"
        )
    }

    @Test
    fun searchRepositories_delegatesToApi_andCachesResult() = runTest {
        val fakeApi = FakeGitHubApiService()
        val dto = createDto()
        fakeApi.searchResponse = SearchResponseDto(items = listOf(dto))

        val repository = DefaultGitHubRepository(
            apiService = fakeApi,
            searchCache = InMemoryCache()
        )

        // First search call
        val result1 = repository.searchRepositories("kotlin")
        assertEquals(1, fakeApi.searchCallCount)
        assertEquals(1, result1.items.size)
        assertEquals("owner/repo", result1.items[0].name)
        assertEquals("Kotlin", result1.items[0].language)

        // Second search call with same query (should hit in-memory cache)
        val result2 = repository.searchRepositories("kotlin")
        assertEquals(1, fakeApi.searchCallCount) // Call count remains 1
        assertEquals(result1, result2)
    }

    @Test
    fun searchRepositories_whenRateLimitExceeded_failsImmediatelyWithoutRetry() = runTest {
        val fakeApi = FakeGitHubApiService()
        fakeApi.throwOnSearch = NetworkException.RateLimitExceededException("Rate limit hit")

        val repository = DefaultGitHubRepository(
            apiService = fakeApi,
            searchCache = InMemoryCache()
        )

        assertFailsWith<NetworkException.RateLimitExceededException> {
            repository.searchRepositories("rate-limited")
        }
        // Unrecoverable error: should only attempt once
        assertEquals(1, fakeApi.searchCallCount)
    }

    @Test
    fun getRepositoryDetails_delegatesToApi_andCachesResult() = runTest {
        val fakeApi = FakeGitHubApiService()
        fakeApi.detailResponse = createDto(fullName = "octocat/Hello-World")

        val repository = DefaultGitHubRepository(
            apiService = fakeApi,
            detailCache = InMemoryCache()
        )

        // First detail call
        val result1 = repository.getRepositoryDetails("octocat", "Hello-World")
        assertEquals(1, fakeApi.detailCallCount)
        assertEquals("octocat/Hello-World", result1.name)

        // Second call with same owner/repo
        val result2 = repository.getRepositoryDetails("octocat", "Hello-World")
        assertEquals(1, fakeApi.detailCallCount) // Cached!
        assertEquals(result1, result2)
    }
}
