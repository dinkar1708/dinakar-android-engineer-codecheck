package jp.co.yumemi.android.codecheck.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════════
// 6 MAIN DESIGN PALETTE COLORS
// All colors across the app are one of these or a shade/tint/opacity.
// ═══════════════════════════════════════════════════════════════════
val AppNavy = Color(0xFF2D3545)   // Header, dark surfaces
val AppBlue = Color(0xFF3B50DF)   // Action, links, focus
val AppGreen = Color(0xFF2DB36C)  // Positive, match
val AppAmber = Color(0xFFF59E0B)  // Stars, review
val AppWhite = Color(0xFFFFFFFF)  // Card surface, light components

// ═══════════════════════════════════════════════════════════════════
// SLATE NEUTRAL RAMP (#0f172a -> #f5f6f8)
// Used for typography, borders, dividers, and screen backgrounds.
// ═══════════════════════════════════════════════════════════════════
val Slate900 = Color(0xFF0F172A)  // Darkest neutral, primary text
val Slate800 = Color(0xFF1E293B)  // High contrast text
val Slate700 = Color(0xFF334155)  // Body text, secondary headings
val Slate600 = Color(0xFF475569)  // Subtitles, owner handles
val Slate500 = Color(0xFF64748B)  // Meta counts, forks, icons
val Slate400 = Color(0xFF94A3B8)  // Muted text, placeholders
val Slate300 = Color(0xFFCBD5E1)  // Light outlines, secondary borders
val Slate200 = Color(0xFFE2E8F0)  // Card borders, subtle dividers
val Slate100 = Color(0xFFF1F5F9)  // Skeleton placeholders, subtle surfaces
val Slate50 = Color(0xFFF5F6F8)   // Light mode app background

// ═══════════════════════════════════════════════════════════════════
// DERIVED TINTS & INTERACTIONS
// ═══════════════════════════════════════════════════════════════════
// Monogram tints: Blue, Green, or Slate at 7% on white
val MonogramBlueBg = AppBlue.copy(alpha = 0.07f)
val MonogramBlueText = AppBlue
val MonogramGreenBg = AppGreen.copy(alpha = 0.07f)
val MonogramGreenText = AppGreen
val MonogramSlateBg = Slate500.copy(alpha = 0.07f)
val MonogramSlateText = Slate600

// Interaction states
val HoverTint = Slate900.copy(alpha = 0.04f)
val ButtonHoverBlue = Color(0xFF2E3EB8) // Blue darkened one step
val SelectedBlueBg = AppBlue.copy(alpha = 0.09f) // Selected/active state background (9%)

// Modal & overlay states
val ScrimOverlay = AppNavy.copy(alpha = 0.60f) // Bottom sheet scrim (60%)

// ═══════════════════════════════════════════════════════════════════
// DARK THEME SPECIFICATION
// Per design spec: light and dark mode currently use the exact same color palette.
// Custom dark theme palette will be configured in a future update.
// ═══════════════════════════════════════════════════════════════════
val DarkAppBackground = Slate50          // Uses same background as light mode for now
val DarkCardSurface = AppWhite           // Uses same card surface as light mode for now
val DarkBorder = Slate200                // Uses same border as light mode for now
val DarkActionBlue = AppBlue             // Uses same action blue as light mode for now
val DarkGreen = AppGreen                  // Positive, match (unchanged)
val DarkAmber = AppAmber                  // Stars, review (unchanged)

// Text tokens for dark mode (matching light mode palette for now)
val TextPrimaryDark = Slate900
val TextSecondaryDark = Slate600

// ═══════════════════════════════════════════════════════════════════
// SEMANTIC CONVENIENCE TOKENS
// Aliases keeping existing call sites clean without introducing new colors.
// ═══════════════════════════════════════════════════════════════════
val StarGold = AppAmber
val StarAmber = AppAmber
val LanguageBadge = AppBlue
val ForkNeutral = Slate500
