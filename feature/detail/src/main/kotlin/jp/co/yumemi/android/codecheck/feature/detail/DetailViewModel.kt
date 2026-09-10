package jp.co.yumemi.android.codecheck.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.usecase.GetRepositoryDetailsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing repository detail screen state and lifecycle.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var currentOwner: String = savedStateHandle.get<String>("owner").orEmpty()
    private var currentRepo: String = savedStateHandle.get<String>("repo").orEmpty()

    init {
        if (currentOwner.isNotBlank() && currentRepo.isNotBlank()) {
            loadDetails(currentOwner, currentRepo)
        }
    }

    /**
     * Loads detailed repository information for the given [owner] and [repo].
     */
    fun loadDetails(owner: String, repo: String) {
        currentOwner = owner
        currentRepo = repo
        savedStateHandle["owner"] = owner
        savedStateHandle["repo"] = repo

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val item = getRepositoryDetailsUseCase(owner, repo)
                _uiState.value = DetailUiState.Success(item)
            } catch (throwable: Throwable) {
                _uiState.value = DetailUiState.Error(
                    message = throwable.message ?: "Failed to load repository details."
                )
            }
        }
    }

    /**
     * Seed details with a pre-existing [RepositoryItem] (e.g. from search navigation).
     */
    fun setRepository(item: RepositoryItem) {
        _uiState.value = DetailUiState.Success(item)
    }

    /**
     * Retries loading repository details.
     */
    fun retry() {
        if (currentOwner.isNotBlank() && currentRepo.isNotBlank()) {
            loadDetails(currentOwner, currentRepo)
        }
    }
}
