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
# Clean and build debug APK (dev flavor)
./gradlew clean assembleDevDebug

# Assemble all debug flavor APKs
./gradlew assembleDebug

# Run unit tests across all modules
./gradlew test

# Run static analysis
./gradlew detekt
```

---

## Product Flavors

The project supports 4 product flavors configured across development and QA environments:
- **`dev`**: Sandbox backend environment with active sprint features.
- **`mock`**: 100% deterministic offline mock engine for zero-latency testing.
- **`stg`**: Staging environment mirroring production.
- **`prod`**: Live production environment.

---

## Project Structure

```
├── app/                 - Application shell, Hilt DI assembly, MainActivity
├── core/
│   ├── domain/          - Business entities & repository contracts (Pure Kotlin)
│   ├── network/         - Ktor HTTP engine & GitHub API client
│   ├── data/            - Repository implementations, in-memory cache, error handling
│   ├── designsystem/    - Material 3 theme, color schemes, typography tokens
│   └── ui/              - Shared Compose components (LoadingView, ErrorView, EmptyView)
├── feature/
│   ├── splash/          - Splash screen
│   ├── search/          - Search screen with debouncing & SearchViewModel
│   ├── detail/          - Repository detail view & Chrome Custom Tabs
│   ├── starred/         - Starred / bookmarked repositories
│   └── settings/        - Dynamic theme & language localization
├── shared-core/         - Headless KMP umbrella framework (exports to iOS)
├── iosApp/              - Native iOS companion app (SwiftUI)
├── build-logic/         - Convention plugins enforcing Java 17 across modules
└── docs/                - Multi-tier documentation (Company, Architecture, Sprints)
```

---

## Build Verification

```bash
# Verify Gradle version
./gradlew --version

# Expected: Gradle 8.5+
```

---

**Status**: ✅ All 9 challenge issues completed (#3–#11) with 164+ automated tests.

**Next steps:**
- See [00_START_HERE.md](./00_START_HERE.md) for documentation navigation.
- See [01_project_setup.md](./02_project_architecture/setup/01_project_setup.md) for detailed IDE configuration.
- See [CONTRIBUTING.md](./CONTRIBUTING.md) for branching strategy and commit conventions.
