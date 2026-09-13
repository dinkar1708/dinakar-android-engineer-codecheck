package jp.co.yumemi.android.codecheck.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class LocalizedContextTest {

    private fun Context.findActivity(): Activity? {
        var ctx: Context? = this
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    @Test
    fun localizedContext_preservesActivityContextHierarchyForHilt() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()
        val config = Configuration(activity.resources.configuration).apply {
            setLocale(Locale.JAPANESE)
        }

        // Direct createConfigurationContext returns ContextImpl which breaks Hilt's findActivity()
        val rawConfigContext = activity.createConfigurationContext(config)
        assertNull(rawConfigContext.findActivity())

        // LocalizedContext wraps the activity so Hilt finds the activity correctly
        val localizedContext = LocalizedContext(activity, config)
        val unwrappedActivity = localizedContext.findActivity()

        assertNotNull(unwrappedActivity)
        assertEquals(activity, unwrappedActivity)
        assertNotNull(localizedContext.resources)
        assertNotNull(localizedContext.assets)
    }

    @Test
    fun localizedContext_createConfigurationContext_preservesBaseHierarchy() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()
        val config = Configuration(activity.resources.configuration).apply {
            setLocale(Locale.ENGLISH)
        }

        val localizedContext = LocalizedContext(activity, config)
        val subConfigContext = localizedContext.createConfigurationContext(Configuration(config))

        assertEquals(activity, subConfigContext.findActivity())
    }
}
