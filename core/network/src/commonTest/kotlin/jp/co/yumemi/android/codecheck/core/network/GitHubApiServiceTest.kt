package jp.co.yumemi.android.codecheck.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import jp.co.yumemi.android.codecheck.core.network.error.NetworkException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GitHubApiServiceTest {

    private fun createMockClient(
        statusCode: HttpStatusCode,
        responseJson: String
    ): HttpClient {
        val mockEngine = MockEngine {
            respond(
                content = responseJson,
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                        isLenient = true
                    }
                )
            }
        }
    }

    @Test
    fun searchRepositories_onSuccess_deserializesItems() = runTest {
        val jsonBody = """
            {
                "total_count": 1,
                "items": [
                    {
                        "name": "codecheck",
                        "full_name": "yumemi/codecheck",
                        "owner": {
                            "login": "yumemi",
                            "avatar_url": "https://example.com/icon.png"
                        },
                        "language": "Kotlin",
                        "stargazers_count": 42
                    }
                ]
            }
        """.trimIndent()

        val client = createMockClient(HttpStatusCode.OK, jsonBody)
        val apiService = GitHubApiServiceImpl(client)

        val result = apiService.searchRepositories("codecheck")

        assertEquals(1, result.items.size)
        assertEquals("yumemi/codecheck", result.items[0].fullName)
        assertEquals("Kotlin", result.items[0].language)
        assertEquals(42L, result.items[0].stargazersCount)
    }

    @Test
    fun searchRepositories_on403_throwsRateLimitExceededException() = runTest {
        val client = createMockClient(
            statusCode = HttpStatusCode.Forbidden,
            responseJson = """{"message": "API rate limit exceeded"}"""
        )
        val apiService = GitHubApiServiceImpl(client)

        assertFailsWith<NetworkException.RateLimitExceededException> {
            apiService.searchRepositories("kotlin")
        }
    }

    @Test
    fun getRepositoryDetails_on404_throwsNotFoundException() = runTest {
        val client = createMockClient(
            statusCode = HttpStatusCode.NotFound,
            responseJson = """{"message": "Not Found"}"""
        )
        val apiService = GitHubApiServiceImpl(client)

        assertFailsWith<NetworkException.NotFoundException> {
            apiService.getRepositoryDetails("owner", "nonexistent")
        }
    }
}
