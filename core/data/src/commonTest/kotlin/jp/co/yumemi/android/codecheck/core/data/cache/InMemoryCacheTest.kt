package jp.co.yumemi.android.codecheck.core.data.cache

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InMemoryCacheTest {

    @Test
    fun get_whenEmpty_returnsNull() = runTest {
        val cache = InMemoryCache<String, String>()
        assertNull(cache.get("nonexistent"))
    }

    @Test
    fun put_and_get_storesAndRetrievesValue() = runTest {
        val cache = InMemoryCache<String, String>()
        cache.put("key1", "value1")

        assertEquals("value1", cache.get("key1"))
    }

    @Test
    fun put_exceedingCapacity_evictsOldestEntry() = runTest {
        val cache = InMemoryCache<String, String>(maxCapacity = 2)

        cache.put("a", "1")
        cache.put("b", "2")
        // Exceed capacity
        cache.put("c", "3")

        // 'a' was oldest, so it should be evicted
        assertNull(cache.get("a"))
        assertEquals("2", cache.get("b"))
        assertEquals("3", cache.get("c"))
    }

    @Test
    fun clear_removesAllEntries() = runTest {
        val cache = InMemoryCache<String, String>()
        cache.put("a", "1")
        cache.put("b", "2")

        cache.clear()

        assertNull(cache.get("a"))
        assertNull(cache.get("b"))
    }
}
