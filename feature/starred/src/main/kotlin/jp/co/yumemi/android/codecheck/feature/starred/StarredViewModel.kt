package jp.co.yumemi.android.codecheck.feature.starred

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.StarredRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing starred repositories for offline browsing.
 */
@HiltViewModel
class StarredViewModel @Inject constructor(
    private val starredRepository: StarredRepository
) : ViewModel() {

    val uiState: StateFlow<StarredUiState> = starredRepository.getStarredRepositories()
        .map { starred ->
            if (starred.isEmpty()) {
                StarredUiState.Empty
            } else {
                StarredUiState.Success(repositories = starred)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StarredUiState.Loading
        )

    /**
     * Unstars a repository from offline list.
     */
    fun unstar(repository: RepositoryItem) {
        viewModelScope.launch {
            if (repository.id != 0L) {
                starredRepository.unstarRepository(repository.id)
            } else {
                starredRepository.unstarRepository(repository.name)
            }
        }
    }

    /**
     * Clears all starred repositories.
     */
    fun clearAllStars() {
        viewModelScope.launch {
            starredRepository.clearAllStars()
        }
    }
}
