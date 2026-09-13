---
name: yumemi-code-review
description: >-
  Use this skill when reviewing code, refactors, or pull requests for the Yumemi Android Engineer challenge against Qiita evaluation criteria and company code review guidelines.
---

# Yumemi Code Review Guidelines & Quality Gate

This skill outlines the standard review checklist and criteria used by Yumemi reviewers (as referenced in their [Qiita evaluation article](https://qiita.com/blendthink/items/aa70b8b3106fb4e3555f)).

## 📱 Current Codebase Tech Stack
- **Build Infrastructure:** Composite `build-logic` Convention Plugins + Version Catalog (`libs.versions.toml`) + JVM-17 target validation
- **Static Analysis:** Detekt 1.23.6 (`config/detekt/detekt.yml`) + Android Lint (`lintDebug`)
- **Modules (11 Modules):** Multi-module Clean Architecture
  - Core KMP: `:core:domain`, `:core:network`, `:core:data`, `:shared-core`
  - Core UI: `:core:designsystem`, `:core:ui`
  - Features: `:feature:splash`, `:feature:search`, `:feature:detail`, `:feature:settings`, `:feature:starred`
  - App Shell: `:app`
- **UI:** 100% Jetpack Compose + Material 3 Theme (`CodeCheckTheme`) + Dual-Theme Light/Dark `@Preview` on all screens/components
- **ViewModel:** `@HiltViewModel` with `StateFlow` Unidirectional Data Flow (UDF) & `SavedStateHandle`
- **DI:** Dagger Hilt (`@AndroidEntryPoint`, `@HiltAndroidApp`, `@Module`, `@InstallIn`)
- **Network:** KMP Ktor `HttpClient` with `Logging` plugin and explicit request/response logging
- **Async:** Coroutines + `viewModelScope` + `StateFlow`

---

## 🏷️ Review Feedback Badges
When providing feedback on code or PR diffs, categorize comments using standard Yumemi badges:

| Badge | Severity | Definition | Action Required |
|:---|:---|:---|:---|
| **`[must]`** | **Critical** | Crashes, memory leaks, API contract breaches, regressions, security flaws. | Must be fixed before merge. |
| **`[imo]`** | **Important** | "In My Opinion" — architectural recommendations, design improvements, better Kotlin idioms. | Discussion / consideration recommended. |
| **`[nits]`** | **Minor** | Typo, formatting, code style consistency, slight naming improvement. | Nice to fix, non-blocking. |
| **`[memo]`** | **Informational**| Sharing context, asking a clarifying question, or praising good design. | No action required. |

---

## 🔍 Core Evaluation Checklist

### 1. SOLID & Architectural Cleanliness
- [ ] **Single Responsibility (SRP)**:
  - `GitHubApiClient`: Handles raw HTTP execution via Ktor only.
  - `RepositoryMapper`: Parses raw JSON strings into domain `RepositoryItem` models only.
  - `GitHubRepository`: Coordinates API client and mapper; encapsulates data layer contracts.
  - `RepositorySearchViewModel`: Manages search business logic, coroutines, and UI state only.
- [ ] **Interface Segregation (ISP)**:
  - `GitHubApiClient` and `GitHubRepository` interfaces expose only necessary operations (`searchRepositories` + `close`).
- [ ] **Dependency Inversion (DIP)**:
  - ViewModel and callers depend on abstractions (`GitHubRepository`, `GitHubApiClient`) rather than concrete implementations.
- [ ] **Principle of Least Surprise**:
  - Zero mutable static variables (e.g. `TopActivity.lastSearchDate` must never be reintroduced).
- [ ] **Command-Query Separation (CQS)**:
  - Methods either execute a command mutating state (`searchRepositories()`, `close()`) or query data without side effects.

### 2. Android Lifecycle & Memory Leak Prevention
- [ ] **Resource Teardown (`Closeable`)**:
  - Does `RepositorySearchViewModel.onCleared()` deterministically invoke `repository.close()` to release the Ktor HTTP engine?
- [ ] **ViewBinding Cleanup**:
  - If using Fragments with stored bindings, is `_binding = null` invoked in `onDestroyView()`?
- [ ] **Configuration Change Survival**:
  - Does UI state survive screen rotation without restarting network requests?
- [ ] **Process Death Resilience**:
  - Is critical user state preserved via `SavedStateHandle`?

### 3. State Handling & Edge Cases
- [ ] **4 State Completeness**: Are all 4 UI states handled?
  1. `Loading`: Progress indicator visible, search input handled.
  2. `Success`: Results populated in RecyclerView / LazyColumn.
  3. `Empty`: Friendly message when search yields 0 items (not a blank screen).
  4. `Error`: Graceful error banner/dialog for network failures or rate limits (HTTP 403).
- [ ] **Input Sanitization**: Are blank strings or whitespace-only queries trimmed and ignored safely?

### 4. Code Hygiene, Static Analysis & Documentation
- [ ] No Hungarian notation (`_binding`, `m_variable` except for private backing fields).
- [ ] No wildcard imports (`import foo.bar.*`).
- [ ] Favor immutability (`val` over `var`, immutable collections).
- [ ] Strings and dimensions extracted into resources (`R.string`, `R.dimen`).
- [ ] **Detekt Static Analysis**: Clean `./gradlew detektAll` with 0 issues. Functions keep cyclomatic complexity bounded; no unused imports.
- [ ] **Documentation & KDoc**: Public interfaces, repositories, and non-obvious algorithms have concise English KDoc explaining WHY not WHAT, adhering to `docs/01_company_and_team/12_code_commenting_and_documentation_standards.md`.

### 5. UI, Compose Previews & Adaptive Design
- [ ] **Dual-Theme `@Preview` Coverage**: Every screen and reusable UI component provides `@Preview(name = "Light Mode")` and `@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)`.
- [ ] **Adaptive Layout (`FlowRow`)**: Variable-length metadata badges wrap cleanly on constrained screens without clipping or layout shifts.
- [ ] **Multi-Form Factor Validation**: Verified across mobile phones and tablets in both Portrait and Landscape orientations.

### 6. Testing Architecture & Directory Compliance
- [ ] **Guide 1 (Unit & ViewModel)**: All unit and ViewModel tests are located in `src/test/` (Android) or `src/commonTest/` (KMP).
- [ ] **Guide 2 (Compose View & Screen)**: All Compose UI tests are located in `feature/*/src/test/` and `core/ui/src/test/` using Robolectric (JVM). **Verify that NO `androidTest/` folder was accidentally created in feature modules.**
- [ ] **Guide 3 (Integration & E2E)**: On-device E2E tests are located exclusively in `app/src/androidTest/` (`SearchE2ETest.kt`), and network integration tests use Ktor `MockEngine` in `core/network/src/commonTest/`.
- [ ] **Coverage Gate**: Code coverage is verified with Kover (`./gradlew koverHtmlReportDebug`) and meets the >= 80% line coverage standard.

---

## 🚀 How to Run Review Checks Locally
```bash
# 1. Static analysis & Code Style
./gradlew lintDebug
./gradlew detektAll

# 2. All Unit and Robolectric Compose Tests (81 tests)
./gradlew testDebugUnitTest

# 3. Code Coverage Verification (Target >= 80%)
./gradlew koverHtmlReportDebug

# 4. Full Debug APK assembly
./gradlew clean assembleDebug

# 5. Connected E2E Test on Emulator (when app/src/androidTest changes)
./gradlew :app:connectedAndroidTest
```

---

## 📂 Related Relative Paths
- **Master Testing Strategy**: `docs/02_project_architecture/testing/readme.md`
- **Guide 1: Unit & ViewModel Testing**: `docs/02_project_architecture/testing/01_unit_testing.md`
- **Guide 2: Compose View Testing**: `docs/02_project_architecture/testing/02_compose_ui_testing.md`
- **Guide 3: Integration & E2E Testing**: `docs/02_project_architecture/testing/03_integration_and_e2e_testing.md`
- **Test Traceability Matrix**: `docs/02_project_architecture/testing/04_test_cases_matrix.md`
- **Detekt Configuration**: `config/detekt/detekt.yml`
- **Code Commenting & Documentation Standards**: `docs/01_company_and_team/12_code_commenting_and_documentation_standards.md`
- **UI & UX Design Specification**: `docs/02_project_architecture/design/ui_ux_design_specification.md`
- **Contributing Guide**: `docs/CONTRIBUTING.md`
- **Network Service**: `core/network/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/network/GitHubApiService.kt`
- **Domain Models & Contract**: `core/domain/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/domain/`
- **Repository Implementation**: `core/data/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImpl.kt`
- **Search Feature (UI & ViewModel)**: `feature/search/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/search/`
- **Detail Feature (UI & ViewModel)**: `feature/detail/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/detail/`
- **Settings Feature (UI & ViewModel)**: `feature/settings/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/settings/`
- **Starred Feature (UI & ViewModel)**: `feature/starred/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/starred/`
- **Application Shell & DI**: `app/src/main/kotlin/jp/co/yumemi/android/codecheck/`
- **Review Guidelines**: `docs/01_company_and_team/04_code_review_guidelines.md`
- **Yumemi Review Culture**: `docs/01_company_and_team/09_yumemi_review_culture.md`
- **Definition of Done**: `docs/01_company_and_team/05_definition_of_done.md`
- **Engineering Guardrails**: `docs/01_company_and_team/06_engineering_guardrails.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`


