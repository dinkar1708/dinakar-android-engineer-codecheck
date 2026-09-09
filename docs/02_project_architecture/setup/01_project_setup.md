# Development Guide: Project Setup & Workflow

## 1. Prerequisites & Environment Requirements
- **macOS / Linux / Windows**
- **Android Studio**: Android Studio Hedgehog (2023.1.1) or newer (Iguana, Jellyfish, Koala, Ladybug)
- **Java Development Kit (JDK)**: JDK 17 or JDK 21 (bundled JetBrains Runtime `jbr-21` recommended)
- **Android SDK**: API 34 (UpsideDownCake) compile SDK, Min SDK 24 (Android 7.0)
- **Command Line**: Terminal with `git` and `curl`

---

## 2. Daily Development Commands

```bash
# Clean project
./gradlew clean

# Build shared Kotlin Multiplatform module
./gradlew :shared:assemble

# Run all unit tests (:shared and :app)
./gradlew testDebugUnitTest

# Assemble Debug APK
./gradlew assembleDebug

# Output APK path:
# app/build/outputs/apk/debug/app-debug.apk

# Run full quality gate check (Lint + Unit Tests)
./gradlew lintDebug testDebugUnitTest
```

---

## 3. Recommended Android Studio Configuration
1. **Gradle JDK Selection**:
   - Navigate to **Preferences / Settings &rarr; Build, Execution, Deployment &rarr; Build Tools &rarr; Gradle**.
   - Under **Gradle JDK**, choose **Embedded JDK 17** or **jbr-21** (Java 21).
   - *Note: With Gradle 8.5, Java 21 is natively supported out of the box.*
2. **Kotlin Multiplatform Plugin**:
   - Install the **Kotlin Multiplatform** plugin from the JetBrains Plugin Marketplace to enable direct syntax highlighting and iOS simulator execution in Android Studio.
