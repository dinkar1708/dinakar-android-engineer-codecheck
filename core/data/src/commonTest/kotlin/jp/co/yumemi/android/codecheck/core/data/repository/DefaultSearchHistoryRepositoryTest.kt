package jp.co.yumemi.android.codecheck.core.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultSearchHistoryRepositoryTest {

    @Test
    fun getSearchHistory_initiallyEmpty() = runTest {
        val repository = DefaultSearchHistoryRepository(maxItems = 7)
        val history = repository.getSearchHistory().first()
        assertTrue(history.isEmpty())
    }

    @Test
    fun addSearchQuery_addsInMruOrder() = runTest {
        val repository = DefaultSearchHistoryRepository(maxItems = 7)

        repository.addSearchQuery("kotlin")
        repository.addSearchQuery("android")
        repository.addSearchQuery("compose")

        val history = repository.getSearchHistory().first()
        assertEquals(listOf("compose", "android", "kotlin"), history)
    }

    @Test
    fun addSearchQuery_duplicatePromotesToTop() = runTest {
        val repository = DefaultSearchHistoryRepository(maxItems = 7)

        repository.addSearchQuery("kotlin")
        repository.addSearchQuery("android")
        repository.addSearchQuery("kotlin")

        val history = repository.getSearchHistory().first()
        assertEquals(listOf("kotlin", "android"), history)
    }

    @Test
    fun addSearchQuery_enforcesMaxCapacityOfSeven() = runTest {
        val repository = DefaultSearchHistoryRepository(maxItems = 7)

        for (i in 1..10) {
            repository.addSearchQuery("query-$i")
        }

        val history = repository.getSearchHistory().first()
        assertEquals(7, history.size)
        // Most recent 7: query-10 down to query-4
        assertEquals(listOf("query-10", "query-9", "query-8", "query-7", "query-6", "query-5", "query-4"), history)
    }

    @Test
    fun removeSearchQuery_removesSpecificItem() = runTest {
        val repository = DefaultSearchHistoryRepository(maxItems = 7)

        repository.addSearchQuery("kotlin")
        repository.addSearchQuery("android")
        repository.removeSearchQuery("kotlin")

        val history = repository.getSearchHistory().first()
        assertEquals(listOf("android"), history)
    }

    @Test
    fun clearSearchHistory_clearsAllItems() = runTest {
        val repository = DefaultSearchHistoryRepository(maxItems = 7)

        repository.addSearchQuery("kotlin")
        repository.addSearchQuery("android")
        repository.clearSearchHistory()

        val history = repository.getSearchHistory().first()
        assertTrue(history.isEmpty())
    }
}
