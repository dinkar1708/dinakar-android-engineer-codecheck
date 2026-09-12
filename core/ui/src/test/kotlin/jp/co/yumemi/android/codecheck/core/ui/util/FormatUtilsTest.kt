package jp.co.yumemi.android.codecheck.core.ui.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatUtilsTest {

    @Test
    fun formatCount_smallNumbers_returnsExactString() {
        assertEquals("0", formatCount(0L))
        assertEquals("42", formatCount(42L))
        assertEquals("999", formatCount(999L))
    }

    @Test
    fun formatCount_thousands_returnsCompactK() {
        assertEquals("1.0K", formatCount(1000L))
        assertEquals("1.2K", formatCount(1200L))
        assertEquals("14.2K", formatCount(14200L))
        assertEquals("999.9K", formatCount(999900L))
    }

    @Test
    fun formatCount_millions_returnsCompactM() {
        assertEquals("1.0M", formatCount(1000000L))
        assertEquals("3.5M", formatCount(3500000L))
    }

    @Test
    fun formatStarCount_matchesFormatCount() {
        assertEquals("14.2K", formatStarCount(14200L))
    }

    @Test
    fun formatForkCount_matchesFormatCount() {
        assertEquals("3.5K", formatForkCount(3500L))
    }
}
