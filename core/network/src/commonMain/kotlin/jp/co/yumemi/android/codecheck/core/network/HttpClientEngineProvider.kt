package jp.co.yumemi.android.codecheck.core.network

import io.ktor.client.engine.HttpClientEngine

/**
 * Multiplatform expect/actual provider for the Ktor [HttpClientEngine].
 */
expect fun createHttpClientEngine(): HttpClientEngine
