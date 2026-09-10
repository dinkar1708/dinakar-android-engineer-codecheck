/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.api

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

/**
 * Implementation of GitHubApiClient using Ktor HTTP client
 */
class GitHubApiClientImpl : GitHubApiClient {

    private val client = HttpClient(Android)

    /**
     * Search repositories using GitHub search API
     * @param query Search keyword
     * @return JSON response as String
     * @throws Exception if request fails
     */
    override suspend fun searchRepositories(query: String): String {
        val response: HttpResponse = client.get(BASE_URL) {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", query)
        }
        return response.receive()
    }

    /**
     * Close the HTTP client
     */
    fun close() {
        client.close()
    }

    companion object {
        private const val BASE_URL = "https://api.github.com/search/repositories"
    }
}
