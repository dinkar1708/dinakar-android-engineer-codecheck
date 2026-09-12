package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchResult
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
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

    val mockData: List<RepositoryItem> = (customDataset ?: defaultMockData).mapIndexed { index, item ->
        if (item.updatedAt == null) {
            val generatedDate = when (index % 3) {
                0 -> "2026-09-${(10 + (index % 3)).toString().padStart(2, '0')}T12:00:00Z"
                1 -> "2026-0${1 + (index % 8)}-15T10:00:00Z"
                else -> "2025-11-20T08:00:00Z"
            }
            item.copy(updatedAt = generatedDate)
        } else {
            item
        }
    }

    override suspend fun searchRepositories(
        query: String,
        page: Int,
        sort: SearchSort,
        filter: SearchFilter
    ): SearchResult {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return SearchResult(emptyList(), 0, false)
        }

        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }

        if (trimmed.equals("error", ignoreCase = true)) {
            throw NetworkException.UnknownNetworkException("Simulated mock network failure for testing")
        }

        if (trimmed.equals("empty", ignoreCase = true)) {
            return SearchResult(emptyList(), 0, false)
        }

        // 1. Text filter
        var filtered = mockData.filter { item ->
            item.name.contains(trimmed, ignoreCase = true) ||
                item.owner.login.contains(trimmed, ignoreCase = true) ||
                (item.description?.contains(trimmed, ignoreCase = true) == true) ||
                (item.language?.contains(trimmed, ignoreCase = true) == true)
        }

        // 2. Language filter
        if (!filter.language.isNullOrBlank()) {
            filtered = filtered.filter { item ->
                item.language?.equals(filter.language, ignoreCase = true) == true
            }
        }

        // 3. Minimum stars filter
        val minStars = filter.minStars
        if (minStars != null && minStars > 0) {
            filtered = filtered.filter { item ->
                item.stargazersCount >= minStars
            }
        }

        // 4. Recency filter
        val updatedAfter = filter.updatedAfter ?: when (filter.updatedPeriod) {
            "year" -> "2026-01-01"
            "month" -> "2026-09-01"
            else -> null
        }
        if (!updatedAfter.isNullOrBlank()) {
            filtered = filtered.filter { item ->
                (item.updatedAt ?: "") >= updatedAfter
            }
        }

        // 5. Sorting
        val sorted = when (sort) {
            SearchSort.BEST_MATCH -> filtered
            SearchSort.STARS -> filtered.sortedByDescending { it.stargazersCount }
            SearchSort.FORKS -> filtered.sortedByDescending { it.forksCount }
            SearchSort.UPDATED -> filtered.sortedByDescending { it.updatedAt ?: "" }
        }

        // 5. Pagination (10 items per page)
        val pageSize = 10
        val totalCount = sorted.size
        val startIndex = (page - 1) * pageSize
        val pagedItems = if (startIndex >= sorted.size) {
            emptyList()
        } else {
            sorted.subList(startIndex, minOf(startIndex + pageSize, sorted.size))
        }
        val hasNextPage = (page * pageSize) < totalCount

        return SearchResult(
            items = pagedItems,
            totalCount = totalCount,
            hasNextPage = hasNextPage
        )
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

    override suspend fun getRepositoryById(id: Long): RepositoryItem {
        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }

        val match = mockData.firstOrNull { it.id == id }
        return match ?: mockData.firstOrNull() ?: RepositoryItem(
            id = id,
            name = "mock/repo-$id",
            owner = Owner(login = "mock", avatarUrl = "https://avatars.githubusercontent.com/u/9919?s=200&v=4"),
            language = "Kotlin",
            stargazersCount = 1_000L,
            watchersCount = 1_000L,
            forksCount = 100L,
            openIssuesCount = 5L,
            description = "Mock repository for ID $id",
            htmlUrl = "https://github.com/mock/repo-$id"
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
                name = "tokio-rs/tokio",
                owner = Owner(login = "tokio-rs", avatarUrl = "https://avatars.githubusercontent.com/u/41551030"),
                language = "Rust",
                stargazersCount = 26_400L,
                watchersCount = 26_400L,
                forksCount = 2_400L,
                openIssuesCount = 210L,
                description = "A runtime for writing reliable, asynchronous, and slim applications with the Rust programming language.",
                htmlUrl = "https://github.com/tokio-rs/tokio"
            ),
            RepositoryItem(
                name = "tauri-apps/tauri",
                owner = Owner(login = "tauri-apps", avatarUrl = "https://avatars.githubusercontent.com/u/54033232"),
                language = "Rust",
                stargazersCount = 82_100L,
                watchersCount = 82_100L,
                forksCount = 2_800L,
                openIssuesCount = 310L,
                description = "Build smaller, faster, and more secure desktop and mobile applications with a web frontend.",
                htmlUrl = "https://github.com/tauri-apps/tauri"
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
            ),
            RepositoryItem(
                name = "gin-gonic/gin",
                owner = Owner(login = "gin-gonic", avatarUrl = "https://avatars.githubusercontent.com/u/14945934"),
                language = "Go",
                stargazersCount = 76_500L,
                watchersCount = 76_500L,
                forksCount = 7_900L,
                openIssuesCount = 80L,
                description = "Gin is a HTTP web framework written in Go (Golang). It features a Martini-like API with much better performance.",
                htmlUrl = "https://github.com/gin-gonic/gin"
            ),
            RepositoryItem(
                name = "kubernetes/kubernetes",
                owner = Owner(login = "kubernetes", avatarUrl = "https://avatars.githubusercontent.com/u/13629408"),
                language = "Go",
                stargazersCount = 110_000L,
                watchersCount = 110_000L,
                forksCount = 39_500L,
                openIssuesCount = 2_400L,
                description = "Production-Grade Container Scheduling and Management.",
                htmlUrl = "https://github.com/kubernetes/kubernetes"
            ),
            RepositoryItem(
                name = "pytorch/pytorch",
                owner = Owner(login = "pytorch", avatarUrl = "https://avatars.githubusercontent.com/u/21003710"),
                language = "Python",
                stargazersCount = 84_300L,
                watchersCount = 84_300L,
                forksCount = 22_500L,
                openIssuesCount = 16_000L,
                description = "Tensors and Dynamic neural networks in Python with strong GPU acceleration.",
                htmlUrl = "https://github.com/pytorch/pytorch"
            ),
            RepositoryItem(
                name = "psf/black",
                owner = Owner(login = "psf", avatarUrl = "https://avatars.githubusercontent.com/u/5059136"),
                language = "Python",
                stargazersCount = 38_200L,
                watchersCount = 38_200L,
                forksCount = 2_450L,
                openIssuesCount = 240L,
                description = "The uncompromising Python code formatter.",
                htmlUrl = "https://github.com/psf/black"
            ),
            RepositoryItem(
                name = "huggingface/transformers",
                owner = Owner(login = "huggingface", avatarUrl = "https://avatars.githubusercontent.com/u/25720743"),
                language = "Python",
                stargazersCount = 132_000L,
                watchersCount = 132_000L,
                forksCount = 25_800L,
                openIssuesCount = 740L,
                description = "State-of-the-art Machine Learning for Pytorch, TensorFlow, and JAX.",
                htmlUrl = "https://github.com/huggingface/transformers"
            ),
            RepositoryItem(
                name = "microsoft/typescript",
                owner = Owner(login = "microsoft", avatarUrl = "https://avatars.githubusercontent.com/u/6154722"),
                language = "TypeScript",
                stargazersCount = 99_800L,
                watchersCount = 99_800L,
                forksCount = 12_600L,
                openIssuesCount = 5_200L,
                description = "TypeScript is a superset of JavaScript that compiles to clean JavaScript output.",
                htmlUrl = "https://github.com/microsoft/typescript"
            ),
            RepositoryItem(
                name = "facebook/react",
                owner = Owner(login = "facebook", avatarUrl = "https://avatars.githubusercontent.com/u/69631"),
                language = "TypeScript",
                stargazersCount = 225_000L,
                watchersCount = 225_000L,
                forksCount = 45_800L,
                openIssuesCount = 1_100L,
                description = "The library for web and native user interfaces.",
                htmlUrl = "https://github.com/facebook/react"
            ),
            RepositoryItem(
                name = "vercel/next.js",
                owner = Owner(login = "vercel", avatarUrl = "https://avatars.githubusercontent.com/u/14985020"),
                language = "TypeScript",
                stargazersCount = 124_000L,
                watchersCount = 124_000L,
                forksCount = 27_100L,
                openIssuesCount = 2_800L,
                description = "The React Framework for the Web.",
                htmlUrl = "https://github.com/vercel/next.js"
            ),
            RepositoryItem(
                name = "tensorflow/tensorflow",
                owner = Owner(login = "tensorflow", avatarUrl = "https://avatars.githubusercontent.com/u/15658637"),
                language = "C++",
                stargazersCount = 184_000L,
                watchersCount = 184_000L,
                forksCount = 89_200L,
                openIssuesCount = 3_500L,
                description = "An Open Source Machine Learning Framework for Everyone.",
                htmlUrl = "https://github.com/tensorflow/tensorflow"
            ),
            RepositoryItem(
                name = "protocolbuffers/protobuf",
                owner = Owner(login = "protocolbuffers", avatarUrl = "https://avatars.githubusercontent.com/u/38173599"),
                language = "C++",
                stargazersCount = 65_200L,
                watchersCount = 65_200L,
                forksCount = 15_400L,
                openIssuesCount = 1_200L,
                description = "Protocol Buffers - Google's data interchange format.",
                htmlUrl = "https://github.com/protocolbuffers/protobuf"
            ),
            RepositoryItem(
                name = "electron/electron",
                owner = Owner(login = "electron", avatarUrl = "https://avatars.githubusercontent.com/u/13409222"),
                language = "C++",
                stargazersCount = 114_000L,
                watchersCount = 114_000L,
                forksCount = 15_100L,
                openIssuesCount = 1_450L,
                description = "Build cross-platform desktop apps with JavaScript, HTML, and CSS.",
                htmlUrl = "https://github.com/electron/electron"
            ),
            RepositoryItem(
                name = "extremely-long-repository-name-that-stresses-ui-layout-wrapping-and-overflow-resilience-without-truncation-or-breakage",
                owner = Owner(
                    login = "super-verbose-organization-with-extremely-lengthy-username-account-name",
                    avatarUrl = "https://avatars.githubusercontent.com/u/9919"
                ),
                language = "Visual Basic for Applications (.NET Framework Core Edition)",
                stargazersCount = 9_999_999_999L,
                watchersCount = 8_888_888_888L,
                forksCount = 7_777_777_777L,
                openIssuesCount = 6_666_666_666L,
                description = "This is an exceptionally verbose, extensive, and multi-paragraph repository description crafted specifically to test edge-case rendering, text wrapping, and UI overflow behavior in Jetpack Compose and View hierarchies. It ensures that cards expand appropriately, text does not clip unexpectedly, and typography scales gracefully under high font scale settings.",
                htmlUrl = "https://github.com/super-verbose-organization-with-extremely-lengthy-username-account-name/extremely-long-repository-name"
            ),
            RepositoryItem(
                name = "超長文リポジトリ名_UI表示崩れ・折り返し・文字あふれ検証用テストケース_株式会社ゆめみ_Android課題提出用リポジトリ",
                owner = Owner(
                    login = "yumemi-inc-advanced-android-mobile-engineering-core-team",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1010328"
                ),
                language = "TypeScript / Kotlin Multiplatform",
                stargazersCount = 123_456_789L,
                watchersCount = 123_456_789L,
                forksCount = 45_678_901L,
                openIssuesCount = 98_765L,
                description = "画面レイアウトの耐久性テスト用の長い日本語説明文です。テキストが複数行にわたって適切に折り返され、省略記号（Ellipsis）やカードの高さ調整が崩れることなく正常に描画されるかを検証します。極端に長い文字列や全角・半角混在テキストでもUIが美しく表示されることを確認するためのフィクスチャです。",
                htmlUrl = "https://github.com/yumemi-inc-advanced-android-mobile-engineering-core-team/yumemi-stress-test"
            )
        )
    }
}
