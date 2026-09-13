package jp.co.yumemi.android.codecheck.core.network.error

/**
 * Domain-mapped exceptions thrown by the networking layer.
 */
sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class RateLimitExceededException(message: String = "GitHub API rate limit exceeded (HTTP 403)") : NetworkException(message)
    class NotFoundException(message: String = "Requested resource was not found (HTTP 404)") : NetworkException(message)
    class ServerException(val statusCode: Int, message: String = "GitHub server error (HTTP $statusCode)") : NetworkException(message)
    class NoConnectivityException(message: String = "No network connection available", cause: Throwable? = null) : NetworkException(message, cause)
    class UnknownNetworkException(message: String = "Unexpected network failure", cause: Throwable? = null) : NetworkException(message, cause)
}
