package jp.co.yumemi.android.codecheck.feature.detail

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DetailFormatters].
 */
class DetailFormattersTest {

    @Test
    fun formatSize_returnsExpectedValues() {
        assertEquals("0 KB", DetailFormatters.formatSize(0L))
        assertEquals("0 KB", DetailFormatters.formatSize(-100L))
        assertEquals("512 KB", DetailFormatters.formatSize(512L))
        assertEquals("1023 KB", DetailFormatters.formatSize(1023L))
        assertEquals("1.0 MB", DetailFormatters.formatSize(1024L))
        assertEquals("4.2 MB", DetailFormatters.formatSize(4300L))
        assertEquals("1.0 GB", DetailFormatters.formatSize(1024L * 1024L))
        assertEquals("2.5 GB", DetailFormatters.formatSize((2.5 * 1024 * 1024).toLong()))
    }

    @Test
    fun formatPushDate_returnsFormattedIsoDate() {
        assertEquals("2026-09-02", DetailFormatters.formatPushDate("2026-09-02T14:32:00Z"))
        assertEquals("2026-09-02", DetailFormatters.formatPushDate("2026-09-02"))
        assertEquals("—", DetailFormatters.formatPushDate(null))
        assertEquals("—", DetailFormatters.formatPushDate(""))
        assertEquals("—", DetailFormatters.formatPushDate("   "))
    }
}
