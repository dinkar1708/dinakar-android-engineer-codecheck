# Development Guide: Diagnostic Troubleshooting Guide

This guide details the 7 critical build and runtime pitfalls encountered across different workstations and modern toolchains, along with their exact root causes and enterprise resolutions.

---

## 1. Incompatible Gradle JVM Version (Java 21 vs Gradle 8.0)
- **Symptom**: Android Studio fails during Gradle sync with:
  ```text
  The project's Gradle version 8.0 is incompatible with the Gradle JVM version 21 currently selected to run Gradle build.
  ```
- **Root Cause**: Gradle 8.0 only supported Java up to version 19. Modern Android Studio bundles Java 21 (`jbr-21`).
- **Resolution**:
  - Upgrade Gradle wrapper to **8.5** in `gradle/wrapper/gradle-wrapper.properties`:
    ```properties
    distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
    ```
  - Upgrade Android Gradle Plugin to **8.2.2** in root `build.gradle`:
    ```groovy
    classpath 'com.android.tools.build:gradle:8.2.2'
    ```

---

## 2. Linux CI Runners Case-Sensitivity
- **Symptom**: Local builds succeed on macOS, but GitHub Actions Linux runners fail to resolve `topActivity.kt`.
- **Root Cause**: macOS APFS filesystem is case-insensitive, while Linux filesystems (`ext4`) are case-sensitive.
- **Resolution**: Standardize filenames to match Kotlin class names:
  ```bash
  git mv app/src/main/kotlin/.../topActivity.kt app/src/main/kotlin/.../TopActivity.kt
  ```

---

## 3. Ktor 403 Rate Limit & Silent Empty Lists
- **Symptom**: When API rate limit is exceeded, app displays an empty list ("No repositories found") instead of an error state.
- **Root Cause**: Ktor client without response status validation parses GitHub's 403 error JSON into fallback DTO defaults (`items = emptyList()`).
- **Resolution**: Check `response.status.isSuccess()` in `GitHubApiService`:
  ```kotlin
  if (!response.status.isSuccess()) {
      throw Exception("GitHub API returned HTTP ${response.status.value}: ${response.status.description}")
  }
  ```

---

## 4. Cross-Module Kotlin Smart Cast Failures
- **Symptom**: Compilation fails with:
  ```text
  e: Smart cast to 'String' is impossible, because 'item.language' is a public API property declared in different module
  ```
- **Root Cause**: Properties defined in external modules (`:shared`) cannot be smart-cast directly in consuming modules (`:app`) because the compiler cannot guarantee runtime mutability across module boundaries.
- **Resolution**: Capture into a local immutable variable before checking:
  ```kotlin
  val repoLanguage = item.language
  if (!repoLanguage.isNullOrBlank()) {
      Text(text = repoLanguage)
  }
  ```

---

## 5. Compose Theme Inheritance & XML Inflation
- **Symptom**: App crashes on launch with theme inflation error when transitioning to 100% Jetpack Compose.
- **Root Cause**: Legacy XML themes inheriting from `Theme.MaterialComponents.DayNight.DarkActionBar` require ActionBar action items.
- **Resolution**: Inherit from `Theme.Material3.DayNight.NoActionBar` in `res/values/themes.xml` and `res/values-night/themes.xml`.

---

## 6. Android Lint Typography Ellipsis (`&#8230;`)
- **Symptom**: `./gradlew lintDebug` issues warnings for string resources ending with three dots `...`.
- **Resolution**: Use the typography character entity `&#8230;` in all XML resource files.

---

## 7. Strict Non-Automated Git Commit Policy
- **Directive**: Autonomous AI agents must never execute `git commit` or `git push` automatically.
- **Resolution**: All changes are left uncommitted in the working tree for developer review, with formatted terminal commands provided as text.
