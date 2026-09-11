package jp.co.yumemi.android.codecheck.core.network.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.IOException

class NetworkExceptionTest {

    @Test
    fun rateLimitExceededException_hasExpectedMessage() {
        val defaultEx = NetworkException.RateLimitExceededException()
        assertEquals("GitHub API rate limit exceeded (HTTP 403)", defaultEx.message)

        val customEx = NetworkException.RateLimitExceededException("Custom 403")
        assertEquals("Custom 403", customEx.message)
    }

    @Test
    fun notFoundException_hasExpectedMessage() {
        val defaultEx = NetworkException.NotFoundException()
        assertEquals("Requested resource was not found (HTTP 404)", defaultEx.message)

        val customEx = NetworkException.NotFoundException("Custom 404")
        assertEquals("Custom 404", customEx.message)
    }

    @Test
    fun serverException_containsStatusCodeAndMessage() {
        val ex = NetworkException.ServerException(500)
        assertEquals(500, ex.statusCode)
        assertEquals("GitHub server error (HTTP 500)", ex.message)
    }

    @Test
    fun noConnectivityException_preservesCause() {
        val cause = IOException("Network down")
        val ex = NetworkException.NoConnectivityException(cause = cause)
        assertEquals("No network connection available", ex.message)
        assertEquals(cause, ex.cause)
    }

    @Test
    fun unknownNetworkException_preservesCause() {
        val cause = RuntimeException("Crash")
        val ex = NetworkException.UnknownNetworkException(cause = cause)
        assertEquals("Unexpected network failure", ex.message)
        assertEquals(cause, ex.cause)
    }
}
