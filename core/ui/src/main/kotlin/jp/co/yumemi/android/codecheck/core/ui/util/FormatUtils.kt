package jp.co.yumemi.android.codecheck.core.ui.util

import java.util.Locale

/**
 * Formats large numeric counts into human-readable compact representations (e.g. 1.2K, 3.5M).
 */
fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format(Locale.US, "%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format(Locale.US, "%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

/**
 * Formats repository star counts into compact representations.
 */
fun formatStarCount(count: Long): String = formatCount(count)

/**
 * Formats repository fork counts into compact representations.
 */
fun formatForkCount(count: Long): String = formatCount(count)
