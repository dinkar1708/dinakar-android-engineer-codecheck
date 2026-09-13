package jp.co.yumemi.android.codecheck.feature.detail

import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem

/**
 * UI state hierarchy for repository detail display following Unidirectional Data Flow (UDF).
 */
sealed interface DetailUiState {

    /** Repository details request in flight */
    data object Loading : DetailUiState

    /** Successfully loaded repository details */
    data class Success(val repository: RepositoryItem) : DetailUiState

    /** Failed to load repository details */
    data class Error(val message: String) : DetailUiState
}
