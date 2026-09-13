package jp.co.yumemi.android.codecheck.feature.bookmarks

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem

/**
 * UI State for the Bookmarks screen.
 */
sealed interface BookmarksUiState {
    data object Loading : BookmarksUiState
    data object Empty : BookmarksUiState
    data class Success(
        val repositories: List<RepositoryItem>
    ) : BookmarksUiState
}
