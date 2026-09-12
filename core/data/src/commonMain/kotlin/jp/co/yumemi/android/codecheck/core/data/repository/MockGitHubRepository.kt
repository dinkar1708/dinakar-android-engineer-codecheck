package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.network.error.NetworkException
import kotlinx.coroutines.delay

/**
 * 100% Offline Mock Repository providing realistic GitHub datasets,
 * layout stress-testing scenarios, and zero rate-limit development.
 *
 * Designed for KMP multiplatform consumption (Android, iOS SwiftUI companion, and unit tests).
 */
class MockGitHubRepository(
    private val simulatedDelayMs: Long = 300L,
    customDataset: List<RepositoryItem>? = null
) : GitHubRepository {

    val mockData: List<RepositoryItem> = customDataset ?: defaultMockData

    override suspend fun searchRepositories(query: String): List<RepositoryItem> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return emptyList()
        }

        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }

        if (trimmed.equals("error", ignoreCase = true)) {
            throw NetworkException.UnknownNetworkException("Simulated mock network failure for testing")
        }

        if (trimmed.equals("empty", ignoreCase = true)) {
            return emptyList()
        }

        return mockData.filter { item ->
            item.name.contains(trimmed, ignoreCase = true) ||
                item.owner.login.contains(trimmed, ignoreCase = true) ||
                (item.description?.contains(trimmed, ignoreCase = true) == true) ||
                (item.language?.contains(trimmed, ignoreCase = true) == true)
        }
    }

    override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItem {
        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }

        val match = mockData.firstOrNull { item ->
            (item.name.equals(repo, ignoreCase = true) && item.owner.login.equals(owner, ignoreCase = true)) ||
                item.name.equals("$owner/$repo", ignoreCase = true) ||
                (item.name.contains("/") && item.name.substringAfter("/").equals(repo, ignoreCase = true))
        }

        return match ?: RepositoryItem(
            name = repo,
            owner = Owner(login = owner, avatarUrl = "https://avatars.githubusercontent.com/u/9919?s=200&v=4"),
            language = "Kotlin",
            stargazersCount = 1_200L,
            watchersCount = 1_200L,
            forksCount = 340L,
            openIssuesCount = 12L,
            description = "Mock repository for $owner/$repo",
            htmlUrl = "https://github.com/$owner/$repo"
        )
    }

    companion object {
        val defaultMockData: List<RepositoryItem> = listOf(
            RepositoryItem(
                name = "google/android",
                owner = Owner(login = "google", avatarUrl = "https://avatars.githubusercontent.com/u/1342004"),
                language = "Kotlin",
                stargazersCount = 185_400L,
                watchersCount = 185_400L,
                forksCount = 45_200L,
                openIssuesCount = 142L,
                description = "Android Open Source Project official source and tools repository.",
                htmlUrl = "https://github.com/google/android"
            ),
            RepositoryItem(
                name = "jetbrains/kotlin",
                owner = Owner(login = "jetbrains", avatarUrl = "https://avatars.githubusercontent.com/u/878437"),
                language = "Kotlin",
                stargazersCount = 48_500L,
                watchersCount = 48_500L,
                forksCount = 5_650L,
                openIssuesCount = 89L,
                description = "The Kotlin Programming Language. Concise, safe, interoperable, and fun.",
                htmlUrl = "https://github.com/jetbrains/kotlin"
            ),
            RepositoryItem(
                name = "android/architecture-samples",
                owner = Owner(login = "android", avatarUrl = "https://avatars.githubusercontent.com/u/32689599"),
                language = "Kotlin",
                stargazersCount = 44_200L,
                watchersCount = 44_200L,
                forksCount = 11_900L,
                openIssuesCount = 35L,
                description = "A collection of samples to showcase architectural tools and patterns for Android apps.",
                htmlUrl = "https://github.com/android/architecture-samples"
            ),
            RepositoryItem(
                name = "android/nowinandroid",
                owner = Owner(login = "android", avatarUrl = "https://avatars.githubusercontent.com/u/32689599"),
                language = "Kotlin",
                stargazersCount = 17_800L,
                watchersCount = 17_800L,
                forksCount = 3_400L,
                openIssuesCount = 67L,
                description = "A fully functional Android app built entirely with Kotlin and Jetpack Compose.",
                htmlUrl = "https://github.com/android/nowinandroid"
            ),
            RepositoryItem(
                name = "JetBrains/compose-multiplatform",
                owner = Owner(login = "JetBrains", avatarUrl = "https://avatars.githubusercontent.com/u/878437"),
                language = "Kotlin",
                stargazersCount = 16_200L,
                watchersCount = 16_200L,
                forksCount = 1_150L,
                openIssuesCount = 412L,
                description = "Compose Multiplatform, a modern UI framework for Kotlin targeting Android, iOS, Desktop, and Web.",
                htmlUrl = "https://github.com/JetBrains/compose-multiplatform"
            ),
            RepositoryItem(
                name = "dmtrKovalenko/fff",
                owner = Owner(login = "dmtrKovalenko", avatarUrl = ""),
                language = "Rust",
                stargazersCount = 10_695L,
                watchersCount = 10_695L,
                forksCount = 444L,
                openIssuesCount = 87L,
                description = "Fastest file finder, built for terminal workflows. MIT licensed.",
                htmlUrl = "https://github.com/dmtrKovalenko/fff"
            ),
            RepositoryItem(
                name = "ravidsrk/kotlinextensions.com",
                owner = Owner(login = "ravidsrk", avatarUrl = "https://avatars.githubusercontent.com/u/1010328"),
                language = "Kotlin",
                stargazersCount = 589L,
                watchersCount = 589L,
                forksCount = 96L,
                openIssuesCount = 0L,
                description = "A handy collection of most commonly used Kotlin extensions to boost your productivity.",
                htmlUrl = "https://github.com/ravidsrk/kotlinextensions.com"
            ),
            RepositoryItem(
                name = "square/retrofit",
                owner = Owner(login = "square", avatarUrl = "https://avatars.githubusercontent.com/u/82592"),
                language = "Java",
                stargazersCount = 42_800L,
                watchersCount = 42_800L,
                forksCount = 7_300L,
                openIssuesCount = 120L,
                description = "A type-safe HTTP client for Android and the JVM.",
                htmlUrl = "https://github.com/square/retrofit"
            ),
            RepositoryItem(
                name = "square/okhttp",
                owner = Owner(login = "square", avatarUrl = "https://avatars.githubusercontent.com/u/82592"),
                language = "Kotlin",
                stargazersCount = 45_100L,
                watchersCount = 45_100L,
                forksCount = 9_100L,
                openIssuesCount = 198L,
                description = "Square’s meticulous HTTP client for the JVM, Android, and GraalVM.",
                htmlUrl = "https://github.com/square/okhttp"
            ),
            RepositoryItem(
                name = "cashapp/turbine",
                owner = Owner(login = "cashapp", avatarUrl = "https://avatars.githubusercontent.com/u/82592"),
                language = "Kotlin",
                stargazersCount = 5_200L,
                watchersCount = 5_200L,
                forksCount = 210L,
                openIssuesCount = 15L,
                description = "A small testing library for kotlinx.coroutines Flow.",
                htmlUrl = "https://github.com/cashapp/turbine"
            ),
            RepositoryItem(
                name = "rust-lang/rust",
                owner = Owner(login = "rust-lang", avatarUrl = "https://avatars.githubusercontent.com/u/5430905"),
                language = "Rust",
                stargazersCount = 98_700L,
                watchersCount = 98_700L,
                forksCount = 12_400L,
                openIssuesCount = 9_500L,
                description = "Empowering everyone to build reliable and efficient software.",
                htmlUrl = "https://github.com/rust-lang/rust"
            ),
            RepositoryItem(
                name = "golang/go",
                owner = Owner(login = "golang", avatarUrl = "https://avatars.githubusercontent.com/u/4314092"),
                language = "Go",
                stargazersCount = 122_000L,
                watchersCount = 122_000L,
                forksCount = 17_800L,
                openIssuesCount = 8_900L,
                description = "The Go programming language open source project.",
                htmlUrl = "https://github.com/golang/go"
            )
        )
    }
}
