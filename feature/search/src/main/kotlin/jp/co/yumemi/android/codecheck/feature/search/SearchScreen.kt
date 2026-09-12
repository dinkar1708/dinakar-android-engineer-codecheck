package jp.co.yumemi.android.codecheck.feature.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppNavy
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate400
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate900
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.ui.component.EmptyView
import jp.co.yumemi.android.codecheck.core.ui.component.ErrorView
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.search.R
import jp.co.yumemi.android.codecheck.feature.search.component.LoadMoreButton
import jp.co.yumemi.android.codecheck.feature.search.component.RecentSearchesSection
import jp.co.yumemi.android.codecheck.feature.search.component.RepositoryCard
import jp.co.yumemi.android.codecheck.feature.search.component.RepositoryCardSkeleton
import jp.co.yumemi.android.codecheck.feature.search.component.SortTabs
import java.text.NumberFormat
import java.util.Locale

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import jp.co.yumemi.android.codecheck.feature.search.component.FilterBar
import jp.co.yumemi.android.codecheck.feature.search.component.FilterBottomSheet

/**
 * Screen composable for searching GitHub repositories matching the approved design specification:
 * - Dark Navy anchor header housing the search bar (#1d2331 / #12161f in dark)
 * - Sort underline tabs ("Best match", "Most stars", "Most forks")
 * - Filter bar with "Filters" button and active filter chips
 * - Filter sheet for Language, Min Stars, and Recency filtering
 * - Sub-header results counter ("1–12 OF 3,120") and "Clear all"
 * - Repository cards and "Load more" pagination button
 * - Skeleton loading and empty state with "Clear search" action
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    SearchScreen(
        uiState = uiState,
        query = query,
        selectedSort = selectedSort,
        filter = filter,
        searchHistory = searchHistory,
        onQueryChanged = viewModel::onQueryChanged,
        onSearch = { viewModel.searchRepositories(query) },
        onRecentQueryClick = { recentQuery ->
            viewModel.onQueryChanged(recentQuery)
            viewModel.searchRepositories(recentQuery)
        },
        onRemoveRecentQuery = viewModel::removeSearchHistory,
        onSortSelected = viewModel::onSortChanged,
        onFilterChanged = viewModel::onFilterChanged,
        onLoadNextPage = viewModel::loadNextPage,
        onClearQuery = viewModel::clearQuery,
        onClearAll = {
            viewModel.clearQuery()
            viewModel.clearFilters()
        },
        onRetry = viewModel::retry,
        onRepositoryClick = onRepositoryClick,
        modifier = modifier
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    uiState: SearchUiState,
    query: String,
    selectedSort: SearchSort = SearchSort.BEST_MATCH,
    filter: SearchFilter = SearchFilter(),
    searchHistory: List<String> = emptyList(),
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onRecentQueryClick: (String) -> Unit = {},
    onRemoveRecentQuery: (String) -> Unit = {},
    onSortSelected: (SearchSort) -> Unit = {},
    onFilterChanged: (SearchFilter) -> Unit = {},
    onLoadNextPage: () -> Unit = {},
    onClearQuery: () -> Unit,
    onClearAll: () -> Unit = onClearQuery,
    onRetry: () -> Unit,
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }

    val headerBgColor = AppNavy
    val searchBoxBgColor = AppWhite
    val searchBoxBorderColor = Slate200
    val searchIconColor = AppBlue
    val searchTextColor = Slate900
    val screenTitle = stringResource(R.string.search_screen_title)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = screenTitle },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Dark Navy Anchor Header housing the Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBgColor)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = searchBoxBgColor,
                    border = BorderStroke(1.dp, searchBoxBorderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_submit_content_description),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    focusManager.clearFocus()
                                    onSearch()
                                },
                            tint = searchIconColor
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        BasicTextField(
                            value = query,
                            onValueChange = onQueryChanged,
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = searchTextColor,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                    onSearch()
                                }
                            ),
                            cursorBrush = SolidColor(AppBlue),
                            decorationBox = { innerTextField ->
                                if (query.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.search_input_placeholder),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 15.sp
                                        ),
                                        color = Slate400
                                    )
                                }
                                innerTextField()
                            }
                        )

                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = onClearQuery,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.search_clear_content_description),
                                    modifier = Modifier.size(18.dp),
                                    tint = Slate500
                                )
                            }
                        }
                    }
                }
            }

            // Sort Tabs & Filter Bar always visible below search box
            if (query.isNotEmpty()) {
                SortTabs(
                    selectedSort = selectedSort,
                    onSortSelected = onSortSelected
                )

                FilterBar(
                    filter = filter,
                    onOpenFilterSheet = { showFilterSheet = true },
                    onRemoveLanguage = { onFilterChanged(filter.copy(language = null)) },
                    onRemoveMinStars = { onFilterChanged(filter.copy(minStars = null)) },
                    onRemoveUpdatedPeriod = { onFilterChanged(filter.copy(updatedPeriod = "any", updatedAfter = null)) }
                )
            }

            // Body Area presenting stateful views
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is SearchUiState.Idle -> {
                        if (searchHistory.isNotEmpty()) {
                            RecentSearchesSection(
                                history = searchHistory,
                                onQueryClick = onRecentQueryClick,
                                onRemoveQuery = onRemoveRecentQuery
                            )
                        } else {
                            EmptyView(
                                title = stringResource(R.string.search_idle_title),
                                description = stringResource(R.string.search_idle_description)
                            )
                        }
                    }

                    is SearchUiState.Loading -> {
                        // Display skeleton placeholder cards matching mockup
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(4) {
                                RepositoryCardSkeleton()
                            }
                        }
                    }

                    is SearchUiState.Empty -> {
                        EmptyView(
                            title = stringResource(R.string.search_empty_title),
                            description = stringResource(R.string.search_empty_description, query),
                            actionLabel = stringResource(R.string.search_clear_search),
                            onActionClick = onClearQuery
                        )
                    }

                    is SearchUiState.Error -> {
                        ErrorView(
                            message = state.message,
                            onRetry = onRetry
                        )
                    }

                    is SearchUiState.Success -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Sub-header results counter & clear all (mockup 03b)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val totalFormatted = NumberFormat.getNumberInstance(Locale.US).format(state.totalCount)
                                Text(
                                    text = stringResource(R.string.search_results_counter, 1, state.repositories.size, totalFormatted),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.05.em,
                                    color = Slate500
                                )

                                Text(
                                    text = stringResource(R.string.search_clear_all),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppBlue,
                                    modifier = Modifier.clickable(onClick = onClearAll)
                                )
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(
                                    items = state.repositories,
                                    key = { it.name + "/" + it.ownerIconUrl }
                                ) { item ->
                                    RepositoryCard(
                                        item = item,
                                        onClick = onRepositoryClick
                                    )
                                }

                                if (state.hasNextPage) {
                                    item(key = "load_more_button") {
                                        LoadMoreButton(
                                            isLoading = state.isLoadingMore,
                                            onClick = onLoadNextPage,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            initialFilter = filter,
            onApplyFilter = onFilterChanged,
            onDismiss = { showFilterSheet = false }
        )
    }
}

