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
import jp.co.yumemi.android.codecheck.feature.search.component.RepositoryCard
import jp.co.yumemi.android.codecheck.feature.search.component.RepositoryCardSkeleton

/**
 * Screen composable for searching GitHub repositories matching the approved design specification:
 * - Dark Navy anchor header housing the search bar (#1d2331 / #12161f in dark)
 * - Brand Blue for action (#3b50df / #6b7cf0 in dark)
 * - Slate neutrals for structure (#0f172a -> #f5f6f8)
 * - Results count and sort controls
 * - Skeleton loading and empty state with "Clear search" action
 */
@Composable
fun SearchScreen(
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    SearchScreen(
        uiState = uiState,
        query = query,
        onQueryChanged = viewModel::onQueryChanged,
        onSearch = { viewModel.searchRepositories(query) },
        onClearQuery = viewModel::clearQuery,
        onRetry = viewModel::retry,
        onRepositoryClick = onRepositoryClick,
        modifier = modifier
    )
}

@Composable
internal fun SearchScreen(
    uiState: SearchUiState,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    onRetry: () -> Unit,
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    val headerBgColor = AppNavy
    val searchBoxBgColor = AppWhite
    val searchBoxBorderColor = Slate200
    val searchIconColor = AppBlue
    val searchTextColor = Slate900

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "GitHub Repository Search" },
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
                            contentDescription = "Submit search",
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
                                        text = "Search repositories...",
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
                                    contentDescription = "Clear search",
                                    modifier = Modifier.size(18.dp),
                                    tint = Slate500
                                )
                            }
                        }
                    }
                }
            }

            // Body Area presenting stateful views
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is SearchUiState.Idle -> {
                        EmptyView(
                            title = "Search GitHub Repositories",
                            description = "Type a search query above and press Enter."
                        )
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
                            title = "No repositories found",
                            description = "No results for “$query”. Check the spelling or search a shorter term.",
                            actionLabel = "Clear search",
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
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
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
                        }
                    }
                }
            }
        }
    }
}

