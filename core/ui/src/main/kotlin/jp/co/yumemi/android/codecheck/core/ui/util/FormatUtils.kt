package jp.co.yumemi.android.codecheck.core.ui.util

import java.util.Locale

private const val ONE_MILLION = 1_000_000L
private const val ONE_MILLION_DOUBLE = 1_000_000.0
private const val ONE_THOUSAND = 1_000L
private const val ONE_THOUSAND_DOUBLE = 1_000.0

/**
 * Formats large numeric counts into human-readable compact representations (e.g. 1.2K, 3.5M).
 */
fun formatCount(count: Long): String {
    return when {
        count >= ONE_MILLION -> String.format(Locale.US, "%.1fM", count / ONE_MILLION_DOUBLE)
        count >= ONE_THOUSAND -> String.format(Locale.US, "%.1fK", count / ONE_THOUSAND_DOUBLE)
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

/**
 * Formats a count with decimal grouping commas (e.g. 10695 -> "10,695").
 */
fun formatDecimalNumber(count: Long): String {
    return java.text.NumberFormat.getNumberInstance(Locale.US).format(count)
}

/**
 * Derives a 2-character monogram string from an owner handle or name.
 */
fun getMonogramInitials(name: String): String {
    val clean = name.trim()
    if (clean.isEmpty()) return "?"

    val parts = clean.split(Regex("[\\s._-]+")).filter { it.isNotEmpty() }
    if (parts.size >= 2) {
        return "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
    }

    val upperIndices = clean.indices.filter { it > 0 && clean[it].isUpperCase() }
    if (upperIndices.isNotEmpty()) {
        return "${clean.first().uppercaseChar()}${clean[upperIndices.first()].uppercaseChar()}"
    }

    return clean.take(2).uppercase()
}
