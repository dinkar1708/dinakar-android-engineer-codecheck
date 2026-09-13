package jp.co.yumemi.android.codecheck.core.domain.usecase

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository

/**
 * Use case that encapsulates the business logic for fetching repository details.
 * Validates inputs and coordinates retrieval from [GitHubRepository].
 */
class GetRepositoryDetailsUseCase(
    private val repository: GitHubRepository
) {
    /**
     * Executes repository detail lookup.
     *
     * @param owner Repository owner username / organisation
     * @param repo Repository name
     * @return Detailed [RepositoryItem]
     * @throws IllegalArgumentException if owner or repo is blank
     */
    suspend operator fun invoke(owner: String, repo: String): RepositoryItem {
        val trimmedOwner = owner.trim()
        val trimmedRepo = repo.trim()
        require(trimmedOwner.isNotBlank()) { "Repository owner must not be blank" }
        require(trimmedRepo.isNotBlank()) { "Repository name must not be blank" }

        return repository.getRepositoryDetails(trimmedOwner, trimmedRepo)
    }
}
