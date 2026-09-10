package jp.co.yumemi.android.codecheck.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

/**
 * iOS actual implementation using Ktor Darwin engine.
 */
actual fun createHttpClientEngine(): HttpClientEngine = Darwin.create()
