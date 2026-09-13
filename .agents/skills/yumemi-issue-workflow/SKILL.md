---
name: yumemi-issue-workflow
description: >-
  Use this skill when implementing, refactoring, or resolving any Yumemi Android Engineer code check issue or ticket. Guides TDD, layer separation, atomic PR sizing, and acceptance criteria verification.
---

# Yumemi Issue Implementation Workflow

A structured, end-to-end procedure for tackling any issue in the Yumemi Android Engineer Code Check challenge.

## 📱 Current Codebase Context
**Current Implementation State (Post-Sprint 4 / All 9 Challenge Tasks Completed):**
- **Build Infrastructure:** Composite `build-logic` with convention plugins (`AndroidApplication`, `AndroidLibrary`, `AndroidFeature`, `KotlinMultiplatform`, `AndroidHilt`).
- **Dependency Management:** Centralized Version Catalog (`gradle/libs.versions.toml`).
- **Static Analysis:** Detekt 1.23.6 (`config/detekt/detekt.yml`) + Android Lint (`lintDebug`).
- **Multi-Module Layout (11 Modules):**
  - KMP Core Modules: `:core:domain` (pure Kotlin), `:core:network` (Ktor), `:core:data` (caching), `:shared-core` (umbrella framework).
  - Core UI Modules: `:core:designsystem` (M3 tokens), `:core:ui` (shared composables with dual-theme `@Preview`s).
  - Feature Modules: `:feature:splash`, `:feature:search`, `:feature:detail`, `:feature:settings`, `:feature:starred`.
  - Application Shell (`:app`): Single-Activity (`MainActivity : ComponentActivity`) with Hilt DI (`@AndroidEntryPoint`) + Compose Navigation (`AppNavHost`).
- **Target Architecture:**
  - Jetpack Compose + StateFlow UDF + Hilt + Multi-Module Clean Architecture + KMP Type 2 (shared business logic, native UI).

---

## 1. Issue Analysis & Triage
1. Read the target issue description in Japanese and English.
2. Identify the difficulty tier:
   - **初級 (Beginner)**: Foundational bug fixes, typos, memory leaks, crash prevention.
   - **中級 (Intermediate)**: Architecture refactoring, layer extraction (Domain/Data/Presentation), SOLID principles, resource lifecycle.
   - **ボーナス (Bonus)**: Multi-module setup, Jetpack Compose migration, KMP readiness, advanced testing.
3. Formulate binary Acceptance Criteria (Given / When / Then).

---

## 2. Atomic PR Sizing Directive
- **Keep PR diffs under ~300 lines**: Large PRs hide bugs and slow down review.
- **Decompose major issues into atomic PRs**:
  - Example (Issue #8: アーキテクチャを適用):
    - `PR 8.1` ✅: Multi-module layout & `build-logic` convention plugins foundation.
    - `PR 8.2` ✅: Pure Kotlin `:core:domain`, Ktor `:core:network`, caching `:core:data`, and umbrella `:shared-core`.
    - `PR 8.3` ✅: `:feature:search`, `:feature:detail`, Hilt DI wiring & Compose Navigation (Closes #8).
  - Next Issue: **Issue #9: テストを追加 (Add Tests)** &rarr; Turbine Flow testing + Ktor `MockEngine` HTTP testing.
- **Zero Broken Intermediate Builds**: Every atomic PR must compile cleanly (`./gradlew clean assembleDebug`) and maintain existing functionality before merging.

---

## 3. Test-First Implementation (TDD) & Directory Architecture
Before modifying production code, adhere strictly to the 3-guide testing layout:
1. **Guide 1 (Unit & ViewModel Tests - JVM)**:
   - Write tests in `feature/*/src/test/` (ViewModels with Turbine), `core/*/src/commonTest/` (KMP domain/data/network), or `app/src/test/` (Navigation & DI).
   - Ref: [`docs/02_project_architecture/testing/01_unit_testing.md`](../../docs/02_project_architecture/testing/01_unit_testing.md)
2. **Guide 2 (Compose View & Screen Tests - Robolectric JVM)**:
   - Write declarative UI tests in `feature/*/src/test/` and `core/ui/src/test/` using Robolectric.
   - **CRITICAL**: Do NOT create `androidTest/` in feature modules. All screen state tests (`Idle`, `Loading`, `Success`, `Empty`, `Error`) run on JVM in `src/test/` to ensure ~2s execution and 100% Kover coverage contribution.
   - Ref: [`docs/02_project_architecture/testing/02_compose_ui_testing.md`](../../docs/02_project_architecture/testing/02_compose_ui_testing.md)
3. **Guide 3 (Integration & End-to-End Tests)**:
   - Network integration tests: `core/network/src/commonTest/` using Ktor `MockEngine`.
   - On-device E2E tests: Located **exclusively in `app/src/androidTest/`** (`SearchE2ETest.kt`).
   - Ref: [`docs/02_project_architecture/testing/03_integration_and_e2e_testing.md`](../../docs/02_project_architecture/testing/03_integration_and_e2e_testing.md)
4. Confirm test fails initially (Red phase) before implementing green behavior.

---

## 4. Layer Separation & Codebase Contracts

### Domain Layer
- **Pure Kotlin**: No Android SDK imports (`android.*`).
- Define repository contracts:
  ```kotlin
  interface GitHubRepository : Closeable {
      suspend fun searchRepositories(query: String): List<RepositoryItem>
  }
  ```

### Data Layer
- **Client**: `GitHubApiClient` encapsulates Ktor HTTP execution.
- **Mapper**: `RepositoryMapper` parses raw JSON strings with structure validation.
- **Repository Implementation**: `GitHubRepositoryImpl` coordinates Client and Mapper, cascading resource cleanup via `close()`.

### Presentation Layer
- **Unidirectional Data Flow (UDF)**:
  - Expose `StateFlow<SearchUiState>`
  - Sealed interface for state: `Idle`, `Loading`, `Success(items)`, `Empty`, `Error(message)`
- **Lifecycle & Resource Cleanup**:
  - Always clean up ViewBinding in `onDestroyView()` when using legacy views
  - Implement `onCleared()` in ViewModels to close HTTP resources (`repository.close()`)
  - Preserve state across configuration changes using `SavedStateHandle`

---

## 5. Local Quality Verification
Always execute these verification commands before presenting changes:

```bash
# 1. Run all Unit and Robolectric Compose Tests (all 81 tests)
./gradlew testDebugUnitTest

# 2. Run Android Lint Checks
./gradlew lintDebug

# 3. Run Detekt Static Analysis
./gradlew detektAll

# 4. Verify Code Coverage (Target >= 80% line coverage)
./gradlew koverHtmlReportDebug

# 5. Assemble Debug APK
./gradlew clean assembleDebug

# 6. Run Live E2E Instrumented Test on Emulator (When app/src/androidTest is touched)
./gradlew :app:connectedAndroidTest
```

---

## 6. Prepare PR Artifacts
1. Ensure all changes remain uncommitted in the working tree for user review (`git diff`).
2. Suggest conventional commit command:
   ```bash
   git commit -m "<type>: <concise description of issue resolution>"
   ```
3. Generate PR description using `.github/pull_request_template.md` referencing the issue number (`close #X`), ensuring all checklist items in **Testing Architecture Compliance** are satisfied.
4. Verify that AI skills and docs were updated if new code designs were introduced.

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
- **Sprint Delivery Roadmap**: `docs/03_sprint_execution/01_execution_roadmap.md`
- **Issue-to-Ticket Mapping**: `docs/03_sprint_execution/02_how_to_proceed_and_issue_mapping.md`
- **Authoritative Issue Specifications**: `docs/03_sprint_execution/03_issues_summary.md`
- **Clean Architecture & UDF Blueprint**: `docs/02_project_architecture/architecture/01_clean_architecture_and_udf.md`
- **ADR Decision Records**: `docs/02_project_architecture/adr/readme.md`
- **PR Template**: `.github/pull_request_template.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`

