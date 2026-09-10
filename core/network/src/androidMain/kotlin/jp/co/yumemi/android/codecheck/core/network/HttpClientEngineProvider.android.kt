package jp.co.yumemi.android.codecheck.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

/**
 * Android actual implementation using Ktor Android engine.
 */
actual fun createHttpClientEngine(): HttpClientEngine = Android.create()
