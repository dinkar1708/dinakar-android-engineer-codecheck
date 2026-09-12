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
