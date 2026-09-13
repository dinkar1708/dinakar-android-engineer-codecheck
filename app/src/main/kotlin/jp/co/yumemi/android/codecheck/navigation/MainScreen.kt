package jp.co.yumemi.android.codecheck.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import jp.co.yumemi.android.codecheck.core.designsystem.component.AppBottomBar
import jp.co.yumemi.android.codecheck.core.designsystem.component.MainTab
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.feature.bookmarks.BookmarksScreen
import jp.co.yumemi.android.codecheck.feature.bookmarks.BookmarksViewModel
import jp.co.yumemi.android.codecheck.feature.search.SearchScreen
import jp.co.yumemi.android.codecheck.feature.search.SearchViewModel
import jp.co.yumemi.android.codecheck.feature.settings.SettingsScreen
import jp.co.yumemi.android.codecheck.feature.settings.SettingsViewModel

/**
 * Main application screen hosting the 3 bottom navigation tabs:
 * 1. Search (Home)
 * 2. Bookmarks (Offline saved repositories)
 * 3. Settings (Theme, Language, App Info)
 */
@Composable
fun MainScreen(
    searchViewModel: SearchViewModel,
    bookmarksViewModel: BookmarksViewModel,
    settingsViewModel: SettingsViewModel,
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier,
    initialTab: MainTab = MainTab.SEARCH
) {
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                currentTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.SEARCH -> {
                    SearchScreen(
                        viewModel = searchViewModel,
                        onRepositoryClick = onRepositoryClick
                    )
                }
                MainTab.BOOKMARKS -> {
                    BookmarksScreen(
                        viewModel = bookmarksViewModel,
                        onRepositoryClick = onRepositoryClick
                    )
                }
                MainTab.SETTINGS -> {
                    SettingsScreen(
                        viewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
