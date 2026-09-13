package jp.co.yumemi.android.codecheck.feature.starred

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem

/**
 * UI State for the Starred repositories screen.
 */
sealed interface StarredUiState {
    data object Loading : StarredUiState
    data object Empty : StarredUiState
    data class Success(
        val repositories: List<RepositoryItem>
    ) : StarredUiState
}
