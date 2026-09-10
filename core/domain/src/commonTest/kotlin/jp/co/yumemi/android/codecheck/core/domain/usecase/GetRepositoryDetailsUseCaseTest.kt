package jp.co.yumemi.android.codecheck.core.domain.usecase

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetRepositoryDetailsUseCaseTest {

    private class FakeGitHubRepository : GitHubRepository {
        var lastOwner: String? = null
        var lastRepo: String? = null
        var shouldThrow: Throwable? = null
        var returnItem: RepositoryItem? = null

        override suspend fun searchRepositories(query: String): List<RepositoryItem> {
            throw UnsupportedOperationException()
        }

        override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem {
            lastOwner = owner
            lastRepo = repo
            shouldThrow?.let { throw it }
            return returnItem ?: throw NoSuchElementException("Not found")
        }
    }

    @Test
    fun invoke_withBlankOwnerOrRepo_throwsIllegalArgumentException() = runTest {
        val fakeRepo = FakeGitHubRepository()
        val useCase = GetRepositoryDetailsUseCase(fakeRepo)

        assertFailsWith<IllegalArgumentException> {
            useCase("", "repo")
        }
        assertFailsWith<IllegalArgumentException> {
            useCase("owner", "  ")
        }
    }

    @Test
    fun invoke_withValidParameters_trimsAndFetchesDetails() = runTest {
        val fakeRepo = FakeGitHubRepository()
        val expected = RepositoryItem(
            name = "android/nowinandroid",
            owner = Owner("https://example.com/icon.png")
        )
        fakeRepo.returnItem = expected
        val useCase = GetRepositoryDetailsUseCase(fakeRepo)

        val result = useCase(" android ", " nowinandroid ")

        assertEquals("android", fakeRepo.lastOwner)
        assertEquals("nowinandroid", fakeRepo.lastRepo)
        assertEquals(expected, result)
    }

    @Test
    fun invoke_whenRepositoryFails_propagatesException() = runTest {
        val fakeRepo = FakeGitHubRepository()
        fakeRepo.shouldThrow = RuntimeException("Connection error")
        val useCase = GetRepositoryDetailsUseCase(fakeRepo)

        assertFailsWith<RuntimeException> {
            useCase("android", "nowinandroid")
        }
    }
}
