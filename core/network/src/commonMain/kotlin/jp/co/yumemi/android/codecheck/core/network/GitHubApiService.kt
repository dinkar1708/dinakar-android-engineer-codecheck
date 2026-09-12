package jp.co.yumemi.android.codecheck.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import jp.co.yumemi.android.codecheck.core.network.error.NetworkException
import jp.co.yumemi.android.codecheck.core.network.model.RepositoryItemDto
import jp.co.yumemi.android.codecheck.core.network.model.SearchResponseDto
import kotlinx.serialization.json.Json

/**
 * Service contract for interacting with GitHub REST API v3.
 */
interface GitHubApiService {

    /**
     * Search repositories by keyword query with pagination, sorting, and ordering.
     */
    suspend fun searchRepositories(
        query: String,
        page: Int = 1,
        perPage: Int = 30,
        sort: String? = null,
        order: String? = null
    ): SearchResponseDto

    /**
     * Fetch detailed repository metadata.
     */
    suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItemDto

    /**
     * Fetch repository metadata directly by numeric repository ID.
     */
    suspend fun getRepositoryById(id: Long): RepositoryItemDto
}

/**
 * Multiplatform implementation of [GitHubApiService] powered by Ktor.
 */
class GitHubApiServiceImpl(
    private val client: HttpClient
) : GitHubApiService {

    constructor() : this(createDefaultHttpClient())

    override suspend fun searchRepositories(
        query: String,
        page: Int,
        perPage: Int,
        sort: String?,
        order: String?
    ): SearchResponseDto {
        return safeApiCall {
            val endpoint = "$BASE_URL/search/repositories"
            println("[GitHubApi] --> GET $endpoint?q=$query&page=$page&sort=$sort&order=$order")
            val response: HttpResponse = client.get(endpoint) {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", query)
                parameter("page", page)
                parameter("per_page", perPage)
                if (!sort.isNullOrBlank()) parameter("sort", sort)
                if (!order.isNullOrBlank()) parameter("order", order)
            }
            println("[GitHubApi] <-- ${response.status.value} ${response.status.description} (${response.call.request.url})")
            handleHttpResponse(response)
        }
    }

    override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItemDto {
        return safeApiCall {
            val endpoint = "$BASE_URL/repos/$owner/$repo"
            println("[GitHubApi] --> GET $endpoint")
            val response: HttpResponse = client.get(endpoint) {
                header("Accept", "application/vnd.github.v3+json")
            }
            println("[GitHubApi] <-- ${response.status.value} ${response.status.description} (${response.call.request.url})")
            handleHttpResponse(response)
        }
    }

    override suspend fun getRepositoryById(id: Long): RepositoryItemDto {
        return safeApiCall {
            val endpoint = "$BASE_URL/repositories/$id"
            println("[GitHubApi] --> GET $endpoint")
            val response: HttpResponse = client.get(endpoint) {
                header("Accept", "application/vnd.github.v3+json")
            }
            println("[GitHubApi] <-- ${response.status.value} ${response.status.description} (${response.call.request.url})")
            handleHttpResponse(response)
        }
    }

    private suspend inline fun <reified T> handleHttpResponse(response: HttpResponse): T {
        when (response.status) {
            HttpStatusCode.OK -> return response.body()
            HttpStatusCode.Forbidden -> throw NetworkException.RateLimitExceededException()
            HttpStatusCode.NotFound -> throw NetworkException.NotFoundException()
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
            HttpStatusCode.ServiceUnavailable -> throw NetworkException.ServerException(response.status.value)
            else -> throw NetworkException.ServerException(
                response.status.value,
                "HTTP request returned status ${response.status}"
            )
        }
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: NetworkException) {
            println("[GitHubApi] <-- Failure: ${e::class.simpleName} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("[GitHubApi] <-- Unexpected Error: ${e.message}")
            throw NetworkException.UnknownNetworkException(e.message ?: "Network execution failed", e)
        }
    }

    companion object {
        private const val BASE_URL = "https://api.github.com"

        fun createDefaultHttpClient(): HttpClient {
            return HttpClient(createHttpClientEngine()) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            coerceInputValues = true
                            isLenient = true
                        }
                    )
                }
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("[KtorHttp] $message")
                        }
                    }
                    level = LogLevel.ALL
                }
            }
        }
    }
}
