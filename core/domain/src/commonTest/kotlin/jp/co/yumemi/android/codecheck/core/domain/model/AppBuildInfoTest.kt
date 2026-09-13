package jp.co.yumemi.android.codecheck.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AppBuildInfoTest {

    @Test
    fun formattedVersion_formatsVersionNameAndCode() {
        val buildInfo = AppBuildInfo(
            versionName = "1.0-dev",
            versionCode = 42,
            flavor = "dev"
        )
        assertEquals("1.0-dev (Build 42)", buildInfo.formattedVersion)
    }

    @Test
    fun environmentLabel_mapsFlavorsCorrectly() {
        assertEquals("DEVELOPMENT", AppBuildInfo(flavor = "dev").environmentLabel)
        assertEquals("DEVELOPMENT", AppBuildInfo(flavor = "DEV").environmentLabel)
        assertEquals("OFFLINE MOCK", AppBuildInfo(flavor = "mock").environmentLabel)
        assertEquals("STAGING", AppBuildInfo(flavor = "stg").environmentLabel)
        assertEquals("PRODUCTION", AppBuildInfo(flavor = "prod").environmentLabel)
        assertEquals("CUSTOM", AppBuildInfo(flavor = "custom").environmentLabel)
    }

    @Test
    fun apiSourceLabel_identifiesMockAndRestApi() {
        assertEquals(
            "Offline Mock Dataset (Zero Rate Limits)",
            AppBuildInfo(flavor = "mock").apiSourceLabel
        )
        assertEquals(
            "GitHub REST API v3",
            AppBuildInfo(flavor = "dev").apiSourceLabel
        )
        assertEquals(
            "GitHub REST API v3",
            AppBuildInfo(flavor = "prod").apiSourceLabel
        )
    }
}
