package jp.co.yumemi.android.codecheck.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════════
// 6 MAIN DESIGN PALETTE PILLARS (Common_Palette.html)
// "Six colours carry the whole system. Every other value in the screens
// is a shade, tint or opacity of one of them."
// ═══════════════════════════════════════════════════════════════════

// 1. Navy · #2d3545 (Header, dark surfaces)
val AppNavy = Color(0xFF2D3545)

// 2. Blue · #3b50df (Action, links, focus)
val AppBlue = Color(0xFF3B50DF)

// 3. Green · #2db36c (Positive, match)
val AppGreen = Color(0xFF2DB36C)

// 4. Amber · #f59e0b (Stars, review)
val AppAmber = Color(0xFFF59E0B)

// 5. White · #ffffff (Card surface, light components)
val AppWhite = Color(0xFFFFFFFF)

// 6. Slate Neutral Ramp · #0f172a → #f5f6f8 (Text, borders, dividers, background)
val Slate900 = Color(0xFF0F172A)  // Darkest neutral, primary text (#0f172a)
val Slate800 = Color(0xFF1E293B)  // High contrast text (#1e293b)
val Slate700 = Color(0xFF334155)  // Body text, secondary headings (#334155)
val Slate600 = Color(0xFF475569)  // Subtitles, owner handles, chips (#475569)
val Slate500 = Color(0xFF64748B)  // Meta counts, forks, icons (#64748b / #5b6879)
val Slate400 = Color(0xFF94A3B8)  // Muted text, placeholders (#94a3b8)
val Slate300 = Color(0xFFCBD5E1)  // Light outlines, secondary borders (#cbd5e1)
val Slate200 = Color(0xFFE2E8F0)  // Card borders, subtle dividers (#e2e8f0)
val Slate100 = Color(0xFFF1F5F9)  // Skeleton placeholders, subtle surfaces (#f1f5f9)
val Slate50 = Color(0xFFF5F6F8)   // Light mode app background (#f5f6f8)

// ═══════════════════════════════════════════════════════════════════
// APPROVED DERIVED TINTS & OVERLAYS (Common_Palette.html)
// ═══════════════════════════════════════════════════════════════════
val MonogramGreenBg = AppGreen.copy(alpha = 0.07f) // Green monogram tint at 7% on white
val ScrimOverlay = AppNavy.copy(alpha = 0.60f)     // Bottom sheet scrim overlay (60%)

// ═══════════════════════════════════════════════════════════════════
// DARK THEME PALETTE & INVERTED SURFACES (Common_Palette.html)
// "Same structure, inverted surfaces. Dark theme shades navy down for
// the app background and lifts blue for contrast."
// ═══════════════════════════════════════════════════════════════════
val DarkAppBackground = Color(0xFF12161F)  // Dark page background (#12161f)
val DarkCardSurface = Color(0xFF1F2634)    // Card surface (#1f2634)
val DarkBorder = Color(0xFF333C4E)         // Border (#333c4e)
val DarkDivider = Color(0xFF2B3344)        // Divider between rows (#2b3344)
val DarkActionBlue = Color(0xFF8F9DF5)     // Active icons and text (#8f9df5)
val DarkActionBorder = Color(0xFF6B7CF0)   // Active border and chip accent (#6b7cf0)
val DarkIconTileBg = Color(0xFF2A3350)     // Section icon tile and monogram tint (#2a3350)
val DarkAmberBg = Color(0xFF2E2716)        // Amber badge background in dark mode (#2e2716)
val DarkAmberIcon = Color(0xFFFBBF24)      // Amber icon and stars in dark mode (#fbbf24)

// Text tokens for dark mode
val TextPrimaryDark = Color(0xFFF1F5F9)    // Primary text / values (#f1f5f9)
val TextSecondaryDark = Color(0xFF8C99AB)  // Secondary text / subtitles (#8c99ab)
val TextBodyDark = Color(0xFFA9B4C4)       // Description / body text (#a9b4c4)
