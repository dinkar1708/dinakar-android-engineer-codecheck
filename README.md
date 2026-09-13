# Android Engineer Code Check - Solution

Android GitHub repository search application with modern architecture and best practices.

📚 **Documentation:** See [docs/](./docs/) for complete project documentation

---

## 🚀 Quick Start

```bash
# Build Android debug APK
./gradlew clean assembleDebug

# Run Android unit tests
./gradlew test

# Run test coverage report
./gradlew koverHtmlReport

# Build KMP iOS framework & open native SwiftUI companion app
./gradlew :shared-core:linkDebugFrameworkIosSimulatorArm64
open iosApp/CodeCheck-iOS.xcodeproj
```

**Prerequisites:** JDK 17+, Android Studio Iguana+, Gradle 8.5 | Xcode 15+ (for iOS companion)

- **Test Suite:** 164+ automated unit/UI tests (100% green, 0 failures) + on-device E2E tests covering domain use cases/models (`:core:domain`), data cache/repositories (`:core:data`), Ktor network API/mappers (`:core:network`), presentation ViewModels, Compose screens/components (`:core:ui`, `:feature:search`, `:feature:detail`), and app navigation/DI (`:app`).
- **iOS Test Suite:** Automated Swift Testing unit tests (`iosApp/CodeCheck-iOSTests`) verifying KMP SharedCore interop.
- **Code Coverage:** 81.0% - 83.9% repository-wide line coverage via Kotlinx Kover (100% `:core:domain`, 92.6% `:core:data`, 84.6% `:feature:detail`, 83.2% `:feature:search`, 68.9% `:feature:splash`, 56.5% `:core:ui`, 23.2% `:app`).

📖 **Full guide:** See [docs/GETTING_STARTED.md](./docs/GETTING_STARTED.md) | [iosApp/README.md](./iosApp/README.md)

---

## 📚 Documentation

- **[Start Here](./docs/00_START_HERE.md)** ⭐ - Solution overview and reviewer navigation
- **[日本語サマリー (Japanese Reviewer Guide)](./docs/00_START_HERE_JA.md)** 🇯🇵 - 日本語によるプロジェクト概要、技術選定、および評価ポイント
- **[Getting Started](./docs/GETTING_STARTED.md)** - Build, run, and prerequisite instructions
- **[Architecture Overview](./docs/ARCHITECTURE_OVERVIEW.md)** - System design and technology choices
- **[iOS Companion App Guide](./iosApp/README.md)** 🍏 - Native SwiftUI companion powered by KMP
- **[Issues Summary](./docs/03_sprint_execution/03_issues_summary.md)** - Breakdown and roadmap for all 9 challenge issues
- **[Market Research & Product Strategy](./docs/03_sprint_execution/05_market_research_and_product_strategy.md)** 📊 - GitHub client ecosystem research & architecture benchmarking
- **[Contributing Guidelines](./docs/CONTRIBUTING.md)** - Branch naming, commit conventions, and workflow
- **[Agent Guidelines & Context](./GEMINI.md)** 🤖 - AI pairing rules, guardrails, and architectural directives
- **[Technical References](./docs/references.md)** - External references and architecture benchmarks
- **[Documentation Portal](./docs/readme.md)** - Full documentation index

---

## 📌 Issue Number Mapping (GitHub Issues vs Challenge Tasks)

> [!NOTE]
> **Issue Number Offset Rationale:**  
> This repository was initially configured as a **Private** repository. Due to GitHub Actions CI execution and billing limits on private repositories, PR #1 (build fixes) and PR #2 (foundational documentation) were created and merged before running the automated issue copying action (`copy-issues.yml`). Because GitHub allocates a single shared counter for both Pull Requests and Issues, the 9 challenge issues are mapped to **GitHub Issues #3 through #11** (an offset of +2). See [Technical References](./docs/references.md#7-github-api--assessment-standards) for upstream specifications and evaluation benchmarks.

| Challenge Task | GitHub Issue | Milestone | Category / Level | Status |
|:---|:---:|:---:|:---:|:---:|
| **#1: ソースコードの可読性の向上** *(Improve Code Readability)* | [**Issue #3**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | 課題 | 初級 (Beginner) | ✅ Done (PR #17) |
| **#2: ソースコードの安全性の向上** *(Improve Code Safety)* | [**Issue #4**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | 課題 | 初級 (Beginner) | ✅ Done (PR #18) |
| **#3: バグを修正** *(Fix Bugs)* | [**Issue #5**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | 課題 | 初級 (Beginner) | ✅ Done (PR #19) |
| **#4: Fat Fragment の回避** *(Avoid Fat Fragment)* | [**Issue #6**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | 課題 | 初級 (Beginner) | ✅ Done (PR #20) |
| **#5: プログラム構造をリファクタリング** *(Refactor Program Structure)* | [**Issue #7**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | 課題 | 中級 (Intermediate) | ✅ Done (PR #21) |
| **#6: アーキテクチャを適用** *(Apply Architecture)* | [**Issue #8**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | 課題 | 中級 (Intermediate) | ✅ Done (PRs 8.1, 8.2, 8.3) |
| **#7: テストを追加** *(Add Tests)* | [**Issue #9**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | 課題 | 中級 (Intermediate) | ✅ Done (PR #22) |
| **#8: UI をブラッシュアップ** *(Polish UI)* | [**Issue #10**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10) | 課題 | ボーナス (Bonus) | ✅ Done (PRs #35, #36, #37) |
| **#9: 新機能を追加** *(Add New Features)* | [**Issue #11**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11) | 課題 | ボーナス (Bonus) | ✅ Done (PRs #41, #42) |

---

## 🏗️ Architecture

- **Pattern:** Clean Architecture + MVVM + UDF (Unidirectional Data Flow)
- **UI:** Jetpack Compose + Material 3 (Android) | SwiftUI (iOS Companion)
- **DI:** Hilt (`:app`) | Kotlin Multiplatform Facade (`:shared-core` for iOS)
- **Network:** Ktor HTTP Client with Darwin (iOS) & OkHttp (Android) engines
- **Cross-Platform:** Kotlin Multiplatform (KMP) sharing `:core:domain`, `:core:network`, and `:core:data` as an Apple native binary (`shared_core.framework`)
- **Modules:** Multi-module Clean Architecture (`:core:*`, `:feature:*`, `:app`, `:shared-core`)

---

## 🍏 Native iOS Companion App (Kotlin Multiplatform)

> [!NOTE]
> **Technical Demonstration Only — Not a Full iOS Application**  
> The iOS companion app (`iosApp/CodeCheck-iOS.xcodeproj`) is strictly a **lightweight technical demonstration** designed to prove that our Kotlin domain, networking, and caching logic (`:shared-core`) compiles into Apple native binaries and runs seamlessly inside a native SwiftUI frontend. It intentionally focuses on a clean search list with console logging to showcase KMP interop without superfluous mobile UI complexity.

- **100% Shared Business Logic:** Repository contracts, use cases, Ktor network engine, caching, and offline datasets are shared from `:shared-core`.
- **First-Class Swift Concurrency:** Kotlin `suspend` functions bridge automatically into Swift `async`/`await` (`try await repository.searchRepositories(query: trimmed)`).
- **Design System Parity:** Native SwiftUI views implement the exact same color palette (`ColorTheme.swift`) as Android Material 3 (`:core:designsystem`).
- **Flavor Switching:** Xcode schemes support 4 configurations: **Mock** (100% offline, zero rate limits), **Dev**, **Stg**, and **Prod** (Live GitHub REST API).
- **Automated Native Tests:** Native Swift Testing suite (`CodeCheck-iOSTests`) tests KMP repository interop directly.

See the **[iOS Companion App Guide](./iosApp/README.md)** for complete details and Xcode build instructions.

---

## 🤖 AI Pairing & Prompt Disclosure

In strict alignment with the [upstream code challenge AI policy](https://github.com/yumemi-inc/android-engineer-codecheck#use-of-ai-services), this project utilized AI pair programming (Google Gemini Agent / Claude) to accelerate test-driven development, architectural scaffolding, and edge-case verification. All architectural designs, code refactoring, and implementations were reviewed and verified through automated test suites and on-device execution.

### Agent Framework & Guardrails
- **[GEMINI.md](./GEMINI.md)**: Always-on project context, architectural directives (Clean Architecture, UDF, zero android.* leaks in domain), and strict git safety rules (zero autonomous commits, Conventional Commits, branch naming policies from [docs/CONTRIBUTING.md](./docs/CONTRIBUTING.md)).
- **[`yumemi-issue-workflow`](./.agents/skills/yumemi-issue-workflow/SKILL.md)**: Standardized runbook for TDD implementation, layer separation, atomic PR sizing (<300 lines), and acceptance criteria verification.
- **[`yumemi-code-review`](./.agents/skills/yumemi-code-review/SKILL.md)**: Automated pre-PR review gate evaluating code against authoritative Qiita evaluation criteria (see [Technical References](./docs/references.md#7-github-api--assessment-standards)) using feedback badges (`[must]`, `[imo]`, `[nits]`, `[memo]`).

### Representative Prompts Used
Below are sample representative prompts illustrating how AI pairing was directed throughout development:

1. **Test-Driven Architecture & Error Mapping:**
   > *"Write a unit test suite for GitHubRepositoryImpl verifying in-memory LRU caching and network error translation. Ensure HTTP 403 rate limits map to RateLimitExceeded, unknown host maps to OfflineException, and timeouts trigger exponential backoff. Do not let Ktor or Android framework exceptions escape the data layer into domain models."*

2. **Defensive Layout & Intrinsic Resiliency:**
   > *"Investigate the mock stress test card where language is 63 characters long ('Visual Basic for Applications (.NET Framework Core Edition)') and star count is in the billions. In Compose Row, zero-width wrapping causes 21-line vertical expansion (~1,170px card height). Refactor using @OptIn(ExperimentalLayoutApi::class) FlowRow with maxLines = 1 and TextOverflow.Ellipsis so stats flow cleanly below long titles. Add Robolectric Compose UI tests asserting bounded height."*

3. **Multi-Module Clean Architecture Boundaries:**
   > *"Analyze our build-logic convention plugins and module dependencies. Ensure :core:domain contains zero Android dependencies (pure Kotlin), :core:network encapsulates Ktor engines, and :shared-core exports an Objective-C framework for native iOS companion integration without leaking internal Gradle tasks."*

---

## 🧪 Features

### Implemented & Verified
- GitHub repository search with debounced queries
- Repository detail view with owner profile, repo stats, and language badge
- Multi-Module Clean Architecture (`:core:*`, `:feature:*`, `:app`, `:shared-core`)
- 100% Jetpack Compose UI (Material 3 with custom brand tokens)
- Hilt Dependency Injection with modular component bindings
- Unidirectional Data Flow (UDF) with StateFlow
- KMP Ktor Network Client with HTTP Logging
- Query in-memory caching and resilient retry with full jitter
- 100% Offline Mock Flavor (`mock` vs `prod`) & deterministic fixtures (12 edge cases)
- Comprehensive test suite (164+ unit/UI tests, Robolectric Compose tests, Turbine Flow tests, Ktor MockEngine, on-device E2E)
- Dynamic Material 3 Light/Dark theme mode switching
- Dynamic bilingual Japanese/English localization without activity recreation
- Bottom navigation tabs with Starred/Bookmarks collection & persistence
- Chrome Custom Tabs for external GitHub URL navigation
- **Native iOS SwiftUI Companion App (`CodeCheck-iOS`) consuming KMP SharedCore**

---

## 📦 Build Variants

- **Debug:** Development with logging
- **Release:** Optimized production build

---

## 🔗 Links

- **Project Board (Issues & Milestones):** [Mobile Platform Engineering - Issue Tracker](https://github.com/users/dinkar1708/projects/1/views/1)
- **Live GitHub Issue Tracker:** [dinkar1708/dinakar-android-engineer-codecheck/issues](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues)
- **Challenge Repository:** [yumemi-inc/android-engineer-codecheck](https://github.com/yumemi-inc/android-engineer-codecheck)

---

## 📄 License

See [LICENSE](./LICENSE)
