/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.api.GitHubApiClient
import jp.co.yumemi.android.code_check.api.GitHubApiClientImpl
import jp.co.yumemi.android.code_check.api.RepositoryMapper
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * ViewModel for GitHub repository search functionality
 * Handles repository search logic and data management
 */
class RepositorySearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val apiClient: GitHubApiClient = GitHubApiClientImpl()

    private val _searchResults = MutableLiveData<List<RepositoryItem>>()
    val searchResults: LiveData<List<RepositoryItem>> = _searchResults

    /**
     * Search GitHub repositories by keyword using the GitHub API
     * @param inputText Search keyword
     */
    fun searchRepositories(inputText: String) {
        // Validate input
        if (inputText.isBlank()) {
            Log.w(TAG, "Search query is empty")
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val responseBody = apiClient.searchRepositories(inputText)
                val items = RepositoryMapper.parseSearchResponse(responseBody, getApplication())

                TopActivity.lastSearchDate = Date()

                _searchResults.value = items
            } catch (e: Exception) {
                Log.e(TAG, "Failed to search repositories: ${e.message}", e)
                _searchResults.value = emptyList()
            }
        }
    }

    companion object {
        private const val TAG = "RepositorySearchVM"
    }
}

/**
 * Data class representing a GitHub repository item
 */
@Parcelize
data class RepositoryItem(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable