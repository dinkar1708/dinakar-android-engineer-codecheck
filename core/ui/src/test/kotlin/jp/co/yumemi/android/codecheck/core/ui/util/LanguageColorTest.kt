package jp.co.yumemi.android.codecheck.core.ui.util

import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppGreen
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LanguageColorTest {

    @Test
    fun getLanguageColor_knownLanguages_mapsToExpectedDataColors() {
        // Blue group
        assertEquals(AppBlue, getLanguageColor("Kotlin"))
        assertEquals(AppBlue, getLanguageColor("rust"))
        assertEquals(AppBlue, getLanguageColor("Go"))
        assertEquals(AppBlue, getLanguageColor("typescript"))
        assertEquals(AppBlue, getLanguageColor("ruby"))

        // Green group
        assertEquals(AppGreen, getLanguageColor("python"))
        assertEquals(AppGreen, getLanguageColor("Shell"))
        assertEquals(AppGreen, getLanguageColor("Vue"))
        assertEquals(AppGreen, getLanguageColor("HTML"))
        assertEquals(AppGreen, getLanguageColor("php"))

        // Amber group
        assertEquals(AppAmber, getLanguageColor("Java"))
        assertEquals(AppAmber, getLanguageColor("swift"))
        assertEquals(AppAmber, getLanguageColor("C++"))
        assertEquals(AppAmber, getLanguageColor("javascript"))

        // Slate group
        assertEquals(Slate500, getLanguageColor("C"))
        assertEquals(Slate500, getLanguageColor("C#"))
        assertEquals(Slate500, getLanguageColor("css"))
    }

    @Test
    fun getLanguageColor_nullOrUnknown_doesNotCrashAndReturnsValidColor() {
        assertNotNull(getLanguageColor(null))
        assertNotNull(getLanguageColor(""))
        assertNotNull(getLanguageColor("Zig"))
        assertNotNull(getLanguageColor("Haskell"))
    }
}
