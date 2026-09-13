package jp.co.yumemi.android.codecheck.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources

/**
 * A [ContextWrapper] that preserves the underlying [Activity] context hierarchy (required by Hilt
 * ViewModel factories) while overriding [Resources] and [AssetManager] for runtime locale updates.
 */
class LocalizedContext(
    base: Context,
    private val localizedConfig: Configuration
) : ContextWrapper(base) {

    private val configContext: Context = base.createConfigurationContext(localizedConfig)

    override fun getResources(): Resources = configContext.resources

    override fun getAssets(): AssetManager = configContext.assets

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        val merged = Configuration(localizedConfig).apply {
            updateFrom(overrideConfiguration)
        }
        return LocalizedContext(baseContext, merged)
    }
}
