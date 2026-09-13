# Yumemi Android Engineer Code Challenge

<div align="center">

**Modern Android GitHub Repository Search Application**

Enterprise-grade Clean Architecture • Kotlin Multiplatform • Jetpack Compose • Material 3

[![CI Status](https://img.shields.io/badge/CI-Passing-brightgreen)]()
[![Test Coverage](https://img.shields.io/badge/Coverage-81.2%25-brightgreen)]()
[![Tests](https://img.shields.io/badge/Tests-170%20Passing-brightgreen)]()
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM%20%2B%20UDF-blue)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF)]()
[![Compose](https://img.shields.io/badge/Compose-1.7.5-4285F4)]()

</div>

---

## 🎥 Demo Videos

Visual walkthroughs demonstrating the application running live across both phone and tablet form factors:

<div align="center">

| 📱 Phone Walkthrough Demo | 💻 Tablet Landscape Demo (2560 × 1600) |
|:---:|:---:|
| **[▶️ Watch Phone Demo Video (`demo.mov`)](./docs/videos/demo.mov)** | **[▶️ Watch Tablet Demo Video (`tablet_demo.mov`)](./docs/videos/tablet_demo.mov)** |
| 300ms Debounced Search • Repository Details • Star/Bookmark Sync • Light/Dark Dynamic Theming • Instant In-App Bilingual Locale (EN ↔ 日本語) | Adaptive Widescreen Layout • Defensive `FlowRow` Metadata Auto-Wrapping • Tablet Navigation Bar • Zero-Layout-Shift (CLS) Rendering |

📖 *For recording specifications, technical parameters, and video scenarios, see [**Demo Videos Documentation (`docs/videos/README.md`)**](./docs/videos/README.md).*

</div>

---

## 📱 Application Screenshots

<div align="center">

| Search Screen | Repository Details | Dark Mode | Settings |
|:---:|:---:|:---:|:---:|
| ![Search](./docs/screenshots/app/light/03-search-results.png) | ![Details](./docs/screenshots/app/light/05-detail-screen.png) | ![Dark](./docs/screenshots/app/dark/03-search-results.png) | ![Settings](./docs/screenshots/app/light/06-settings-screen.png) |

> ⚡ **Fast Inner-Loop Development with Compose Previews:**  
> All UI screens and reusable components feature `@Preview` configurations, enabling **sub-second UI iteration**, offline mock data previews, and side-by-side Light/Dark/Loading/Error state verification without running an emulator. See [**Compose Previews Gallery (`docs/screenshots/compose_preview/`)**](./docs/screenshots/compose_preview/README.md).

</div>

---

> 📊 **Executive & Reviewer Overview:** For a 3-minute leadership walkthrough, see the [**Executive Presentation Deck (`docs/presentation/README.md`)**](./docs/presentation/README.md) covering multi-platform architecture, team velocity, quality gates, and technical ROI.

## 🎯 About This Project

This project is a comprehensive solution to the **[Yumemi Android Engineer Coding Challenge](https://github.com/yumemi-inc/android-engineer-codecheck)**. The challenge evaluates Android engineering skills across multiple dimensions: code readability, architecture, testing, UI/UX, and modern development practices.

### Challenge Overview

**Objective:** Build a GitHub repository search application that demonstrates production-quality Android development.

**Requirements:**
- Search GitHub repositories via REST API
- Display repository list with key information
- Show detailed repository view with stats and metadata
- Apply modern architecture patterns
- Comprehensive testing strategy
- Clean, maintainable code

### Solution Highlights

✅ **All 9 Challenge Issues Completed** (Beginner + Intermediate + Bonus)
✅ **170 Automated Tests** (100% passing across 35 test suites, **81.2% line coverage** via Kotlinx Kover)
✅ **Multi-Module Clean Architecture** (12 modules with strict dependency rules)
✅ **100% Jetpack Compose UI** (Material 3 with dynamic theming)
✅ **Responsive Multi-Device Design** (Validated across Phone & Tablet in Portrait & Landscape)
✅ **Kotlin Multiplatform** (iOS companion app sharing business logic)
✅ **Production-Ready Quality** (Detekt, CI/CD, documentation)

📚 **Start Here:** [docs/00_START_HERE.md](./docs/00_START_HERE.md) ⭐

---

## 🏗️ Architecture & Modules

This application implements **multi-module Clean Architecture** with strict separation of concerns. Modules are organized from core business logic (most important) to UI presentation layers.

```mermaid
flowchart TB
    subgraph app["🚀 Application Layer"]
        APP[":app<br/>Android App Entry Point<br/>Navigation • Hilt DI • Theme"]
    end

    subgraph features["🎨 Feature Modules (Screens)"]
        direction LR
        SEARCH[":feature:search<br/>Search Screen"]
        DETAIL[":feature:detail<br/>Detail Screen"]
        STARRED[":feature:starred<br/>Starred Screen"]
        SPLASH[":feature:splash<br/>Splash Screen"]
        SETTINGS[":feature:settings<br/>Settings Screen"]
    end

    subgraph ui["🎭 UI Foundation"]
        direction LR
        CORE_UI[":core:ui<br/>Shared Compose<br/>Components"]
        DESIGN[":core:designsystem<br/>Material 3<br/>Theme & Tokens"]
    end

    subgraph data["💾 Data Layer"]
        DATA[":core:data<br/>Repositories<br/>Cache Logic"]
    end

    subgraph foundation["⚡ Core Foundation (Platform-Agnostic)"]
        direction LR
        NETWORK[":core:network<br/>Ktor Client<br/>API Service"]
        DOMAIN[":core:domain<br/>⭐ Use Cases<br/>Models<br/>(Pure Kotlin)"]
    end

    subgraph kmp["📱 Multiplatform"]
        SHARED[":shared-core<br/>KMP Framework<br/>(iOS Export)"]
    end

    APP --> SEARCH
    APP --> DETAIL
    APP --> STARRED
    APP --> SPLASH
    APP --> SETTINGS

    SEARCH --> CORE_UI
    DETAIL --> CORE_UI
    STARRED --> CORE_UI
    SPLASH --> CORE_UI
    SETTINGS --> CORE_UI

    CORE_UI --> DESIGN
    CORE_UI --> DOMAIN

    SEARCH --> DOMAIN
    DETAIL --> DOMAIN
    STARRED --> DOMAIN

    DATA --> NETWORK
    DATA --> DOMAIN

    NETWORK --> DOMAIN

    SHARED --> DOMAIN
    SHARED --> NETWORK
    SHARED --> DATA

    style APP fill:#FF6B6B,stroke:#C92A2A,stroke-width:3px,color:#fff
    style DOMAIN fill:#51CF66,stroke:#37B24D,stroke-width:3px,color:#000
    style SHARED fill:#748FFC,stroke:#4C6EF5,stroke-width:2px,color:#fff
    style DATA fill:#FFA94D,stroke:#FD7E14,stroke-width:2px,color:#000
    style NETWORK fill:#FFA94D,stroke:#FD7E14,stroke-width:2px,color:#000
    style CORE_UI fill:#4DABF7,stroke:#1C7ED6,stroke-width:2px,color:#000
    style DESIGN fill:#4DABF7,stroke:#1C7ED6,stroke-width:2px,color:#000
```

### 📦 Module Details (From Core to UI)

#### 🔷 Core Business Logic (Platform-Agnostic)

**1. `:core:domain`** ⭐ *Most Important*
- **Purpose:** Pure business logic, zero Android dependencies
- **Contains:**
  - Domain models (`GitHubRepository`, `RepositoryOwner`)
  - Use case interfaces (`SearchRepositoriesUseCase`, `GetRepositoryDetailsUseCase`)
  - Repository contracts (interfaces only)
  - Domain-specific exceptions (`GitHubException`, `RateLimitExceededException`)
- **Dependencies:** None (pure Kotlin)
- **Test Coverage:** 100%
- **Why Important:** Defines the business rules and ensures clean dependency inversion

**2. `:core:network`**
- **Purpose:** External API communication
- **Contains:**
  - Ktor HTTP client configuration
  - GitHub REST API service (`GitHubApiService`)
  - DTO models (`RepositoryDto`, `SearchResponseDto`)
  - Response mappers (DTO → Domain)
  - HTTP error handling & retry logic
- **Dependencies:** `:core:domain`, Ktor (Darwin/OkHttp engines)
- **Test Coverage:** Ktor MockEngine tests with fixture responses
- **Key Feature:** Network error → Domain exception translation

**3. `:core:data`**
- **Purpose:** Data layer orchestration
- **Contains:**
  - Repository implementations (`GitHubRepositoryImpl`)
  - In-memory LRU cache (search query caching)
  - Mock data fixtures (offline development)
  - Data source coordination
- **Dependencies:** `:core:domain`, `:core:network`
- **Test Coverage:** 92.6% (cache, network fallback, error mapping)
- **Key Feature:** Transparent caching with `CacheFirst` strategy

**4. `:shared-core`**
- **Purpose:** Kotlin Multiplatform export for iOS
- **Contains:**
  - KMP facade exposing domain/network/data to native iOS
  - Objective-C framework bindings
  - iOS-compatible API surface
- **Dependencies:** `:core:domain`, `:core:network`, `:core:data`
- **Platform:** Compiles to `shared_core.framework` (Apple Silicon/x86_64)
- **Integration:** Consumed by SwiftUI `CodeCheck-iOS` companion app

---

#### 🔷 Android UI Foundation

**5. `:core:designsystem`**
- **Purpose:** Material 3 design system tokens
- **Contains:**
  - Color palette (`md_theme_light_*`, `md_theme_dark_*`)
  - Typography scale (Roboto font family)
  - Shape definitions (rounded corners, etc.)
  - Material 3 theme composition
- **Dependencies:** Compose Material 3
- **Key Feature:** Dynamic light/dark theme switching

**6. `:core:ui`**
- **Purpose:** Shared Compose UI components
- **Contains:**
  - Reusable components (`LoadingView`, `ErrorView`, `EmptyView`)
  - Common chips and badges
  - Format utilities (`formatCount`, `formatDate`)
  - Bottom navigation bar
- **Dependencies:** `:core:domain`, `:core:designsystem`, Compose
- **Test Coverage:** Robolectric Compose UI tests

---

#### 🔷 Feature Modules (Screens)

**7. `:feature:search`**
- **Purpose:** Repository search screen
- **Contains:**
  - `SearchScreen` (Compose UI)
  - `SearchViewModel` (UDF state management)
  - Search bar with debouncing (300ms)
  - Repository card list
  - Filter/sort UI components
- **Dependencies:** `:core:domain`, `:core:ui`, Hilt
- **Test Coverage:** 83.2% (ViewModel + Compose tests)

**8. `:feature:detail`**
- **Purpose:** Repository detail screen
- **Contains:**
  - `DetailScreen` (Compose UI)
  - `DetailViewModel`
  - Owner profile display
  - Stats cards (stars, forks, watchers, issues)
  - Language badge with color coding
- **Dependencies:** `:core:domain`, `:core:ui`, Hilt
- **Test Coverage:** 84.6%

**9. `:feature:starred`**
- **Purpose:** Bookmarked repositories
- **Contains:**
  - `StarredScreen` (favorites collection)
  - Local persistence (in-memory for now)
  - Empty state handling
- **Dependencies:** `:core:domain`, `:core:ui`, Hilt

**10. `:feature:splash`**
- **Purpose:** App launch screen
- **Contains:**
  - Animated logo mark
  - Automatic navigation to main screen
- **Test Coverage:** 68.9%

**11. `:feature:settings`**
- **Purpose:** App configuration
- **Contains:**
  - Theme mode toggle (Light/Dark/System)
  - Language switcher (English/日本語)
  - App information tiles
- **Dependencies:** `:core:ui`, Hilt

---

#### 🔷 Application Entry Point

**12. `:app`**
- **Purpose:** Android application orchestration
- **Contains:**
  - `MainActivity` (single-activity architecture)
  - Jetpack Navigation setup (bottom tabs + screens)
  - Hilt dependency injection configuration
  - Product flavor configurations (Mock/Dev/Stg/Prod)
  - App-level theme composition
- **Dependencies:** All `:feature:*` and `:core:*` modules
- **Build Variants:** 4 flavors × 2 build types = 8 variants

---

## 🎨 UI/UX Design Specifications

Complete design specifications are available in [**`docs/03_design/`**](./docs/03_design/).

**View Interactive Design Specs:**
```bash
# Open in browser
open docs/03_design/00\ Index.html
```

### Design Files

| Screen | Design File | Description |
|--------|-------------|-------------|
| Splash | [`01 SplashScreen.html`](./docs/03_design/01%20SplashScreen.html) | App launch with animated logo |
| Main | [`02 MainScreen.html`](./docs/03_design/02%20MainScreen.html) | Repository list with bottom nav |
| Search | [`03 SearchScreen.html`](./docs/03_design/03%20SearchScreen.html) | Search interface with filters |
| Starred | [`04 StarredScreen.html`](./docs/03_design/04%20StarredScreen.html) | Bookmarked repositories |
| Detail | [`05 DetailScreen.html`](./docs/03_design/05%20DetailScreen.html) | Repository details with stats |
| Settings | [`06 SettingsScreen.html`](./docs/03_design/06%20SettingsScreen.html) | Theme & language settings |

### Component Library

| Component | Design File | Usage |
|-----------|-------------|-------|
| App Icon | [`Common_AppIcon.html`](./docs/03_design/Common_AppIcon.html) | App launcher icon specs |
| Chip | [`Common_Chip.html`](./docs/03_design/Common_Chip.html) | Language tags, filters |
| Repository Card | [`Common_RepoCard.html`](./docs/03_design/Common_RepoCard.html) | List item component |
| Stat Card | [`Common_StatCard.html`](./docs/03_design/Common_StatCard.html) | Stars/Forks/Watchers display |
| Color Palette | [`Common_Palette.html`](./docs/03_design/Common_Palette.html) | Material 3 color system |

<div align="center">

| Design Palette & Tokens | UI Component Specifications |
|:---:|:---:|
| <img src="./docs/screenshots/claude-design/screens/Screenshot 2026-09-13 at 22.16.37.png" width="460" /> | <img src="./docs/screenshots/claude-design/screens/Screenshot 2026-09-13 at 22.16.46.png" width="460" /> |

</div>

---

## ✅ Challenge Issue Completion Status

All **9 Yumemi coding challenge issues** have been successfully completed.

> [!NOTE]
> **Issue Number Offset:** This repository was initially private. PRs #1-2 were merged before copying challenge issues, resulting in a +2 offset (Challenge #1 = GitHub Issue #3, etc.).

| Challenge Task | GitHub Issue | Level | Status | PRs |
|:---|:---:|:---:|:---:|:---:|
| **#1: ソースコードの可読性の向上**<br>*(Improve Code Readability)* | [#3](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | 初級 | ✅ Done | [#17](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/17) |
| **#2: ソースコードの安全性の向上**<br>*(Improve Code Safety)* | [#4](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | 初級 | ✅ Done | [#18](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/18) |
| **#3: バグを修正**<br>*(Fix Bugs)* | [#5](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | 初級 | ✅ Done | [#19](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/19) |
| **#4: Fat Fragment の回避**<br>*(Avoid Fat Fragment)* | [#6](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | 初級 | ✅ Done | [#20](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/20) |
| **#5: プログラム構造をリファクタリング**<br>*(Refactor Program Structure)* | [#7](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | 中級 | ✅ Done | [#21](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/21) |
| **#6: アーキテクチャを適用**<br>*(Apply Architecture)* | [#8](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | 中級 | ✅ Done | Multiple |
| **#7: テストを追加**<br>*(Add Tests)* | [#9](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | 中級 | ✅ Done | [#22](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/22) |
| **#8: UI をブラッシュアップ**<br>*(Polish UI)* | [#10](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10) | ボーナス | ✅ Done | [#35](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/35), [#36](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/36), [#37](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/37) |
| **#9: 新機能を追加**<br>*(Add New Features)* | [#11](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11) | ボーナス | ✅ Done | [#41](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/41), [#42](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/42) |

📖 **Detailed Issue Breakdown:** [docs/03_sprint_execution/03_issues_summary.md](./docs/03_sprint_execution/03_issues_summary.md) &nbsp;|&nbsp; 📋 **[GitHub Project Delivery Board](https://github.com/users/dinkar1708/projects/1/views/1)**

<div align="center">

![GitHub Project Delivery Board](./docs/screenshots/github-project/project-board.png)

</div>

---

## 🚀 Quick Start

### Prerequisites

- **JDK:** 17 or higher
- **Android Studio:** Iguana (2023.2.1) or higher
- **Gradle:** 8.5 (wrapper included)
- **Xcode:** 15+ (optional, for iOS companion app)

### Build & Run

```bash
# Clone repository
git clone https://github.com/dinkar1708/dinakar-android-engineer-codecheck.git
cd dinakar-android-engineer-codecheck

# Build Android debug APK
./gradlew clean assembleDebug

# Install on connected device
./gradlew installDebug

# Run unit tests
./gradlew test

# Generate test coverage report
./gradlew koverHtmlReport
# Report: build/reports/kover/html/index.html

# Run static analysis (Detekt)
./gradlew detekt
# Report: build/reports/detekt/detekt.html
```

### Build Variants (Product Flavors)

| Flavor | Description | App ID | Use Case |
|--------|-------------|--------|----------|
| **mock** | 100% offline, zero API calls | `.mock` | Development, UI testing, rate limit avoidance |
| **dev** | Live GitHub API + debug logs | `.dev` | Active development with network calls |
| **stg** | Staging environment | `.stg` | Pre-production validation |
| **prod** | Production release | *(default)* | Release builds |

```bash
# Build specific flavor
./gradlew assembleMockDebug    # Offline mock data
./gradlew assembleDevDebug     # Live API with logs
./gradlew assembleProdRelease  # Production release
```

📖 **Full Setup Guide:** [docs/GETTING_STARTED.md](./docs/GETTING_STARTED.md)

---

## 🍏 iOS Companion App (Kotlin Multiplatform)

A **native SwiftUI iOS companion app** (`iosApp/CodeCheck-iOS`) consuming shared Kotlin business logic via KMP (`shared_core.framework`).

<div align="center">

| iOS SwiftUI App Running on iPhone Simulator (Live KMP Search) | Xcode Architecture & Scheme Configurations |
|:---:|:---:|
| <img src="./docs/screenshots/ios/ios-app-demo-using-kmp.png" width="560" /> | <img src="./docs/screenshots/ios/ios-app-using-kmp.png" width="380" /> |
| **Live SwiftUI App via KMP**<br>iPhone 17 Pro simulator searching GitHub repositories with shared domain & network engine | **Project Structure & Schemes**<br>Native SwiftUI app with Mock/Dev/Stg/Prod schemes consuming `shared_core.framework` |

</div>

> 📱 **Native Apple Silicon Framework (`shared_core.framework`):**  
> The iOS companion app (`iosApp/CodeCheck-iOS`) consumes the headless Kotlin Multiplatform engine via native Swift `async/await`, sharing 50% of the core domain, networking, and caching logic while maintaining 100% native SwiftUI rendering.



### Key Features

✅ **100% Shared Business Logic** - Domain, network, and data layers reused from Android
✅ **Native Swift Concurrency** - Kotlin `suspend` functions → Swift `async/await`
✅ **Design System Parity** - Same Material 3 colors in SwiftUI
✅ **Flavor Support** - Mock/Dev/Stg/Prod Xcode schemes
✅ **Swift Testing Suite** - Native tests for KMP interop

```bash
# Build KMP framework and run iOS app
./gradlew :shared-core:linkDebugFrameworkIosSimulatorArm64
open iosApp/CodeCheck-iOS.xcodeproj
```

> [!NOTE]
> This is a **technical demonstration** of KMP capabilities, not a full-featured iOS app. Focus is on proving cross-platform business logic sharing.

📖 **iOS Guide:** [iosApp/README.md](./iosApp/README.md)

---

## 🧪 Testing Strategy

- **Total Tests:** **170 automated tests** (269 test executions across all modules, 100% passing, JVM test execution in < 2s)
- **Repository-Wide Line Coverage:** **81.2%** via Kotlinx Kover (exceeds the 80% enterprise threshold)
- **Core Business Logic Coverage:** **89.8% – 100%** (100% domain, 93.3% data, 89.8% network)

### Test Breakdown by Layer

| Module | Tests | Line Coverage | Frameworks |
|--------|-------|---------------|------------|
| `:core:domain` | Use case + model tests | **100%** | JUnit, Kotlin Test |
| `:core:data` | Repository + cache tests | **93.3%** | Turbine, Ktor MockEngine |
| `:core:network` | API service + DTO tests | **89.8%** | Ktor MockEngine, Fixtures |
| `:feature:detail` | Screen logic tests | **85.1%** | Robolectric, Hilt Test |
| `:feature:search` | ViewModel + Compose UI | **83.5%** | Robolectric, Compose Test |
| `:feature:splash` | Splash navigation & layout | **68.9%** | Robolectric, Compose Test |
| `:core:ui` | Design system components | **56.5%** | Compose Test API |

### Testing Frameworks

- **Unit Tests:** JUnit 5, Kotlin Test, MockK
- **Flow Testing:** Turbine (async flow assertions)
- **Compose UI:** Robolectric + Compose Test API
- **Network Mocking:** Ktor MockEngine with fixture JSON
- **Coverage:** Kotlinx Kover (HTML reports)
- **iOS:** Swift Testing (native framework)

```bash
# Run all tests with coverage
./gradlew testDebugUnitTest koverHtmlReport

# Run specific module tests
./gradlew :core:data:testDebugUnitTest
./gradlew :feature:search:testDebugUnitTest

# Run Detekt static analysis
./gradlew detekt
```

---

## 🎨 Features & Functionality

### ✅ Implemented

#### Core Features
- ✅ **GitHub Repository Search** - Debounced search (300ms) with in-memory TTL query caching
- ✅ **Pagination & Feed Chunking** - Incremental page loading with manual 'Load More' trigger and state preservation
- ✅ **Repository Details** - Owner profile, stats (stars/forks/watchers/issues), language
- ✅ **Starred Repositories** - Bookmark favorite repos (persistent across sessions)
- ✅ **Chrome Custom Tabs** - In-app browser for GitHub URLs

#### UI/UX
- ✅ **100% Jetpack Compose** - Material 3 with dynamic color schemes
- ✅ **Dark Mode Support** - Auto/Light/Dark theme switching
- ✅ **Bilingual Localization** - English/日本語 without activity recreation
- ✅ **Responsive & Adaptive Multi-Device Design** - Validated across Mobile Phones (`emulator-5554`) and Tablets (`emulator-5556`) in both Horizontal (Landscape) and Vertical (Portrait) orientations
- ✅ **Defensive Layout Wrapping** - Adaptive `FlowRow` dynamically wrapping extreme metadata (63-character language tags, multi-billion counts)
- ✅ **Configuration Change Survival** - Instantaneous state preservation across screen rotation via `SavedStateHandle` and `rememberSaveable`
- ✅ **Compose Previews & Fast Inner Loop** - Sub-second visual iteration via comprehensive `@Preview` states (Light/Dark/Loading/Error)
- ✅ **Error Handling** - Graceful offline mode, rate limit messaging
- ✅ **Loading States & Shimmer Skeletons** - Skeleton screens and progress indicators with zero layout shifts

#### Architecture & Enterprise Scalability
- ✅ **Clean Architecture** - Strict layer separation (Domain → Data → UI)
- ✅ **Multi-Module** - 12 Gradle modules with dependency rules (scales to 50+ engineers without merge conflicts)
- ✅ **MVVM + UDF** - StateFlow-based unidirectional data flow
- ✅ **Hilt DI** - Compile-time dependency injection
- ✅ **Kotlin Multiplatform** - iOS companion app sharing business logic
- ✅ **High-Performance Non-Blocking Coroutines** - Zero main-thread blocking (`Dispatchers.IO`), cancellation of superseded search jobs, and TTL in-memory caching

#### Quality Assurance
- ✅ **170 Automated Tests** - Unit, UI, and integration tests (100% passing in <2s)
- ✅ **81.2% Line Coverage** - Kotlinx Kover reports (100% domain, 93.3% data, 89.8% network)
- ✅ **Detekt Static Analysis** - CI/CD quality gates (0 violations)
- ✅ **Conventional Commits** - Semantic versioning ready
- ✅ **CI/CD Pipeline** - GitHub Actions (build, test, lint)

---

## 🤖 AI-Assisted Development Disclosure

In full transparency per [Yumemi's AI policy](https://github.com/yumemi-inc/android-engineer-codecheck#use-of-ai-services), this project leveraged **AI pair programming** to accelerate development while maintaining strict quality standards.

### AI Tools Used

This project utilized the following AI tools, and we **encourage teams to adopt these tools** for increased productivity:

| AI Tool | Version/Model | Primary Use Case | Usage Scope |
|:---|:---|:---|:---:|
| **Google Gemini** | Gemini 1.5 Pro/Flash | Code generation, architecture design, test writing | 🟢 Heavy |
| **Claude Code** | Claude Sonnet 3.5/4 | Pair programming, refactoring, code review | 🟢 Heavy |
| **Claude AI** | Claude 3.5 Sonnet | UI/UX design specification export (HTML) | 🟡 Moderate |
| **GitHub Copilot** | Latest | Code completion, boilerplate generation | 🟡 Moderate |

> **Note:** All AI-generated code was validated through 170 automated tests, manual code review, and CI/CD quality gates (Detekt, build verification).

### Agent Framework & Guardrails

- **[GEMINI.md](./GEMINI.md)** - Always-on architectural context and coding rules
- **[`yumemi-issue-workflow`](./.agents/skills/yumemi-issue-workflow/)** - Standardized TDD workflow
- **[`yumemi-code-review`](./.agents/skills/yumemi-code-review/)** - Automated PR review against evaluation criteria

### Representative Prompts

**1. Test-Driven Architecture:**
> *"Write unit tests for `GitHubRepositoryImpl` verifying LRU cache and network error translation. Ensure HTTP 403 → `RateLimitExceeded`, unknown host → `OfflineException`. Never let Ktor exceptions escape into domain."*

**2. Defensive UI Layout:**
> *"Fix the stress test card where 63-char language name causes 21-line vertical expansion. Use `FlowRow` with `maxLines=1` and `TextOverflow.Ellipsis`. Add Robolectric tests asserting bounded height."*

**3. Multi-Module Boundaries:**
> *"Analyze build-logic convention plugins. Ensure `:core:domain` has zero Android deps, `:core:network` encapsulates Ktor, and `:shared-core` exports Objective-C framework without leaking Gradle tasks."*

📖 **Full AI Usage Documentation:** [GEMINI.md](./GEMINI.md)

---

## 📚 Documentation

Comprehensive documentation is available in [**`docs/`**](./docs/)

### Quick Links

| Document | Description |
|----------|-------------|
| **[🌟 START HERE](./docs/00_START_HERE.md)** | Solution overview and reviewer navigation guide |
| [Getting Started](./docs/GETTING_STARTED.md) | Build instructions and prerequisites |
| [Architecture Overview](./docs/ARCHITECTURE_OVERVIEW.md) | System design and technology stack |
| [Contributing Guide](./docs/CONTRIBUTING.md) | Git workflow, commit conventions, PR templates |
| [Issues Summary](./docs/03_sprint_execution/03_issues_summary.md) | All 9 challenge issues breakdown |
| [Market Research](./docs/03_sprint_execution/05_market_research_and_product_strategy.md) | Ecosystem analysis and benchmarking |
| [Code Review Standards](./docs/01_company_and_team/04_code_review_guidelines.md) | Review levels and criteria |
| [Testing Guide](./docs/01_company_and_team/06_engineering_guardrails.md) | Test architecture and placement rules |
| [AI Tools Adoption](./docs/01_company_and_team/11_ai_tools_adoption_proposal.md) | AI pairing strategy and tools |
| [Technical References](./docs/references.md) | External links and evaluation criteria |

---

## 🏆 Technology Stack

### Core Technologies

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| **Language** | Kotlin | 2.1.0 | Primary development language |
| **UI Framework** | Jetpack Compose | 1.7.5 | Declarative UI toolkit |
| **Design System** | Material 3 | Latest | Google's design language |
| **Architecture** | Clean + MVVM + UDF | - | Multi-layer separation |
| **Dependency Injection** | Hilt | 2.51 | Compile-time DI |
| **Networking** | Ktor Client | 3.0.0 | HTTP client (KMP-compatible) |
| **Async/Coroutines** | Kotlin Coroutines | 1.9.0 | Structured concurrency |
| **Navigation** | Compose Navigation | 2.8.0 | Screen routing |
| **Testing** | JUnit 5 + Turbine + MockK | - | Unit/UI/Flow testing |
| **Code Coverage** | Kotlinx Kover | 0.8.3 | Coverage reports |
| **Static Analysis** | Detekt | 1.23.6 | Code quality enforcement |
| **CI/CD** | GitHub Actions | - | Automated build/test/deploy |

### Kotlin Multiplatform

- **iOS Framework:** `shared_core.framework` (Darwin engine)
- **iOS UI:** SwiftUI (native)
- **Swift Bridge:** Kotlin/Native Objective-C interop
- **iOS Testing:** Swift Testing framework

### Build System

- **Gradle:** 8.5
- **JDK:** 17
- **Android Gradle Plugin:** 8.7.3
- **Kotlin Gradle Plugin:** 2.1.0
- **Convention Plugins:** Custom `build-logic` module

### 🛠️ Developer & Profiling Tooling Suite

| Category | Tool | Primary Role / Telemetry Output | Documentation & Proof |
|:---|:---|:---|:---:|
| **Performance Profiling** | **Android Studio CPU Profiler** | System Trace verifying 0 main-thread blocks (<16ms frame target) | [Tools Guide](./docs/screenshots/tools/README.md) |
| **Memory Inspection** | **Android Studio Memory Profiler** | Java/Kotlin Heap Dump proving 0 retained Activity/ViewModel leaks | [Heap Analysis](./docs/screenshots/tools/README.md) |
| **Network Telemetry** | **Android Studio Network Inspector** | Proves 300ms query debouncing & `InMemoryCache` hits (0ms latency) | [Network Telemetry](./docs/screenshots/tools/README.md) |
| **Fast Inner Loop** | **Compose Split-Editor `@Preview`** | Sub-second visual iteration for Light/Dark/Loading/Error states | [Compose Previews](./docs/screenshots/compose_preview/README.md) |
| **Environment Control** | **Android Studio Build Variants** | Instant 1-click switching across `dev`, `mock`, `stg`, and `prod` | [Build Variants](./docs/screenshots/README.md) |
| **Static Code Analysis** | **Detekt 1.23.6** | Static lint rules enforcing Clean Architecture & Compose guidelines | [CI Pipeline](./docs/screenshots/ci/ci_pipeline.png) |
| **Code Coverage** | **Kotlinx Kover 0.8.3** | Automated verification of 81.2% repository-wide line coverage | [Coverage Report](./docs/screenshots/coverage/ci_coverage.png) |
| **Flow & Network Mocking** | **Turbine 1.2.0 & Ktor MockEngine** | Deterministic Flow state-machine testing without internet calls | [Test Architecture](./docs/01_company_and_team/06_engineering_guardrails.md) |
| **AI Pair Programming** | **Gemini 1.5, Claude Sonnet, Copilot** | TDD test writing, architectural ADRs, and refactoring | [AI Disclosure](#-ai-assisted-development-disclosure) |

---


## 📱 Complete Visual Screen Gallery

A comprehensive visual showcase of all application screens, states, themes, performance profiling telemetry, and developer tools. For capture guidelines and technical parameters, see [**Screenshots Guide (`docs/screenshots/screenshots_guide.md`)**](./docs/screenshots/screenshots_guide.md) and [**Media Assets (`docs/screenshots/README.md`)**](./docs/screenshots/README.md).

---

### 🏆 Priority 1: Application Screen Previews (Core App Experience — Top Priority)

The core user journey is built with **100% Jetpack Compose** and **Material 3** following Unidirectional Data Flow (UDF). Every screen defensively handles all 4 UI states (**Idle/Empty**, **Loading**, **Success**, and **Error**) with zero main-thread ANRs or memory leaks.

#### ☀️ 1.1 Light Theme — Complete Screen Lifecycle & State Flows

| Splash Screen | Empty Search State | Shimmer Loading Skeleton | Search Results |
|:---:|:---:|:---:|:---:|
| <img src="./docs/screenshots/app/light/00-splash.png" width="220" /> | <img src="./docs/screenshots/app/light/01-search-empty.png" width="220" /> | <img src="./docs/screenshots/app/light/02-search-loading.png" width="220" /> | <img src="./docs/screenshots/app/light/03-search-results.png" width="220" /> |
| **Android 12+ Splash API**<br>Seamless branded cold start | **Idle / Empty State**<br>Visual prompt guiding query entry | **Zero-CLS Shimmer**<br>Skeleton screen preventing layout shifts | **Debounced Results**<br>300ms debounce with avatars & stars |

| Repository Details | Starred Bookmarks | Defensive Error Boundary | Settings Hub |
|:---:|:---:|:---:|:---:|
| <img src="./docs/screenshots/app/light/05-detail-screen.png" width="220" /> | <img src="./docs/screenshots/app/light/14-starred-screen.png" width="220" /> | <img src="./docs/screenshots/app/light/04-search-error.png" width="220" /> | <img src="./docs/screenshots/app/light/06-settings-screen.png" width="220" /> |
| **Detail & Stats**<br>Forks, stars, owner & web link | **Starred Collection**<br>Persistent offline bookmarks | **Error Recovery**<br>Retry prompt replacing fatal crashes | **Settings & Info**<br>Theme, language, & flavor info |

| Language Selection Dialog | Theme Selection Dialog |
|:---:|:---:|
| <img src="./docs/screenshots/app/light/07-settings-language.png" width="220" /> | <img src="./docs/screenshots/app/light/08-settings-theme.png" width="220" /> |
| **Bilingual Dialog**<br>Instant toggle between English & 日本語 | **Theme Selector Dialog**<br>System Default, Light, or Dark mode |

---

#### 🌙 1.2 Dark Theme — Material 3 High Contrast

High-contrast dark theme utilizing Material 3 tonal palettes for optimal low-light accessibility and battery efficiency:

| Empty Search (Dark) | Loading Skeleton (Dark) | Search Results (Dark) | Repository Details (Dark) |
|:---:|:---:|:---:|:---:|
| <img src="./docs/screenshots/app/dark/01-search-empty.png" width="220" /> | <img src="./docs/screenshots/app/dark/02-search-loading.png" width="220" /> | <img src="./docs/screenshots/app/dark/03-search-results.png" width="220" /> | <img src="./docs/screenshots/app/dark/05-detail-screen.png" width="220" /> |
| **Dark Idle State**<br>Soft dark contrast illustration | **Dark Shimmer Skeleton**<br>Subtle pulsating animation | **Dark Results Feed**<br>High-contrast star badges & text | **Dark Detail Screen**<br>Legible metadata cards & avatar |

| Starred Bookmarks (Dark) | Error Recovery (Dark) | Settings Overview (Dark) |
|:---:|:---:|:---:|
| <img src="./docs/screenshots/app/dark/14-starred-screen.png" width="220" /> | <img src="./docs/screenshots/app/dark/04-search-error.png" width="220" /> | <img src="./docs/screenshots/app/dark/06-settings-screen.png" width="220" /> |
| **Dark Starred Collection**<br>Instant bookmark state sync | **Dark Error State**<br>Clear user guidance & retry button | **Dark Settings Hub**<br>Tonal elevation with clean dividers |

---

#### 🌐 1.3 Dynamic Bilingual Localization & Product Flavors

Runtime locale switching without Activity recreation, paired with defensive product flavor environments:

| English Interface | Japanese Interface (日本語) | DEV Environment (Live API) | MOCK Environment (100% Offline) |
|:---:|:---:|:---:|:---:|
| <img src="./docs/screenshots/app/light/09-english.png" width="220" /> | <img src="./docs/screenshots/app/light/10-japanese.png" width="220" /> | <img src="./docs/screenshots/app/light/11-dev-flavor.png" width="220" /> | <img src="./docs/screenshots/app/light/12-mock-flavor.png" width="220" /> |
| **English Locale**<br>Standard English labels & numbers | **日本語 Localization**<br>Instant in-app Japanese switch | **`dev` Flavor**<br>Live GitHub API with URL logging | **`mock` Flavor**<br>100% offline (bypasses 60 req/hr limit) |

---

#### 📱 1.4 Responsive Multi-Device Adaptations

Defensive responsive design ensuring fluid usability across form factors and orientations:

##### Tablet Widescreen (2560 × 1600)
Adaptive widescreen layout featuring side navigation rail, multi-pane reading density, and `FlowRow` auto-wrapping:

| Empty State (Tablet) | Search Results (Tablet) |
|:---:|:---:|
| <img src="./docs/screenshots/app/tablet/01-search-empty-tablet.png" width="460" /> | <img src="./docs/screenshots/app/tablet/02-search-results-tablet.png" width="460" /> |
| **Widescreen Idle**<br>Generous spacing with persistent side rail | **Widescreen Results**<br>Expanded card width & auto-wrapping language chips |

| Repository Details (Tablet) | Starred Bookmarks (Tablet) | Settings Hub (Tablet) |
|:---:|:---:|:---:|
| <img src="./docs/screenshots/app/tablet/03-detail-tablet.png" width="310" /> | <img src="./docs/screenshots/app/tablet/04-starred-tablet.png" width="310" /> | <img src="./docs/screenshots/app/tablet/05-settings-tablet.png" width="310" /> |
| **Tablet Detail View**<br>Spacious header with clear metrics | **Tablet Starred View**<br>Fast access to offline bookmarks | **Tablet Settings View**<br>Comfortable tablet touch targets |

##### Phone Landscape Orientation
Defensively optimized for horizontal usage, preserving scroll positions, soft keyboard space, and card layouts:

| Search Results (Landscape) | Repository Details (Landscape) |
|:---:|:---:|
| <img src="./docs/screenshots/app/landscape/01-search-results-landscape.png" width="460" /> | <img src="./docs/screenshots/app/landscape/02-detail-landscape.png" width="460" /> |
| **Landscape Search**<br>Compact search bar leaving maximum screen for list items | **Landscape Detail**<br>Optimized horizontal spacing for repository statistics |

| Starred Repositories (Landscape) | Settings Screen (Landscape) |
|:---:|:---:|
| <img src="./docs/screenshots/app/landscape/03-starred-landscape.png" width="460" /> | <img src="./docs/screenshots/app/landscape/04-settings-landscape.png" width="460" /> |
| **Landscape Starred**<br>Responsive item layout with quick remove toggle | **Landscape Settings**<br>Side-by-side preference arrangement |

---

### 🍏 Priority 2: Native iOS Companion App (Kotlin Multiplatform)

A native **SwiftUI iOS application** (`iosApp/CodeCheck-iOS`) consuming the shared Kotlin Multiplatform engine from `:shared-core` (`shared_core.framework`). Demonstrates 50% core code reuse with 100% native platform fidelity:

| iOS SwiftUI App on iPhone Simulator (Live KMP Search) | Xcode Project Architecture & Scheme Configurations |
|:---:|:---:|
| <img src="./docs/screenshots/ios/ios-app-demo-using-kmp.png" width="560" /> | <img src="./docs/screenshots/ios/ios-app-using-kmp.png" width="380" /> |
| **Live iOS Repository Search**<br>iPhone 17 Pro simulator executing live GitHub queries with shared domain models, Ktor HTTP client, and in-memory caching | **Multiplatform Xcode Structure**<br>Native SwiftUI app configured with Mock, Dev, Stg, and Prod schemes consuming `shared_core.framework` |

---

### 🎥 Priority 3: Live Video Walkthroughs

High-fidelity recordings demonstrating fluid 60fps animations, 300ms debounced queries, dynamic theme toggling, and multi-device responsiveness:

| 📱 Phone Walkthrough Demo | 💻 Tablet Landscape Demo (2560 × 1600) |
|:---:|:---:|
| **[▶️ Watch Phone Demo Video (`demo.mov`)](./docs/videos/demo.mov)** | **[▶️ Watch Tablet Demo Video (`tablet_demo.mov`)](./docs/videos/tablet_demo.mov)** |
| 4.9 MB • Full user journey: splash, debounced search, details view, Chrome Custom Tabs, star persistence, dynamic Light/Dark toggling, and bilingual switching. | 1.6 MB • Tablet widescreen experience: adaptive layouts, persistent side navigation rail, `FlowRow` metadata wrap, and defensive error testing. |

> 🎨 *Also see the [**Claude AI Design Specification Walkthrough (`docs/screenshots/claude-design/design_demo.mov`)**](./docs/screenshots/claude-design/design_demo.mov) demonstrating the interactive HTML/CSS design specs.*

---

### ⚡ Priority 4: Developer Velocity & Fast Inner-Loop Development

Tools and configurations providing sub-second developer feedback loops:

| Compose Split-Editor `@Preview` Canvas | Android Studio Build Variants |
|:---:|:---:|
| <img src="./docs/screenshots/compose_preview/preview.png" width="460" /> | <img src="./docs/screenshots/dev/build_variants.png" width="460" /> |
| **Sub-Second Compose Previews**<br>Renders Light, Dark, Loading, Empty, and Error states simultaneously in Android Studio with zero build wait time. | **Product Flavor Matrix**<br>Instant switching between `dev` (Live API), `mock` (100% Offline), `stg`, and `prod` configurations. |

---

### 🔬 Priority 5: Android Studio Performance Telemetry & Profiling Proofs

Empirical telemetry proofs captured via Android Studio Profiler, validating zero main-thread ANRs, zero memory leaks, and optimal network utilization:

| Profiler Telemetry Overview | CPU Profiler: 0 Main-Thread Blocks |
|:---:|:---:|
| <img src="./docs/screenshots/tools/profiler_overview.png" width="460" /> | <img src="./docs/screenshots/tools/cpu-zero_leaks.png" width="460" /> |
| **Real-Time Telemetry Dashboard**<br>Simultaneous monitoring of CPU, Memory, Energy, and Network during active queries. | **CPU Trace Verification**<br>Zero main-thread blocks: all networking and JSON parsing offloaded to `Dispatchers.IO`. |

| Memory Profiler: 0 Memory Leaks | Network Inspector: Debounce & Cache Hits |
|:---:|:---:|
| <img src="./docs/screenshots/tools/memory_heap_dump.png" width="460" /> | <img src="./docs/screenshots/tools/network.png" width="460" /> |
| **Heap Dump Analysis**<br>Clean garbage collection with 0 Activity or Composable memory leaks after repeated navigation. | **Network Inspector Telemetry**<br>Proves 300ms query debouncing prevents request spam; repeated queries hit `InMemoryCache`. |

| Profiler Launch Home | Network Thread Breakdown |
|:---:|:---:|
| <img src="./docs/screenshots/tools/profile_home.png" width="460" /> | <img src="./docs/screenshots/tools/network_thread_view.png" width="460" /> |
| **Device Session Initialization**<br>Targeted profiling setup for CodeCheck debug process. | **Coroutine Thread Utilization**<br>Confirms background coroutine worker pool handling HTTP I/O. |

---

### 🛡️ Priority 6: Quality Gates, CI/CD & Project Delivery Tracking

Automated quality verification pipelines and agile project execution:

| CI Automated Pipeline Verification | Kotlinx Kover Code Coverage (81.2%) | GitHub Projects Agile Sprint Board |
|:---:|:---:|:---:|
| <img src="./docs/screenshots/ci/ci_pipeline.png" width="310" /> | <img src="./docs/screenshots/coverage/ci_coverage.png" width="310" /> | <img src="./docs/screenshots/github-project/project-board.png" width="310" /> |
| **Automated CI Gate**<br>GitHub Actions verifying build, tests, & Detekt rules. | **Kover Coverage Report**<br>81.2% repo coverage (100% domain, 93.3% data). | **Agile Delivery Board**<br>100% completion of challenge issues #3–#11 across 4 sprints. |

---

### 🎨 Priority 7: Claude AI Design Specifications & Prototypes

HTML/CSS design tokens, typography scales, and interactive screen prototypes exported during early architectural planning (`docs/screenshots/claude-design/screens/`):

| Design Color Palette & Tokens | Component Library & Buttons | Responsive Layout Specs |
|:---:|:---:|:---:|
| <img src="./docs/screenshots/claude-design/screens/Screenshot 2026-09-13 at 22.16.37.png" width="310" /> | <img src="./docs/screenshots/claude-design/screens/Screenshot 2026-09-13 at 22.16.46.png" width="310" /> | <img src="./docs/screenshots/claude-design/screens/Screenshot 2026-09-13 at 22.17.04.png" width="310" /> |
| **Token Architecture**<br>Design tokens exported for Compose theme mapping | **UI Components**<br>Stateful button, chip, and card specifications | **Layout Blueprints**<br>Spacing grid and adaptive breakpoints |

---

## 🔗 Links & Resources

- **GitHub Repository:** [dinkar1708/dinakar-android-engineer-codecheck](https://github.com/dinkar1708/dinakar-android-engineer-codecheck)
- **Project Board:** [Issue Tracker](https://github.com/users/dinkar1708/projects/1/views/1)
- **Challenge Repository:** [yumemi-inc/android-engineer-codecheck](https://github.com/yumemi-inc/android-engineer-codecheck)
- **Evaluation Criteria:** [Qiita Article](https://qiita.com/blendthink/items/aa70b8b3106fb4e3555f)

---

## 📄 License

This project is submitted as part of the Yumemi Android Engineer coding challenge.

See [LICENSE](./LICENSE) for details.

---

<div align="center">

**Built with ❤️ using Clean Architecture, Kotlin Multiplatform, and Jetpack Compose**

Submitted for **Yumemi Android Engineer Position**

</div>

