package jp.co.yumemi.android.codecheck.core.domain.usecase

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SearchRepositoriesUseCaseTest {

    private class FakeGitHubRepository : GitHubRepository {
        var lastQuery: String? = null
        var callCount: Int = 0
        var shouldThrow: Throwable? = null
        var returnItems: List<RepositoryItem> = emptyList()

        override suspend fun searchRepositories(query: String): List<RepositoryItem> {
            callCount++
            lastQuery = query
            shouldThrow?.let { throw it }
            return returnItems
        }

        override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem {
            throw UnsupportedOperationException()
        }
    }

    @Test
    fun invoke_withBlankQuery_returnsEmptyListWithoutCallingRepository() = runTest {
        val fakeRepo = FakeGitHubRepository()
        val useCase = SearchRepositoriesUseCase(fakeRepo)

        val resultEmpty = useCase("")
        val resultWhitespace = useCase("   ")

        assertEquals(emptyList(), resultEmpty)
        assertEquals(emptyList(), resultWhitespace)
        assertEquals(0, fakeRepo.callCount)
    }

    @Test
    fun invoke_withValidQuery_trimsAndDelegatesToRepository() = runTest {
        val fakeRepo = FakeGitHubRepository()
        val expectedItem = RepositoryItem(
            name = "jetbrains/kotlin",
            owner = Owner("https://avatars.githubusercontent.com/u/878437")
        )
        fakeRepo.returnItems = listOf(expectedItem)
        val useCase = SearchRepositoriesUseCase(fakeRepo)

        val result = useCase("  kotlin  ")

        assertEquals(1, fakeRepo.callCount)
        assertEquals("kotlin", fakeRepo.lastQuery)
        assertEquals(listOf(expectedItem), result)
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
