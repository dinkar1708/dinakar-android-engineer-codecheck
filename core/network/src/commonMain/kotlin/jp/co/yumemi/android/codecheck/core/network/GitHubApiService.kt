package jp.co.yumemi.android.codecheck.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
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
     * Search repositories by keyword query.
     */
    suspend fun searchRepositories(query: String): SearchResponseDto

    /**
     * Fetch detailed repository metadata.
     */
    suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItemDto
}

/**
 * Multiplatform implementation of [GitHubApiService] powered by Ktor.
 */
class GitHubApiServiceImpl(
    private val client: HttpClient
) : GitHubApiService {

    constructor() : this(createDefaultHttpClient())

    override suspend fun searchRepositories(query: String): SearchResponseDto {
        return safeApiCall {
            val response: HttpResponse = client.get("$BASE_URL/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", query)
            }
            handleHttpResponse(response)
        }
    }

    override suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryItemDto {
        return safeApiCall {
            val response: HttpResponse = client.get("$BASE_URL/repos/$owner/$repo") {
                header("Accept", "application/vnd.github.v3+json")
            }
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
            throw e
        } catch (e: Exception) {
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
            }
        }
    }
}
