package jp.co.yumemi.android.codecheck.core.domain.model

/**
 * Domain entity representing a GitHub repository item.
 * Zero Android framework dependencies (pure Kotlin / KMP).
 */
data class RepositoryItem(
    val name: String,
    val owner: Owner,
    val language: String? = null,
    val stargazersCount: Long = 0L,
    val watchersCount: Long = 0L,
    val forksCount: Long = 0L,
    val openIssuesCount: Long = 0L,
    val description: String? = null,
    val htmlUrl: String? = null,
    val updatedAt: String? = null
) {
    /**
     * Convenience property exposing the owner avatar URL for presentation layers.
     */
    val ownerIconUrl: String
        get() = owner.avatarUrl
}
