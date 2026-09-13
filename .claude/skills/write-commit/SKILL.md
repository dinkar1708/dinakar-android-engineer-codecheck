---
name: write-commit
description: Use this skill when authoring Conventional Commit messages aligned with our project history, SOLID principles, and before/after metrics.
---

# Write Commit Message

## 📱 Current Codebase Context
When writing commits, reference the **current** implementation:
- **UI Layer:** 100% Jetpack Compose with Material 3 Theme (`CodeCheckTheme`) + dual-theme `@Preview`s
- **ViewModel:** `@HiltViewModel` with `StateFlow` Unidirectional Data Flow (UDF) & `SavedStateHandle`
- **DI:** Dagger Hilt (`@AndroidEntryPoint`, `@HiltAndroidApp`, `@Module`, `@InstallIn`)
- **Network:** KMP Ktor `HttpClient` (`GitHubApiService`) with `Logging` plugin and explicit request/response logging
- **Static Analysis:** Detekt 1.23.6 (`config/detekt/detekt.yml`) + Android Lint (`lintDebug`)
- **Modules (11 Modules):** Multi-module Clean Architecture
  - Core KMP: `:core:domain`, `:core:network`, `:core:data`, `:shared-core`
  - Core UI: `:core:designsystem`, `:core:ui`
  - Features: `:feature:splash`, `:feature:search`, `:feature:detail`, `:feature:settings`, `:feature:starred`
  - App Shell: `:app`

## Format Standards

Follow [Conventional Commits](https://www.conventionalcommits.org/) format:

```text
<type>(<optional-scope>): <imperative summary under 50-72 chars>

[optional body explaining WHAT and WHY, wrapped at 72 chars]
- Detail 1
- Detail 2

Before: [previous state or violation]
After: [new compliant state]

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

---

## Allowed Types & Meanings

| Prefix | Name & Purpose | When to Use | Examples |
|:---|:---|:---|:---|
| **`feat`** | **New Feature** | Adding new user-facing functionality, public API capability, or UI screens. | `feat(search): add repository sorting tabs by stars and forks`<br>`feat(theme): support system dynamic dark mode switching` |
| **`fix`** | **Bug Fix** | Correcting a defect, unexpected crash, regression, memory leak, or logic error. | `fix(search): correct forks_count mapping key typo`<br>`fix(detail): nullify ViewBinding in onDestroyView to prevent leak` |
| **`chore`** | **Routine Maintenance** | Housekeeping tasks, repo configuration, cleanup, or maintenance that does **not** alter app production code, business logic, or test logic. | `chore(repo): update .gitignore for local IDE cache files`<br>`chore(release): prepare v1.0.0 milestone changelog`<br>`chore(license): add MIT license header to source files` |
| **`refactor`** | **Refactoring** | Restructuring production code without changing external behavior or fixing bugs (e.g. modularization, extracting classes, improving readability). | `refactor(domain): decouple repository contract from network client`<br>`refactor(detail): extract repository detail card into composable component` |
| **`build`** | **Build System & Dependencies** | Changes that affect Gradle build scripts, dependency catalogs, SDK upgrades, compiler flags, or ProGuard rules. | `build(deps): bump Kotlin from 1.9.20 to 1.9.22`<br>`build(gradle): configure build-logic convention plugin for Java 17` |
| **`ci`** | **Continuous Integration** | Changes to CI/CD workflows, automation scripts, test matrices, or pipeline configuration files. | `ci(github): add automated unit test and lint verification workflow`<br>`ci(actions): cache Gradle dependencies to speed up PR checks` |
| **`test`** | **Testing** | Adding new automated tests, modifying test doubles, fixing flaky tests, or increasing coverage without altering production logic. | `test(search): add Turbine flow test for SearchViewModel error state`<br>`test(data): add Ktor MockEngine tests for 403 rate limit handling` |
| **`docs`** | **Documentation** | Updating or adding markdown documentation, architecture decision records (ADRs), guides, diagrams, or KDoc comments. | `docs(architecture): add clean architecture layer boundary diagram`<br>`docs(contributing): explain semantic commit prefixes and workflow` |
| **`perf`** | **Performance Improvement** | Code changes focused specifically on improving execution speed, reducing memory footprint, or optimizing UI rendering. | `perf(search): debounce search text input by 300ms to reduce network calls`<br>`perf(list): enable stable keys in LazyColumn for optimal recomposition` |
| **`style`** | **Code Style & Formatting** | Cosmetic changes that do not affect code logic (whitespace, indentation, import reordering, line breaks). | `style(ui): reorder imports and apply spotless ktlint formatting` |
| **`revert`** | **Revert Commit** | Reverting a previous commit that introduced a regression or unintended change. | `revert: "feat(search): add experimental query caching"` |

---

## 🔍 Deep Dive: Understanding `chore` vs `build` vs `refactor`

1. **`chore` (Housekeeping & Maintenance)**:
   - Use `chore` for general repository housekeeping that doesn't change runtime code behavior or build definitions.
   - *Examples*: updating `.gitignore`, cleaning up obsolete asset files, updating release templates, organizing documentation folders, deleting scratch scripts.
   - *Mnemonic*: *"If it's an administrative task that doesn't alter how the app runs or compiles, it's a `chore`."*

2. **`build` vs `chore`**:
   - If the change modifies `build.gradle.kts`, `settings.gradle`, `gradle/libs.versions.toml`, Gradle wrapper, or compilation plugins, use **`build`** (e.g. `build(deps): update ktor to 2.3.7`).
   - If the change is non-compilation tooling or broad maintenance, use **`chore`** (e.g. `chore: clean up deprecated IDE files`).

3. **`refactor` vs `chore`**:
   - If code inside `src/main/` is modified to restructure, rename, or improve code design, it is always a **`refactor`** (never a `chore`).
   - *Rule*: Never use `chore` for changes to application architecture or source code.

---

## Real Codebase Examples

### 1. Architectural Refactoring (SOLID Principles)
```text
refactor: extract API Client layer to enforce Single Responsibility

- Create GitHubApiClient interface and GitHubApiClientImpl
- Move HTTP Ktor execution logic out of RepositorySearchViewModel
- Implement Closeable for clean HTTP client teardown

Before: RepositorySearchViewModel made raw HTTP Ktor calls directly
After: Clean separation between presentation and network client layers

Co-Authored-By: Claude <noreply@anthropic.com>
```

### 2. Eliminating Mutable Globals
```text
refactor: remove mutable static state to follow Principle of Least Surprise

- Delete TopActivity.lastSearchDate global static variable
- Remove hidden side-effects from ViewModel search flow

Before: TopActivity held mutable static state accessed across components
After: Fully stateless Activity; predictable ViewModel execution

Co-Authored-By: Claude <noreply@anthropic.com>
```

### 3. Resource Management
```text
refactor: implement proper resource management with Closeable interfaces

- Implement java.io.Closeable on GitHubApiClient and GitHubRepository
- Cascade resource cleanup in ViewModel onCleared()

Before: Ktor HttpClient remained open indefinitely causing connection leaks
After: Client closed deterministically when ViewModel is destroyed

Co-Authored-By: Claude <noreply@anthropic.com>
```

---

## Authoring Rules
1. **Imperative Mood**: Use "extract", "implement", "resolve", "remove", "add" (never "extracted", "adds", or "fixing"). The commit message must complete: *"If applied, this commit will..."*
2. **50/72 Rule**: Keep summary line under **50 characters** (maximum 72). Wrap body paragraphs at **72 characters**.
3. **Atomic Scope**: Keep commits focused on one logical unit of work. Do not mix refactors, feature additions, and styling changes in one commit.
4. **Specify Scopes**: Use specific scopes when applicable: `search`, `detail`, `settings`, `starred`, `splash`, `domain`, `data`, `network`, `ui`, `branding`, `quality`, `deps`, `workflow`.
5. **Traceability**: Reference specific classes (`GitHubApiClient`, `RepositoryMapper`, `RepositorySearchViewModel`).

## 📂 Related Relative Paths
- **Contributing Guide**: `docs/CONTRIBUTING.md`
- **Developer Workflow**: `docs/01_company_and_team/08_developer_workflow.md`
- **Code Commenting & Documentation Standards**: `docs/01_company_and_team/12_code_commenting_and_documentation_standards.md`
- **Engineering Guardrails**: `docs/01_company_and_team/06_engineering_guardrails.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`

