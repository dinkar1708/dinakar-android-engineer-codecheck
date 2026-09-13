package jp.co.yumemi.android.codecheck.feature.detail

import java.util.Locale

private const val KB_PER_MB = 1024L
private const val KB_PER_MB_DOUBLE = 1024.0
private const val KB_PER_GB = 1024L * 1024L
private const val KB_PER_GB_DOUBLE = 1024.0 * 1024.0
private const val DATE_PREFIX_LENGTH = 10

/**
 * Pure formatters for repository detail screen metadata.
 */
object DetailFormatters {

    /**
     * Formats repository size in KB into a human-readable string (KB, MB, GB).
     * GitHub API reports size in kilobytes.
     */
    fun formatSize(sizeInKb: Long): String {
        return when {
            sizeInKb <= 0L -> "0 KB"
            sizeInKb < KB_PER_MB -> "$sizeInKb KB"
            sizeInKb < KB_PER_GB -> String.format(Locale.US, "%.1f MB", sizeInKb / KB_PER_MB_DOUBLE)
            else -> String.format(Locale.US, "%.1f GB", sizeInKb / KB_PER_GB_DOUBLE)
        }
    }

    /**
     * Formats ISO-8601 push timestamp into YYYY-MM-DD format as specified in design.
     */
    fun formatPushDate(pushedAt: String?): String {
        if (pushedAt.isNullOrBlank()) return "—"
        return try {
            val datePart = pushedAt.substringBefore("T")
            if (datePart.length >= DATE_PREFIX_LENGTH) datePart.substring(0, DATE_PREFIX_LENGTH) else datePart
        } catch (_: Exception) {
            pushedAt
        }
    }
}
