package jp.co.yumemi.android.codecheck.navigation

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [extractOwnerAndRepo] navigation helper function.
 */
class ExtractOwnerAndRepoTest {

    private fun createItem(name: String, htmlUrl: String? = null): RepositoryItem {
        return RepositoryItem(
            name = name,
            owner = Owner(avatarUrl = "https://example.com/avatar.png"),
            stargazersCount = 100L,
            watchersCount = 100L,
            forksCount = 50L,
            openIssuesCount = 5L,
            htmlUrl = htmlUrl
        )
    }

    @Test
    fun extractOwnerAndRepo_withValidGitHubUrl_extractsOwnerAndRepo() {
        val item = createItem(
            name = "dagger",
            htmlUrl = "https://github.com/google/dagger"
        )
        val (owner, repo) = extractOwnerAndRepo(item)
        assertEquals("google", owner)
        assertEquals("dagger", repo)
    }

    @Test
    fun extractOwnerAndRepo_withTrailingSlashInUrl_extractsCleanOwnerAndRepo() {
        val item = createItem(
            name = "architecture-samples",
            htmlUrl = "https://github.com/android/architecture-samples/"
        )
        val (owner, repo) = extractOwnerAndRepo(item)
        assertEquals("android", owner)
        assertEquals("architecture-samples", repo)
    }

    @Test
    fun extractOwnerAndRepo_withoutHtmlUrl_fallbackToSlashSeparatedName() {
        val item = createItem(
            name = "square/retrofit",
            htmlUrl = null
        )
        val (owner, repo) = extractOwnerAndRepo(item)
        assertEquals("square", owner)
        assertEquals("retrofit", repo)
    }

    @Test
    fun extractOwnerAndRepo_withoutHtmlUrlAndSingleName_fallbackToRepositoryPrefix() {
        val item = createItem(
            name = "single-name-repo",
            htmlUrl = null
        )
        val (owner, repo) = extractOwnerAndRepo(item)
        assertEquals("repository", owner)
        assertEquals("single-name-repo", repo)
    }

    @Test
    fun extractOwnerAndRepo_withNonGitHubUrl_fallbackToSlashName() {
        val item = createItem(
            name = "jetbrains/kotlin",
            htmlUrl = "https://gitlab.com/other/path"
        )
        val (owner, repo) = extractOwnerAndRepo(item)
        assertEquals("jetbrains", owner)
        assertEquals("kotlin", repo)
    }
}
