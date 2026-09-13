---
name: review-code
description: Use this skill when performing code reviews against Android, Kotlin, SOLID principles, and Yumemi evaluation standards.
---

# Code Review Guidelines

Review checklist aligned with Yumemi's Qiita evaluation criteria and our codebase architecture.

## 📱 Current Tech Stack Context
- **Build Infrastructure:** Composite `build-logic` Convention Plugins + Version Catalog (`libs.versions.toml`) + JVM-17 target validation
- **Static Analysis:** Detekt 1.23.6 (`config/detekt/detekt.yml`) + Android Lint (`lintDebug`)
- **Module Structure (11 Modules):** Multi-module Clean Architecture
  - Core KMP: `:core:domain`, `:core:network`, `:core:data`, `:shared-core`
  - Core UI: `:core:designsystem`, `:core:ui`
  - Features: `:feature:splash`, `:feature:search`, `:feature:detail`, `:feature:settings`, `:feature:starred`
  - Application Shell: `:app`
- **UI:** 100% Jetpack Compose + Material 3 Theme (`CodeCheckTheme`) + Dual-Theme Light/Dark `@Preview` on all screens/components
- **Architecture:** Unidirectional Data Flow (UDF) + `@HiltViewModel` (`StateFlow<SearchUiState>`) & `SavedStateHandle`
- **DI:** Dagger Hilt (`@AndroidEntryPoint`, `@HiltAndroidApp`, `@Module`, `@InstallIn`)
- **Async:** Kotlin Coroutines + `viewModelScope` + `StateFlow`
- **Network:** KMP Ktor `HttpClient` with `Logging` plugin and explicit request/response logging
- **Navigation:** Jetpack Compose Navigation (`AppNavHost`)

---

## 🏷️ Yumemi Review Badges
Categorize every review comment using these badges:

| Badge | Meaning | When to Use | Action Required |
|:---|:---|:---|:---|
| **`[must]`** | Critical / Blocker | Memory leak, crash, unhandled HTTP 403, broken contract, `!!` unwrap | Must be fixed before merge |
| **`[imo]`** | In My Opinion | Cleaner Kotlin idioms, architectural separation, naming improvements | Discussion recommended |
| **`[nits]`** | Nitpick | Spacing, typos, comment formatting, minor style preference | Optional, non-blocking |
| **`[memo]`** | Informational | Praising good design, sharing background context or docs link | No action needed |

---

## 🔍 Core Review Checklist

### 1. SOLID Principles & Layer Responsibilities
- [ ] **Single Responsibility (SRP)**:
  - `GitHubApiClient`: Network HTTP communication only.
  - `RepositoryMapper`: JSON parsing and validation only.
  - `GitHubRepository`: Data coordination and caching only.
  - `RepositorySearchViewModel`: Presentation state and coroutine lifecycle only.
- [ ] **Interface Segregation (ISP)**: Interfaces are minimal (`searchRepositories()` + `close()`).
- [ ] **Principle of Least Surprise**: No global mutable state (e.g. `TopActivity.lastSearchDate` must stay deleted).
- [ ] **Command-Query Separation (CQS)**: Methods either mutate state (Command) or return data without side effects (Query).

### 2. Android Lifecycle & Resource Cleanup
- [ ] **Closeable Resources**: Does `RepositorySearchViewModel` invoke `repository.close()` in `onCleared()`?
- [ ] **ViewBinding Cleanup**: Is `_binding = null` set in Fragment `onDestroyView()`? (Applies if legacy views or fragments are referenced)
- [ ] **Coroutine Scope**: Are jobs launched within `viewModelScope`? No `GlobalScope` or blocking `runBlocking`.
- [ ] **State Preservation**: Are critical UI selections and query values preserved across rotation / process death via `SavedStateHandle`?

### 3. State Handling & Edge Cases
- [ ] **4 UI State Completeness**: Handled cleanly (`Loading`, `Success`, `Empty`, `Error`).
- [ ] **Input Sanitization**: Blank/whitespace queries trimmed and handled gracefully without unnecessary API requests.
- [ ] **Network Resilience**: HTTP 403 rate limits, socket timeouts, and offline errors caught and converted to friendly user states.

### 4. Code Hygiene, Static Analysis & Documentation
- [ ] No Hungarian notation (`m_var`, `_var` except for private backing properties).
- [ ] No wildcard imports (`import foo.bar.*`).
- [ ] Favor immutability (`val` over `var`, immutable collections).
- [ ] Strings and dimensions extracted into resources (`R.string`, `R.dimen`) or design tokens.
- [ ] **Detekt Static Analysis**: Clean `./gradlew detektAll` with 0 issues. Functions maintain manageable cyclomatic complexity (< 15); unused imports eliminated.
- [ ] **Code Commenting & KDoc**: Public APIs, repositories, and non-obvious algorithms have concise English KDoc explaining WHY not WHAT, per `docs/01_company_and_team/12_code_commenting_and_documentation_standards.md`.

### 5. UI, Compose Previews & Adaptive Design
- [ ] **Dual-Theme `@Preview` Coverage**: Every screen and reusable UI component provides `@Preview(name = "Light Mode")` and `@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)`.
- [ ] **Adaptive Layout (`FlowRow`)**: Variable-length metadata badges wrap cleanly on constrained screens without clipping or layout shifts.
- [ ] **Multi-Form Factor Validation**: Verified across mobile phones and tablets in both Portrait and Landscape orientations.

### 6. Testing Architecture & Directory Compliance
- [ ] **Guide 1 (Unit & ViewModel)**: All unit and ViewModel tests strictly located in `src/test/` or `src/commonTest/` (KMP).
- [ ] **Guide 2 (Compose View & Screen)**: All Compose UI tests strictly located in `feature/*/src/test/` and `core/ui/src/test/` using Robolectric (JVM). **Verify that NO `androidTest/` folder was created in feature modules.**
- [ ] **Guide 3 (Integration & E2E)**: On-device E2E tests strictly located in `app/src/androidTest/` (`SearchE2ETest.kt`), and network integration tests in `core/network/src/commonTest/`.
- [ ] **Coverage Gate**: Code coverage verified via Kover (`./gradlew koverHtmlReportDebug`) with line coverage >= 80%.

---

## 📝 Review Comment Format Example

```markdown
**[must]** ViewBinding memory leak in Fragment
**Suggestion:**
```kotlin
override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
```
**Reason:** Fragments outlive their views in the backstack. Nullifying `_binding` prevents holding dead view references.
```

---

## ✅ Merge Approval Criteria
- [ ] `./gradlew clean assembleDebug` compiles with 0 errors.
- [ ] `./gradlew lintDebug` passes with 0 fatal issues.
- [ ] `./gradlew detektAll` passes with 0 issues.
- [ ] `./gradlew testDebugUnitTest` passes 100% (all 81 unit & Compose UI tests).
- [ ] `./gradlew koverHtmlReportDebug` verifies >= 80% line coverage.
- [ ] `./gradlew :app:connectedAndroidTest` passes on connected emulator/device (when `app/src/androidTest` is touched).
- [ ] Directory layout complies strictly with Testing Guides 1, 2, and 3.
- [ ] No unhandled exceptions or swallowed error states.
- [ ] Follows project conventions in `docs/CONTRIBUTING.md`.
- [ ] Skills and documentation updated if new designs were added (`docs/01_company_and_team/10_ai_agent_skills_guide.md`).

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
- **Engineering Guardrails**: `docs/01_company_and_team/06_engineering_guardrails.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`

