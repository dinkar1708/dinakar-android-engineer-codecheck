# Feature Specification: Animated Splash Screen

## 1. Overview & Visual Progression
The **Animated Splash Screen** is displayed for 1.8 seconds upon cold app launch. It establishes application branding, highlights core technical objectives (GitHub discovery, exploration, and stargazing), and smoothly transitions into the `MainScreen` without lingering or causing perceived latency.

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Splash as SplashScreen (Compose)
    participant Nav as AppNavHost
    participant Main as MainScreen (Search Tab)

    User->>Splash: Cold Launch Application
    Splash->>Splash: Bouncy Spring Scale (0.5 -> 1.0) & Alpha Fade (0 -> 1)
    Note over Splash: 1,800ms duration (ambient gradient + code emblem)
    Splash->>Nav: onSplashFinished()
    Nav->>Main: navigate("main") with popUpTo("splash", inclusive = true)
    Main-->>User: Search / Home tab active
```

---

## 2. Visual Elements & Design Tokens

### 1. Central Brand Emblem:
- Circular `Surface` container (96dp diameter) with `MaterialTheme.colorScheme.primaryContainer` background and 8dp tonal elevation.
- Centered `Icons.Default.Code` icon styled with `MaterialTheme.colorScheme.primary`.
- Entrance animation driven by Compose `Animatable` with bouncy spring specs (`Spring.DampingRatioMediumBouncy`, `Spring.StiffnessLow`).

### 2. Typography & Branding:
- **Title**: `stringResource(R.string.app_name)` rendered with `headlineMedium` typography and bold font weight.
- **Tagline**: `stringResource(R.string.splash_tagline)`:
  - English: *"Discover • Explore • Stargaze"*
  - Japanese: *"発見 • 探求 • スター"*

### 3. Engineering Attribution Footer:
- Styled pill container displaying:
  - `Clean Architecture • Kotlin Multiplatform • Jetpack Compose`

---

## 3. Navigation & Lifecycle Protection
To ensure the back button does not inadvertently return to the splash screen:
```kotlin
composable("splash") {
    SplashScreen(
        onSplashFinished = {
            navController.navigate("main") {
                popUpTo("splash") { inclusive = true }
            }
        }
    )
}
```
`popUpTo("splash") { inclusive = true }` clears the splash screen completely from the navigation backstack, ensuring the user exits the app naturally upon pressing Back from the home search screen.
