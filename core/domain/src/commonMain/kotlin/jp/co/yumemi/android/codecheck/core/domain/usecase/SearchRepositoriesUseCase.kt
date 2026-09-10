package jp.co.yumemi.android.codecheck.core.domain.usecase

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository

/**
 * Use case that encapsulates the business logic for searching GitHub repositories.
 * Sanitizes input queries and delegates to [GitHubRepository].
 */
class SearchRepositoriesUseCase(
    private val repository: GitHubRepository
) {
    /**
     * Executes repository search.
     *
     * @param query Search keyword entered by the user
     * @return List of matching [RepositoryItem], or empty list if query is blank
     */
    suspend operator fun invoke(query: String): List<RepositoryItem> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return emptyList()
        }
        return repository.searchRepositories(trimmed)
    }
}
