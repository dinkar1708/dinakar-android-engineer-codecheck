package jp.co.yumemi.android.codecheck.core.designsystem.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AppDimensions] token system and form factor presets.
 */
class AppDimensionsTest {

    @Test
    fun defaultPhoneDimensions_hasStandardPhoneScale() {
        val dimensions = DefaultPhoneDimensions

        assertFalse(dimensions.isCompactLandscape)
        assertFalse(dimensions.isTablet)
        assertEquals(2, dimensions.statGridColumns)
        assertEquals(4.dp, dimensions.bottomBarItemSpacing)
        assertEquals(16.dp, dimensions.statCardPadding)
        assertEquals(6.dp, dimensions.statSpacerHeight)
        assertEquals(11.sp, dimensions.statLabelSize)
        assertEquals(24.sp, dimensions.statValueSize)
        assertEquals(40.dp, dimensions.avatarSmall)
        assertEquals(56.dp, dimensions.avatarMedium)
        assertEquals(20.dp, dimensions.screenPaddingHorizontal)
        assertEquals(24.dp, dimensions.screenPaddingVertical)
    }

    @Test
    fun compactLandscapeDimensions_hasCompactTokensAndSingleRowStatGrid() {
        val dimensions = CompactLandscapeDimensions

        assertTrue(dimensions.isCompactLandscape)
        assertFalse(dimensions.isTablet)
        assertEquals(4, dimensions.statGridColumns)
        assertEquals(0.dp, dimensions.bottomBarItemSpacing)
        assertEquals(10.dp, dimensions.statCardPadding)
        assertEquals(2.dp, dimensions.statSpacerHeight)
        assertEquals(10.sp, dimensions.statLabelSize)
        assertEquals(18.sp, dimensions.statValueSize)
        assertEquals(34.dp, dimensions.avatarSmall)
        assertEquals(42.dp, dimensions.avatarMedium)
        assertEquals(16.dp, dimensions.screenPaddingHorizontal)
        assertEquals(10.dp, dimensions.screenPaddingVertical)
    }

    @Test
    fun tabletDimensions_hasGenerousTokensAndFourColumnStatGrid() {
        val dimensions = TabletDimensions

        assertFalse(dimensions.isCompactLandscape)
        assertTrue(dimensions.isTablet)
        assertEquals(4, dimensions.statGridColumns)
        assertEquals(6.dp, dimensions.bottomBarItemSpacing)
        assertEquals(20.dp, dimensions.statCardPadding)
        assertEquals(8.dp, dimensions.statSpacerHeight)
        assertEquals(12.sp, dimensions.statLabelSize)
        assertEquals(26.sp, dimensions.statValueSize)
        assertEquals(44.dp, dimensions.avatarSmall)
        assertEquals(64.dp, dimensions.avatarMedium)
        assertEquals(24.dp, dimensions.screenPaddingHorizontal)
        assertEquals(28.dp, dimensions.screenPaddingVertical)
    }
}
