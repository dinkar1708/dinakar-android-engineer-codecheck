package jp.co.yumemi.android.codecheck.feature.detail

import java.util.Locale

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
            sizeInKb < 1024L -> "$sizeInKb KB"
            sizeInKb < 1024L * 1024L -> String.format(Locale.US, "%.1f MB", sizeInKb / 1024.0)
            else -> String.format(Locale.US, "%.1f GB", sizeInKb / (1024.0 * 1024.0))
        }
    }

    /**
     * Formats ISO-8601 push timestamp into YYYY-MM-DD format as specified in design.
     */
    fun formatPushDate(pushedAt: String?): String {
        if (pushedAt.isNullOrBlank()) return "—"
        return try {
            val datePart = pushedAt.substringBefore("T")
            if (datePart.length >= 10) datePart.substring(0, 10) else datePart
        } catch (_: Exception) {
            pushedAt
        }
    }
}
