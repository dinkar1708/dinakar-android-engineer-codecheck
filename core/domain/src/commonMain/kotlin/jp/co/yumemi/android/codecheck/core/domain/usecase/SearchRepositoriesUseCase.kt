package jp.co.yumemi.android.codecheck.core.domain.usecase

import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchResult
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository

/**
 * Use case that encapsulates the business logic for searching GitHub repositories.
 * Sanitizes input queries and delegates to [GitHubRepository].
 */
class SearchRepositoriesUseCase(
    private val repository: GitHubRepository
) {
    /**
     * Executes paginated repository search.
     *
     * @param query Search keyword entered by the user
     * @param page Page number (1-indexed)
     * @param sort Sort ordering ([SearchSort])
     * @param filter Filter criteria ([SearchFilter])
     * @return Paginated [SearchResult]
     */
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        sort: SearchSort = SearchSort.BEST_MATCH,
        filter: SearchFilter = SearchFilter()
    ): SearchResult {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return SearchResult(emptyList(), 0, false)
        }
        return repository.searchRepositories(
            query = trimmed,
            page = page,
            sort = sort,
            filter = filter
        )
    }
}
