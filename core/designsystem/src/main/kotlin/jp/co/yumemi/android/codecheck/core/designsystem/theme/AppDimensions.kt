package jp.co.yumemi.android.codecheck.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Design system dimensional tokens supporting Phone Portrait, Phone Landscape, and Tablet.
 * Centralizes all spacing, sizing, and responsive micro-adjustments into a unified design token system.
 */
data class AppDimensions(
    // Spacing
    val spaceExtraSmall: Dp,
    val spaceSmall: Dp,
    val spaceMedium: Dp,
    val spaceLarge: Dp,
    val spaceExtraLarge: Dp,

    // Layout Padding
    val screenPaddingHorizontal: Dp,
    val screenPaddingVertical: Dp,
    val cardPadding: Dp,
    val itemSpacing: Dp,

    // Component Sizes
    val avatarSmall: Dp,
    val avatarMedium: Dp,
    val avatarLarge: Dp,
    val iconSmall: Dp,
    val iconMedium: Dp,

    // Bottom Navigation Bar Tokens
    val bottomBarVerticalPadding: Dp,
    val bottomBarLabelSize: TextUnit,
    val bottomBarPillHorizontal: Dp,
    val bottomBarPillVertical: Dp,
    val bottomBarItemSpacing: Dp,

    // Typography
    val detailTitleSize: TextUnit,
    val detailTitleLineHeight: TextUnit,
    val bodySize: TextUnit,
    val bodyLineHeight: TextUnit,
    val captionSize: TextUnit,
    val captionLineHeight: TextUnit,

    // Stat Card Tokens
    val statCardPadding: Dp,
    val statLabelSize: TextUnit,
    val statValueSize: TextUnit,
    val statSpacerHeight: Dp,
    val statGridColumns: Int,

    // Form Factor Indicators
    val isCompactLandscape: Boolean = false,
    val isTablet: Boolean = false
)

val DefaultPhoneDimensions = AppDimensions(
    spaceExtraSmall = 4.dp,
    spaceSmall = 6.dp,
    spaceMedium = 10.dp,
    spaceLarge = 14.dp,
    spaceExtraLarge = 20.dp,
    screenPaddingHorizontal = 20.dp,
    screenPaddingVertical = 24.dp,
    cardPadding = 16.dp,
    itemSpacing = 14.dp,
    avatarSmall = 40.dp,
    avatarMedium = 56.dp,
    avatarLarge = 64.dp,
    iconSmall = 14.dp,
    iconMedium = 20.dp,
    bottomBarVerticalPadding = 10.dp,
    bottomBarLabelSize = 12.sp,
    bottomBarPillHorizontal = 20.dp,
    bottomBarPillVertical = 5.dp,
    bottomBarItemSpacing = 4.dp,
    detailTitleSize = 24.sp,
    detailTitleLineHeight = 28.sp,
    bodySize = 14.sp,
    bodyLineHeight = 20.sp,
    captionSize = 13.sp,
    captionLineHeight = 18.sp,
    statCardPadding = 16.dp,
    statLabelSize = 11.sp,
    statValueSize = 24.sp,
    statSpacerHeight = 6.dp,
    statGridColumns = 2,
    isCompactLandscape = false,
    isTablet = false
)

val CompactLandscapeDimensions = AppDimensions(
    spaceExtraSmall = 2.dp,
    spaceSmall = 4.dp,
    spaceMedium = 6.dp,
    spaceLarge = 10.dp,
    spaceExtraLarge = 12.dp,
    screenPaddingHorizontal = 16.dp,
    screenPaddingVertical = 10.dp,
    cardPadding = 10.dp,
    itemSpacing = 10.dp,
    avatarSmall = 34.dp,
    avatarMedium = 42.dp,
    avatarLarge = 48.dp,
    iconSmall = 12.dp,
    iconMedium = 18.dp,
    bottomBarVerticalPadding = 3.dp,
    bottomBarLabelSize = 10.sp,
    bottomBarPillHorizontal = 12.dp,
    bottomBarPillVertical = 2.dp,
    bottomBarItemSpacing = 0.dp,
    detailTitleSize = 20.sp,
    detailTitleLineHeight = 24.sp,
    bodySize = 13.sp,
    bodyLineHeight = 16.sp,
    captionSize = 12.sp,
    captionLineHeight = 16.sp,
    statCardPadding = 10.dp,
    statLabelSize = 10.sp,
    statValueSize = 18.sp,
    statSpacerHeight = 2.dp,
    statGridColumns = 4,
    isCompactLandscape = true,
    isTablet = false
)

val TabletDimensions = AppDimensions(
    spaceExtraSmall = 6.dp,
    spaceSmall = 8.dp,
    spaceMedium = 14.dp,
    spaceLarge = 20.dp,
    spaceExtraLarge = 24.dp,
    screenPaddingHorizontal = 24.dp,
    screenPaddingVertical = 28.dp,
    cardPadding = 20.dp,
    itemSpacing = 16.dp,
    avatarSmall = 44.dp,
    avatarMedium = 64.dp,
    avatarLarge = 72.dp,
    iconSmall = 16.dp,
    iconMedium = 24.dp,
    bottomBarVerticalPadding = 12.dp,
    bottomBarLabelSize = 13.sp,
    bottomBarPillHorizontal = 24.dp,
    bottomBarPillVertical = 6.dp,
    bottomBarItemSpacing = 6.dp,
    detailTitleSize = 26.sp,
    detailTitleLineHeight = 32.sp,
    bodySize = 15.sp,
    bodyLineHeight = 22.sp,
    captionSize = 14.sp,
    captionLineHeight = 20.sp,
    statCardPadding = 20.dp,
    statLabelSize = 12.sp,
    statValueSize = 26.sp,
    statSpacerHeight = 8.dp,
    statGridColumns = 4,
    isCompactLandscape = false,
    isTablet = true
)

val LocalAppDimensions = staticCompositionLocalOf { DefaultPhoneDimensions }

object AppTheme {
    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimensions.current
}
