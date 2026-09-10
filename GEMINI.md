# Yumemi Android Engineer Code Check — Agent Guidelines & Project Context

## Project Overview
This repository contains the Android Engineer Code Check submission for Yumemi Inc.
It is an Android application for searching GitHub repositories, displaying results, and viewing repository details using GitHub's public Search API (`/search/repositories`).

- **Architecture Target**: Multi-Module Clean Architecture + MVI / Unidirectional Data Flow (UDF) + Hilt DI + Jetpack Compose
- **Tech Stack**: Kotlin 1.6+ (moving to 1.9+), Java 17, Gradle (8.2.2/8.5), Android SDK (minSdk 23, compileSdk 33/34), Coroutines/Flow, Ktor Client, Coil, ViewBinding / Jetpack Compose
- **Active Base Branch**: `dev` (All topic PRs branch from and target `dev` using the 3-tier promotion: `dev` &rarr; `stg` &rarr; `main`)

---

## 📍 Current Codebase State (Post-Issue #8 / PR 8.3 Completed)
The application has completed migration to Multi-Module Clean Architecture with Jetpack Compose:
- **`:core:domain`**: Pure Kotlin business models (`RepositoryItem`, `Owner`) and repository contracts (`GitHubRepository`).
- **`:core:network`**: KMP Ktor HTTP service (`GitHubApiService`) with `Logging` plugin (`LogLevel.ALL`) and explicit URL logging.
- **`:core:data`**: `GitHubRepositoryImpl` with query caching and resilient network error mapping.
- **`:shared-core`**: Umbrella module consolidating core dependencies.
- **`:core:designsystem` & `:core:ui`**: Material 3 theme (`CodeCheckTheme`), tokens, and reusable components (`LoadingView`, `EmptyView`, `ErrorView`).
- **`:feature:search` & `:feature:detail`**: Decoupled feature modules with `@HiltViewModel`, `StateFlow` UDF, and Jetpack Compose screens.
- **`:app`**: Single `MainActivity` (`@AndroidEntryPoint`) running Compose `AppNavHost` with Hilt DI.
- **Build Infrastructure**: Custom convention plugins (`build-logic`) enforcing JVM-17 target consistency across JDK 21+ environments.

### Issue Roadmap & Next Priorities
- ✅ **Issues #3 – #7 Completed**: Code Readability, Safety (`SavedStateHandle`), Bug fixes (`forks_count`, remove `runBlocking`, memory leaks), ViewModel extraction, and Program Structure decoupling.
- ✅ **Issue #8 Completed**: **アーキテクチャを適用 (Apply Architecture)** &rarr; Decomposed into 3 atomic PRs:
  - `PR 8.1` ✅: Multi-module layout (`build-logic`, `libs.versions.toml`, `settings.gradle`)
  - `PR 8.2` ✅: Pure Kotlin `:core:domain`, Ktor `:core:network`, caching `:core:data`, and umbrella `:shared-core`
  - `PR 8.3` ✅: `:feature:search`, `:feature:detail`, Hilt DI wiring, and Compose Navigation **(Closes #8)**
- 🎯 **Issue #9 (ACTIVE / NEXT)**: **テストを追加 (Add Tests)** &rarr; Turbine Flow testing + Ktor `MockEngine` HTTP testing.
- 📋 **Issue #10**: **UI をブラッシュアップ (Polish UI)** &rarr; 100% Jetpack Compose + Material 3, Dark theme, bilingual i18n (`values-ja`).
- 📋 **Issue #11**: **新機能を追加 (Add New Features - Bonus)** &rarr; Pagination / Infinite Scrolling, Chrome Custom Tabs, Sorting, Settings Hub, Offline Mock flavor, KMP `:shared`.

---

## 🛡️ Git Commit & Development Guardrails
1. **Code Commits Permitted**: Gemini is permitted to stage files (`git add`) and commit code changes (`git commit`) following task completion or upon user request.
2. **Commit Verification**: Verify that the code builds and unit tests pass before committing. Exclude untracked artifacts, cache files, and `.idea/` configs.
3. **Atomic PR Sizing**: Keep PR diffs under **~300 lines**. Never dump an entire multi-module refactor or an entire issue into a massive 1,500-line PR.
4. **Branch Naming**: Follow semantic prefixes:
   - `feature/<name>` (New features)
   - `fix/<name>` (Bug fixes)
   - `refactor/<name>` (Refactoring without behavior change)
   - `chore/<name>` (Tooling, build scripts, dependencies)
   - `ci/<name>` (CI/CD workflows)
   - `docs/<name>` (Documentation changes)
   - `test/<name>` (Test additions)
5. **Commit Format**: Follow [Conventional Commits](https://www.conventionalcommits.org/):
   `<type>: <imperative description>` (e.g. `feat: implement repository search with Flow`)

---

## 🏗️ Architectural Directives

### 1. Presentation Layer (UDF)
- Expose immutable UI state (`StateFlow<SearchUiState>`) and handle user intentions (`UiIntent`).
- Handle all 4 UI states: `Idle` / `Empty`, `Loading`, `Success(items)`, and `Error(message)`.
- Prevent memory leaks: nullify ViewBinding in `onDestroyView()` when using legacy Fragments; cascade resource teardown in `onCleared()`.
- Handle configuration changes (rotation, process death) gracefully using `SavedStateHandle`.

### 2. Domain Layer (Pure Kotlin)
- Zero Android framework dependencies (`android.*` is prohibited in `:core:domain`).
- Core entities must be immutable data classes (`RepositoryItem`, `Owner`).
- Expose clear repository abstractions (`interface GitHubRepository`).

### 3. Data Layer
- Implement HTTP networking via `GitHubApiClient` / `GitHubApiService` using Ktor.
- Parse responses using dedicated mappers (`RepositoryMapper`) or reflection-free `kotlinx.serialization`.
- Include in-memory query caching and exponential backoff retry with full jitter for network resilience.
- Handle network timeouts, HTTP 403 rate limits, and offline errors gracefully without crashing.

### 4. SOLID & Quality Constraints
- **Single Responsibility (SRP)**: Keep network communication, parsing, caching, and state management in separate classes.
- **Command-Query Separation (CQS)**: Methods must either mutate state or return data without hidden side-effects.
- **Principle of Least Surprise**: Never introduce static global variables or hidden state dependencies.

---

## 🧪 Verification & Build Commands
- Run Lint: `./gradlew lintDebug`
- Run Unit Tests: `./gradlew testDebugUnitTest` (or `./gradlew test`)
- Clean & Assemble Debug APK: `./gradlew clean assembleDebug`

---

## 📝 Pull Request Standards
Always format PR descriptions following `.github/pull_request_template.md`:
- `## issue`: Reference related issue (`close #X` or context)
- `## 概要 (Summary)`: Clear summary in Japanese
- `## 変更内容 (Changes)`: Bullet points of technical changes
- `## スクリーンショット (Screenshots)`: Note visual confirmation
- `## テスト (Testing)`: Checklist of executed verification commands
- `## レビューレベルの設定 (Review Level)`
- `## レビュー観点 (Review Points)`
- `## 参考 (Reference)`: Links to guides and documentation (use branch-agnostic links)

---

## 🤖 Available Specialized Skills & Living Documentation
When performing complex tasks, refer to the skills located in `.agents/skills/` (and `.claude/skills/`):
- `yumemi-issue-workflow` (`.agents/skills/yumemi-issue-workflow/SKILL.md`): Step-by-step TDD, atomic PR sizing (<300 lines), and implementation guide for Yumemi challenge issues.
- `yumemi-code-review` (`.agents/skills/yumemi-code-review/SKILL.md`): Quality gate reviewing code against Yumemi Qiita evaluation criteria using review badges (`[must]`, `[imo]`, `[nits]`, `[memo]`).
- **AI Skills & Pairing Guide**: See [`docs/01_company_and_team/10_ai_agent_skills_guide.md`](./docs/01_company_and_team/10_ai_agent_skills_guide.md) for full cross-tool usage (Gemini, Claude, Cursor, Copilot, ChatGPT).
- **Living Documentation Rule**: Whenever code design, module boundaries, or class responsibilities change, engineers and AI assistants MUST update the corresponding skill files in `.agents/skills/` and `.claude/skills/`.

