---
name: yumemi-issue-workflow
description: >-
  Use this skill when implementing, refactoring, or resolving any Yumemi Android Engineer code check issue or ticket. Guides TDD, layer separation, atomic PR sizing, and acceptance criteria verification.
---

# Yumemi Issue Implementation Workflow

A structured, end-to-end procedure for tackling any issue in the Yumemi Android Engineer Code Check challenge.

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
  - Example (Issue #8):
    - `PR 8.1`: Multi-module layout & `build-logic` foundation (~200 lines).
    - `PR 8.2`: Pure Kotlin `:core:domain` and resilient `:core:data` (~300 lines).
    - `PR 8.3`: Hilt DI wiring & `StateFlow` UDF (~350 lines, closes #8).
- **Zero Broken Intermediate Builds**: Every atomic PR must compile cleanly (`./gradlew assembleDebug`) and pass existing tests before merging.

---

## 3. Test-First Implementation (TDD)
Before modifying production code:
1. Locate or create unit tests in `app/src/test/java/...` or `:core:testing`.
2. Write unit test cases reproducing:
   - **Happy path**: Valid search input returns expected item list.
   - **Edge cases**: Empty search string, whitespace-only query, special characters.
   - **Error scenarios**: HTTP 403 rate limit, HTTP 404, network timeout, malformed JSON.
3. Confirm test fails initially (Red phase).

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
  - Always clean up ViewBinding in `onDestroyView()`
  - Implement `onCleared()` in ViewModels to close HTTP resources (`repository.close()`)
  - Preserve state across configuration changes using `SavedStateHandle`

---

## 5. Local Quality Verification
Always execute these verification commands before presenting changes:

```bash
# 1. Run Unit Tests
./gradlew testDebugUnitTest

# 2. Run Android Lint Checks
./gradlew lintDebug

# 3. Assemble Debug APK
./gradlew clean assembleDebug
```

---

## 6. Prepare PR Artifacts
1. Ensure all changes remain uncommitted in the working tree for user review (`git diff`).
2. Suggest conventional commit command:
   ```bash
   git commit -m "<type>: <concise description of issue resolution>"
   ```
3. Generate PR description using `.github/pull_request_template.md` referencing the issue number (`close #X`).
4. Verify that AI skills and docs were updated if new code designs were introduced.

---

## 📂 Related Relative Paths
- **Sprint Delivery Roadmap**: `docs/03_sprint_execution/01_execution_roadmap.md`
- **Issue-to-Ticket Mapping**: `docs/03_sprint_execution/02_how_to_proceed_and_issue_mapping.md`
- **Authoritative Issue Specifications**: `docs/03_sprint_execution/03_issues_summary.md`
- **Clean Architecture & UDF Blueprint**: `docs/02_project_architecture/architecture/01_clean_architecture_and_udf.md`
- **ADR Decision Records**: `docs/02_project_architecture/adr/readme.md`
- **PR Template**: `.github/pull_request_template.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`

