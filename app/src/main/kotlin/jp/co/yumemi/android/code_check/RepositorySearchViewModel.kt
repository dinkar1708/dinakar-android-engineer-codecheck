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
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.util.Date

/**
 * ViewModel for GitHub repository search functionality
 * Handles repository search logic and data management
 */
class RepositorySearchViewModel(
    application: Application
) : AndroidViewModel(application) {

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
            val client = HttpClient(Android)

            val results = async {
            try {
                val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                    header("Accept", "application/vnd.github.v3+json")
                    parameter("q", inputText)
                }

                val responseBody = response.receive<String>()
                val jsonBody = JSONObject(responseBody)

                // Validate JSON structure
                if (!jsonBody.has("items")) {
                    Log.w(TAG, "API response missing 'items' field")
                    return@async emptyList()
                }

                val jsonItems = jsonBody.optJSONArray("items") ?: return@async emptyList()

                val items = mutableListOf<RepositoryItem>()

                for (i in 0 until jsonItems.length()) {
                    val jsonItem = jsonItems.optJSONObject(i) ?: continue

                    // Validate required fields
                    val name = jsonItem.optString("full_name")
                    if (name.isEmpty()) {
                        Log.w(TAG, "Repository item missing full_name, skipping")
                        continue
                    }

                    val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: ""
                    val language = jsonItem.optString("language")
                    val stargazersCount = jsonItem.optLong("stargazers_count")
                    val watchersCount = jsonItem.optLong("watchers_count")
                    val forksCount = jsonItem.optLong("forks_count")
                    val openIssuesCount = jsonItem.optLong("open_issues_count")

                    items.add(
                        RepositoryItem(
                            name = name,
                            ownerIconUrl = ownerIconUrl,
                            language = getApplication<Application>().getString(R.string.written_language, language),
                            stargazersCount = stargazersCount,
                            watchersCount = watchersCount,
                            forksCount = forksCount,
                            openIssuesCount = openIssuesCount
                        )
                    )
                }

                TopActivity.lastSearchDate = Date()

                return@async items.toList()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to search repositories: ${e.message}", e)
                return@async emptyList()
            } finally {
                client.close()
            }
            }.await()

            _searchResults.value = results
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