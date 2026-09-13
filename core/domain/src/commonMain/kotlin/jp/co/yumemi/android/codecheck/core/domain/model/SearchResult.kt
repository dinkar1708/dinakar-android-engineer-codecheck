package jp.co.yumemi.android.codecheck.core.domain.model

/**
 * Paginated search response holding repository items and match metadata.
 *
 * @param items List of repository items for the current page
 * @param totalCount Total count of matching repositories across GitHub
 * @param hasNextPage Whether additional pages are available to fetch
 */
data class SearchResult(
    val items: List<RepositoryItem> = emptyList(),
    val totalCount: Int = 0,
    val hasNextPage: Boolean = false
)
