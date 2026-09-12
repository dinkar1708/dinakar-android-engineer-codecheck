package jp.co.yumemi.android.codecheck.core.domain.model

/**
 * Supported sorting options for repository search adhering to GitHub REST API.
 */
enum class SearchSort(
    val label: String,
    val apiValue: String?,
    val order: String?
) {
    BEST_MATCH("Best match", null, null),
    STARS("Most stars", "stars", "desc"),
    FORKS("Most forks", "forks", "desc"),
    UPDATED("Recently updated", "updated", "desc");
}
