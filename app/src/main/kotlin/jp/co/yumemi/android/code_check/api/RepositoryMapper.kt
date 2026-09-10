/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.api

import android.content.Context
import android.util.Log
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.RepositoryItem
import org.json.JSONObject

/**
 * Mapper for converting GitHub API JSON responses to domain models
 * Handles parsing and validation of repository data
 */
object RepositoryMapper {

    private const val TAG = "RepositoryMapper"

    /**
     * Parse GitHub search repositories API response
     * @param jsonString Raw JSON response from API
     * @param context Android context for string resources
     * @return List of RepositoryItem or empty list if parsing fails
     */
    fun parseSearchResponse(jsonString: String, context: Context): List<RepositoryItem> {
        return try {
            val jsonBody = JSONObject(jsonString)

            // Validate JSON structure
            if (!jsonBody.has("items")) {
                Log.w(TAG, "API response missing 'items' field")
                return emptyList()
            }

            val jsonItems = jsonBody.optJSONArray("items") ?: return emptyList()

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
                        language = context.getString(R.string.written_language, language),
                        stargazersCount = stargazersCount,
                        watchersCount = watchersCount,
                        forksCount = forksCount,
                        openIssuesCount = openIssuesCount
                    )
                )
            }

            items
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse search response: ${e.message}", e)
            emptyList()
        }
    }
}
