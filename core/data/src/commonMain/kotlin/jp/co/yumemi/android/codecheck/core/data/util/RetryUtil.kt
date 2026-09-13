package jp.co.yumemi.android.codecheck.core.data.util

import kotlin.math.min
import kotlin.random.Random
import kotlinx.coroutines.delay

/**
 * Resilient retry utility employing exponential backoff with full jitter.
 * Prevents thundering herd problems on GitHub API endpoints.
 */
suspend fun <T> retryWithExponentialBackoff(
    maxAttempts: Int = 3,
    initialDelayMs: Long = 300L,
    maxDelayMs: Long = 2000L,
    factor: Double = 2.0,
    shouldRetry: (Throwable) -> Boolean = { true },
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMs
    for (attempt in 1..maxAttempts) {
        try {
            return block()
        } catch (e: Throwable) {
            if (attempt == maxAttempts || !shouldRetry(e)) {
                throw e
            }
            val jitteredDelay = Random.nextLong(1, currentDelay + 1)
            delay(jitteredDelay)
            currentDelay = min((currentDelay * factor).toLong(), maxDelayMs)
        }
    }
    error("Unreachable: retry loop terminated unexpectedly")
}
