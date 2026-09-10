package jp.co.yumemi.android.codecheck.core.data.util

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class RetryUtilTest {

    @Test
    fun retryWithExponentialBackoff_succeedsImmediately() = runTest {
        var attempts = 0
        val result = retryWithExponentialBackoff(maxAttempts = 3, initialDelayMs = 1L) {
            attempts++
            "success"
        }
        assertEquals("success", result)
        assertEquals(1, attempts)
    }

    @Test
    fun retryWithExponentialBackoff_retriesAndSucceeds() = runTest {
        var attempts = 0
        val result = retryWithExponentialBackoff(maxAttempts = 3, initialDelayMs = 1L) {
            attempts++
            if (attempts < 2) throw IOException("Transient error")
            "recovered"
        }
        assertEquals("recovered", result)
        assertEquals(2, attempts)
    }

    @Test
    fun retryWithExponentialBackoff_exceedsMaxAttempts_throwsException() = runTest {
        var attempts = 0
        try {
            retryWithExponentialBackoff<String>(maxAttempts = 3, initialDelayMs = 1L) {
                attempts++
                throw IOException("Persistent error")
            }
            fail("Expected IOException")
        } catch (e: IOException) {
            assertEquals("Persistent error", e.message)
            assertEquals(3, attempts)
        }
    }

    @Test
    fun retryWithExponentialBackoff_nonRetryableError_throwsImmediately() = runTest {
        var attempts = 0
        try {
            retryWithExponentialBackoff<String>(
                maxAttempts = 3,
                initialDelayMs = 1L,
                shouldRetry = { it is IOException }
            ) {
                attempts++
                throw IllegalArgumentException("Fatal error")
            }
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertEquals("Fatal error", e.message)
            assertEquals(1, attempts)
        }
    }
}
