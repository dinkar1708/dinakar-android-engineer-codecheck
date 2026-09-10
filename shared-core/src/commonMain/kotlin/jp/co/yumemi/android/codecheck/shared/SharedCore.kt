package jp.co.yumemi.android.codecheck.shared

import jp.co.yumemi.android.codecheck.core.data.di.DataModule
import jp.co.yumemi.android.codecheck.core.domain.repository.GitHubRepository
import jp.co.yumemi.android.codecheck.core.domain.usecase.GetRepositoryDetailsUseCase
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase

/**
 * Shared Core multiplatform umbrella facade.
 * Exposes core use cases and repository access for native iOS SwiftUI and shared clients.
 */
object SharedCore {
    val repository: GitHubRepository by lazy { DataModule.provideGitHubRepository() }
    val searchRepositoriesUseCase: SearchRepositoriesUseCase by lazy { SearchRepositoriesUseCase(repository) }
    val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase by lazy { GetRepositoryDetailsUseCase(repository) }
}
