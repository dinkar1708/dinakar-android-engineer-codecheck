# Getting Started

**Project**: Android Engineer Code Check Solution
**Author**: Dinakar Prasad Maurya

---

## Prerequisites

- **JDK**: 17 or 21
- **Android Studio**: Iguana (2023.2.1) or newer
- **Gradle**: 8.5 (via wrapper)

---

## Quick Build Commands

```bash
# Clean and build debug APK
./gradlew clean assembleDebug

# Run unit tests
./gradlew test

# Run static analysis (when added)
./gradlew detekt
```

---

## Project Structure

```
app/                    - Main application module
docs/                   - Documentation
gradle/                 - Gradle wrapper
.github/                - GitHub Actions workflows
```

---

## Build Verification

```bash
# Verify Gradle version
./gradlew --version

# Expected: Gradle 8.5+
```

---

**Status**: ✅ Foundation ready

**Next steps:** See [CONTRIBUTING.md](./CONTRIBUTING.md) for development workflow and contribution guidelines.
