package jp.co.yumemi.android.codecheck.core.domain.usecase

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchResult
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort

class SearchRepositoriesUseCaseTest {

    private class FakeGitHubRepository : GitHubRepository {
        var lastQuery: String? = null
        var lastPage: Int = 1
        var lastSort: SearchSort = SearchSort.BEST_MATCH
        var lastFilter: SearchFilter = SearchFilter()
        var callCount: Int = 0
        var shouldThrow: Throwable? = null
        var returnResult: SearchResult = SearchResult()

        override suspend fun searchRepositories(
            query: String,
            page: Int,
            sort: SearchSort,
            filter: SearchFilter
        ): SearchResult {
            callCount++
            lastQuery = query
            lastPage = page
            lastSort = sort
            lastFilter = filter
            shouldThrow?.let { throw it }
            return returnResult
        }

        override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem {
            throw UnsupportedOperationException()
        }
    }

    @Test
    fun invoke_withBlankQuery_returnsEmptySearchResultWithoutCallingRepository() = runTest {
        val fakeRepo = FakeGitHubRepository()
        val useCase = SearchRepositoriesUseCase(fakeRepo)

        val resultEmpty = useCase("")
        val resultWhitespace = useCase("   ")

        assertEquals(SearchResult(emptyList(), 0, false), resultEmpty)
        assertEquals(SearchResult(emptyList(), 0, false), resultWhitespace)
        assertEquals(0, fakeRepo.callCount)
    }

    @Test
    fun invoke_withValidQuery_trimsAndDelegatesToRepository() = runTest {
        val fakeRepo = FakeGitHubRepository()
        val expectedItem = RepositoryItem(
            name = "jetbrains/kotlin",
            owner = Owner("https://avatars.githubusercontent.com/u/878437")
        )
        fakeRepo.returnResult = SearchResult(listOf(expectedItem), 1, false)
        val useCase = SearchRepositoriesUseCase(fakeRepo)

        val result = useCase(
            query = "  kotlin  ",
            page = 2,
            sort = SearchSort.STARS,
            filter = SearchFilter(language = "Kotlin")
        )

        assertEquals(1, fakeRepo.callCount)
        assertEquals("kotlin", fakeRepo.lastQuery)
        assertEquals(2, fakeRepo.lastPage)
        assertEquals(SearchSort.STARS, fakeRepo.lastSort)
        assertEquals("Kotlin", fakeRepo.lastFilter.language)
        assertEquals(listOf(expectedItem), result.items)
        assertEquals(1, result.totalCount)
    }

    @Test
    fun invoke_whenRepositoryFails_propagatesException() = runTest {
        val fakeRepo = FakeGitHubRepository()
        fakeRepo.shouldThrow = IllegalStateException("Network failure")
        val useCase = SearchRepositoriesUseCase(fakeRepo)

        assertFailsWith<IllegalStateException> {
            useCase("kotlin")
        }
    }
}
