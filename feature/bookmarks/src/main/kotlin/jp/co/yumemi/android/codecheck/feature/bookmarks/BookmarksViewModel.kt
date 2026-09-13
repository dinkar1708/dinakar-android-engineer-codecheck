package jp.co.yumemi.android.codecheck.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing offline bookmarked repositories.
 */
@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    val uiState: StateFlow<BookmarksUiState> = bookmarkRepository.getBookmarks()
        .map { bookmarks ->
            if (bookmarks.isEmpty()) {
                BookmarksUiState.Empty
            } else {
                BookmarksUiState.Success(repositories = bookmarks)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = BookmarksUiState.Loading
        )

    /**
     * Removes a repository from offline bookmarks.
     */
    fun removeBookmark(repository: RepositoryItem) {
        viewModelScope.launch {
            if (repository.id != 0L) {
                bookmarkRepository.removeBookmark(repository.id)
            } else {
                bookmarkRepository.removeBookmark(repository.name)
            }
        }
    }

    /**
     * Clears all offline bookmarks.
     */
    fun clearAllBookmarks() {
        viewModelScope.launch {
            bookmarkRepository.clearBookmarks()
        }
    }
}
