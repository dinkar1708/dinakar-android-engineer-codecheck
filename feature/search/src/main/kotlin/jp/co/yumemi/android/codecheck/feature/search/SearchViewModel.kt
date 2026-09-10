package jp.co.yumemi.android.codecheck.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel coordinating GitHub repository search flow and exposing immutable [SearchUiState].
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_LAST_QUERY = "last_search_query"
    }

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow(savedStateHandle.get<String>(KEY_LAST_QUERY).orEmpty())
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    init {
        val initialQuery = savedStateHandle.get<String>(KEY_LAST_QUERY)
        if (!initialQuery.isNullOrBlank()) {
            searchRepositories(initialQuery)
        }
    }

    /**
     * Updates the active search query text.
     */
    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        savedStateHandle[KEY_LAST_QUERY] = newQuery
    }

    /**
     * Executes repository search for the provided [searchQuery].
     */
    fun searchRepositories(searchQuery: String) {
        val trimmed = searchQuery.trim()
        if (trimmed.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val results = searchRepositoriesUseCase(trimmed)
                _uiState.value = if (results.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(results)
                }
            } catch (throwable: Throwable) {
                _uiState.value = SearchUiState.Error(
                    message = throwable.message ?: "An unexpected error occurred. Please try again."
                )
            }
        }
    }

    /**
     * Retries the current query if available.
     */
    fun retry() {
        val currentQuery = _query.value
        if (currentQuery.isNotBlank()) {
            searchRepositories(currentQuery)
        }
    }

    /**
     * Clears the current query and resets state to [SearchUiState.Idle].
     */
    fun clearQuery() {
        _query.value = ""
        savedStateHandle[KEY_LAST_QUERY] = ""
        _uiState.value = SearchUiState.Idle
    }
}
