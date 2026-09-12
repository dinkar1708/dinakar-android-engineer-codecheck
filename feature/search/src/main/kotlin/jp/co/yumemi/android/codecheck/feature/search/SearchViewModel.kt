package jp.co.yumemi.android.codecheck.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
import jp.co.yumemi.android.codecheck.core.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel coordinating GitHub repository search flow, debounced queries,
 * pagination, sorting, and filtering while exposing immutable [SearchUiState].
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_LAST_QUERY = "last_search_query"
        private const val DEBOUNCE_MILLIS = 500L
    }

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow(savedStateHandle.get<String>(KEY_LAST_QUERY).orEmpty())
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedSort = MutableStateFlow(SearchSort.BEST_MATCH)
    val selectedSort: StateFlow<SearchSort> = _selectedSort.asStateFlow()

    private val _filter = MutableStateFlow(SearchFilter())
    val filter: StateFlow<SearchFilter> = _filter.asStateFlow()

    private var currentPage: Int = 1
    private var searchJob: Job? = null
    private var lastSearchedQuery: String = ""
    private var lastSearchedSort: SearchSort = SearchSort.BEST_MATCH
    private var lastSearchedFilter: SearchFilter = SearchFilter()

    init {
        val initialQuery = savedStateHandle.get<String>(KEY_LAST_QUERY).orEmpty()
        if (initialQuery.isNotBlank()) {
            executeSearch(initialQuery, page = 1)
        }

        viewModelScope.launch {
            _query
                .debounce(DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .collect { debouncedQuery ->
                    val trimmed = debouncedQuery.trim()
                    if (trimmed.isBlank()) {
                        lastSearchedQuery = ""
                        _uiState.value = SearchUiState.Idle
                    } else if (trimmed != lastSearchedQuery || _selectedSort.value != lastSearchedSort || _filter.value != lastSearchedFilter) {
                        executeSearch(trimmed, page = 1)
                    }
                }
        }
    }

    /**
     * Updates the active search query text and debounces automatic execution.
     */
    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        savedStateHandle[KEY_LAST_QUERY] = newQuery
    }

    /**
     * Executes repository search immediately for the provided [searchQuery].
     */
    fun searchRepositories(searchQuery: String) {
        val trimmed = searchQuery.trim()
        if (trimmed.isBlank()) {
            lastSearchedQuery = ""
            _uiState.value = SearchUiState.Idle
            return
        }
        executeSearch(trimmed, page = 1)
    }

    /**
     * Selects a sort ordering criteria and re-executes search from page 1.
     */
    fun onSortChanged(newSort: SearchSort) {
        if (_selectedSort.value == newSort) return
        _selectedSort.value = newSort
        val currentQuery = _query.value.trim()
        if (currentQuery.isNotBlank()) {
            executeSearch(currentQuery, page = 1)
        }
    }

    /**
     * Updates filter criteria and re-executes search from page 1.
     */
    fun onFilterChanged(newFilter: SearchFilter) {
        if (_filter.value == newFilter) return
        _filter.value = newFilter
        val currentQuery = _query.value.trim()
        if (currentQuery.isNotBlank()) {
            executeSearch(currentQuery, page = 1)
        }
    }

    /**
     * Resets filter criteria to default and refreshes results.
     */
    fun clearFilters() {
        onFilterChanged(SearchFilter())
    }

    /**
     * Loads the next page of results and appends to existing items.
     */
    fun loadNextPage() {
        val currentState = _uiState.value as? SearchUiState.Success ?: return
        if (currentState.isLoadingMore || !currentState.hasNextPage) return

        val currentQuery = _query.value.trim()
        if (currentQuery.isBlank()) return

        val nextPage = currentPage + 1
        _uiState.value = currentState.copy(isLoadingMore = true)

        viewModelScope.launch {
            try {
                val result = searchRepositoriesUseCase(
                    query = currentQuery,
                    page = nextPage,
                    sort = _selectedSort.value,
                    filter = _filter.value
                )
                currentPage = nextPage
                _uiState.value = currentState.copy(
                    repositories = currentState.repositories + result.items,
                    totalCount = result.totalCount,
                    hasNextPage = result.hasNextPage,
                    isLoadingMore = false
                )
            } catch (e: CancellationException) {
                // Ignore cancellation - this is expected when user navigates away or changes query
                throw e
            } catch (throwable: Throwable) {
                _uiState.value = currentState.copy(isLoadingMore = false)
            }
        }
    }

    /**
     * Retries the current query if available.
     */
    fun retry() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isNotBlank()) {
            executeSearch(currentQuery, page = 1)
        }
    }

    /**
     * Clears the current query and resets state to [SearchUiState.Idle].
     */
    fun clearQuery() {
        _query.value = ""
        savedStateHandle[KEY_LAST_QUERY] = ""
        lastSearchedQuery = ""
        searchJob?.cancel()
        _uiState.value = SearchUiState.Idle
    }

    private fun executeSearch(query: String, page: Int) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            lastSearchedQuery = query
            lastSearchedSort = _selectedSort.value
            lastSearchedFilter = _filter.value
            currentPage = page

            try {
                val result = searchRepositoriesUseCase(
                    query = query,
                    page = page,
                    sort = _selectedSort.value,
                    filter = _filter.value
                )
                if (result.items.isEmpty()) {
                    _uiState.value = SearchUiState.Empty
                } else {
                    _uiState.value = SearchUiState.Success(
                        repositories = result.items,
                        totalCount = result.totalCount,
                        hasNextPage = result.hasNextPage,
                        isLoadingMore = false
                    )
                }
            } catch (e: CancellationException) {
                // Ignore cancellation - this is expected during debouncing when user types quickly
                // The job was cancelled by a newer search request, so don't show error
                throw e
            } catch (throwable: Throwable) {
                _uiState.value = SearchUiState.Error(
                    message = throwable.message ?: "An unexpected error occurred. Please try again."
                )
            }
        }
    }
}
