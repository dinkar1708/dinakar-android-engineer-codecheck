package jp.co.yumemi.android.codecheck.feature.search

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem

/**
 * UI state hierarchy for GitHub repository search following Unidirectional Data Flow (UDF).
 */
sealed interface SearchUiState {

    /** Initial state before any search has been performed */
    data object Idle : SearchUiState

    /** Active search request in progress */
    data object Loading : SearchUiState

    /** Successfully retrieved non-empty list of repositories */
    data class Success(val repositories: List<RepositoryItem>) : SearchUiState

    /** Search completed but returned zero results */
    data object Empty : SearchUiState

    /** Search failed due to network error, rate limit, or invalid input */
    data class Error(val message: String) : SearchUiState
}
