package jp.co.yumemi.android.codecheck.core.domain.model

/**
 * Filter criteria for repository search.
 *
 * @param language Programming language filter (e.g. "Rust", "Kotlin")
 * @param minStars Minimum number of stars (e.g. 100, 500, 1000)
 * @param updatedPeriod Recency period: "any", "year", "month"
 * @param updatedAfter Optional ISO-8601 date string (YYYY-MM-DD) for recency filtering
 */
data class SearchFilter(
    val language: String? = null,
    val minStars: Int? = null,
    val updatedPeriod: String = "any",
    val updatedAfter: String? = null
) {
    val isActive: Boolean
        get() = !language.isNullOrBlank() ||
            (minStars != null && minStars > 0) ||
            updatedPeriod != "any" ||
            !updatedAfter.isNullOrBlank()
}
