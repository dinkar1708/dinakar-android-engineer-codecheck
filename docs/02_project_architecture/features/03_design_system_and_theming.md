# Feature Specification: Design System & Material 3 Theming

## 1. Overview
The application utilizes a **100% Jetpack Compose Material 3** design system. All design tokens (Colors, Typography, Shapes, Spacing) are strictly decoupled from legacy Android XML views, ensuring instantaneous theme switching, dynamic color capability, and accessibility compliance.

---

## 2. Color Palette & Semantic Tokens

### Core Brand & Theme Colors (`Color.kt`):
```kotlin
val PrimaryBlue = Color(0xFF1976D2)
val PrimaryBlueDark = Color(0xFF90CAF9)
val SecondaryTeal = Color(0xFF009688)
val StarYellow = Color(0xFFFFB800)
val ForkBlue = Color(0xFF0288D1)

val BackgroundLight = Color(0xFFF8F9FA)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F3F5)

val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)
val SurfaceVariantDark = Color(0xFF2C2C2C)
```

### Color Schemes (`Theme.kt`):
- **Light Color Scheme**: `lightColorScheme(primary = PrimaryBlue, background = BackgroundLight, surface = SurfaceLight, surfaceVariant = SurfaceVariantLight)`
- **Dark Color Scheme**: `darkColorScheme(primary = PrimaryBlueDark, background = BackgroundDark, surface = SurfaceDark, surfaceVariant = SurfaceVariantDark)`
- **System Integration**: Dynamic color adapts to Android 12+ wallpaper palettes via `dynamicLightColorScheme(context)` and `dynamicDarkColorScheme(context)`.

---

## 3. Spacing Grid System

To avoid arbitrary hardcoded margin and padding values, a typed `Spacing` class is provided via `CompositionLocal`:

```kotlin
data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
```

### Usage in Composables:
```kotlin
val spacing = LocalSpacing.current
Column(modifier = Modifier.padding(spacing.medium)) {
    Spacer(modifier = Modifier.height(spacing.small))
}
```

---

## 4. Typography Scale (`Type.kt`)
Typography conforms to the official Material 3 scale:
- `titleLarge`: Screen headlines (22sp, SemiBold)
- `titleMedium`: Repository item titles (16sp, Medium)
- `bodyLarge`: Primary reading text (16sp, Normal)
- `bodyMedium`: Secondary metric text (14sp, Normal)
- `labelMedium`: Language chip text (12sp, Medium)

---

## 5. Related Design Documentation
- [UI/UX Design Specification & Architecture](../design/ui_ux_design_specification.md)
- [Interactive HTML Design Prototypes (`design/html/`)](../design/html/README.md)
- [AI-Assisted Design-to-Code Workflow](../design/ai_assisted_design_workflow.md)
