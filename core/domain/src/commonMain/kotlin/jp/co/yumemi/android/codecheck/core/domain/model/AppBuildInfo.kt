package jp.co.yumemi.android.codecheck.core.domain.model

/**
 * Metadata describing application build version, environment flavor, and debug state.
 */
data class AppBuildInfo(
    val versionName: String = "1.0",
    val versionCode: Int = 1,
    val flavor: String = "prod",
    val isDebug: Boolean = false
) {
    /**
     * Human-readable formatted version string (e.g. "1.0-dev (Build 1)").
     */
    val formattedVersion: String
        get() = "$versionName (Build $versionCode)"

    /**
     * Environment display label (e.g. "DEVELOPMENT", "OFFLINE MOCK", "STAGING", "PRODUCTION").
     */
    val environmentLabel: String
        get() = when (flavor.lowercase()) {
            "dev" -> "DEVELOPMENT"
            "mock" -> "OFFLINE MOCK"
            "stg" -> "STAGING"
            "prod" -> "PRODUCTION"
            else -> flavor.uppercase()
        }

    /**
     * API service source label.
     */
    val apiSourceLabel: String
        get() = if (flavor.lowercase() == "mock") {
            "Offline Mock Dataset (Zero Rate Limits)"
        } else {
            "GitHub REST API v3"
        }
}
