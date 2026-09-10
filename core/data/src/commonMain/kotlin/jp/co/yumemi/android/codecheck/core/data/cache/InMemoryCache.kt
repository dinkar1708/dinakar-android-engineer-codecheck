package jp.co.yumemi.android.codecheck.core.data.cache

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe, multiplatform in-memory cache with capacity limits and time-to-live (TTL) expiration.
 * Utilizes pure Kotlin monotonic time source.
 */
class InMemoryCache<K, V>(
    private val maxCapacity: Int = 50,
    private val ttl: Duration = 5.minutes,
    private val timeSource: TimeSource = TimeSource.Monotonic
) {
    private data class CacheEntry<V>(
        val value: V,
        val createdAt: TimeMark
    )

    private val mutex = Mutex()
    private val storage = LinkedHashMap<K, CacheEntry<V>>()

    /**
     * Retrieve an entry if present and not expired.
     */
    suspend fun get(key: K): V? = mutex.withLock {
        val entry = storage[key] ?: return null
        if (entry.createdAt.elapsedNow() > ttl) {
            storage.remove(key)
            return null
        }
        return entry.value
    }

    /**
     * Store an entry, evicting the oldest element if capacity is exceeded.
     */
    suspend fun put(key: K, value: V) = mutex.withLock {
        if (storage.size >= maxCapacity && !storage.containsKey(key)) {
            val oldestKey = storage.keys.firstOrNull()
            if (oldestKey != null) {
                storage.remove(oldestKey)
            }
        }
        storage[key] = CacheEntry(value, timeSource.markNow())
    }

    /**
     * Clear all cached entries.
     */
    suspend fun clear() = mutex.withLock {
        storage.clear()
    }
}
