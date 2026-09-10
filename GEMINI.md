# Yumemi Android Engineer Code Check — Agent Guidelines & Project Context

## Project Overview
This repository contains the Android Engineer Code Check submission for Yumemi Inc.
It is an Android application for searching GitHub repositories, displaying results, and viewing repository details using GitHub's public Search API (`/search/repositories`).

- **Architecture Target**: Multi-module Clean Architecture + MVI / Unidirectional Data Flow (UDF)
- **Tech Stack**: Kotlin, Java 17, Gradle (8.x), Android SDK (minSdk 23, compileSdk 33), Coroutines/Flow, Ktor Client, Coil, ViewBinding / Jetpack Compose
- **Active Base Branch**: `dev` (All topic PRs branch from and target `dev`)

---

## 🛡️ Strict Guardrails & Git Safety Policy
1. **Zero Autonomous Commits**: Never execute `git commit`, `git push`, or alter git history automatically. Keep all changes in the working tree for developer inspection (`git diff`, `git status`).
2. **Present Commands as Text**: When changes are ready, suggest git commands as formatted Markdown text for the developer to review and run.
3. **Branch Naming**: Follow semantic prefixes:
   - `feature/<name>` (New features)
   - `fix/<name>` (Bug fixes)
   - `refactor/<name>` (Refactoring without behavior change)
   - `chore/<name>` (Tooling, build scripts, dependencies)
   - `ci/<name>` (CI/CD workflows)
   - `docs/<name>` (Documentation changes)
   - `test/<name>` (Test additions)
4. **Commit Format**: Follow [Conventional Commits](https://www.conventionalcommits.org/):
   `<type>: <imperative description>` (e.g. `feat: implement repository search with Flow`)

---

## 🏗️ Architectural Directives

### 1. Presentation Layer
- Expose immutable UI state (`StateFlow<UiState>`) and handle user intentions (`UiIntent`).
- Prevent memory leaks: nullify ViewBinding in `onDestroyView()` when using Fragments; close any open clients in `onCleared()`.
- Handle configuration changes (rotation, process death) gracefully using `SavedStateHandle`.
- Display all 4 UI states: `Loading`, `Success`, `Empty`, and `Error`.

### 2. Domain Layer (Pure Kotlin)
- Zero Android framework dependencies (`android.*` is prohibited).
- Core entities must be immutable data classes (`RepositoryItem`, etc.).
- Expose clear repository abstractions (`interface GitHubRepository`).

### 3. Data Layer
- Implement HTTP networking via `GitHubApiClient` using Ktor.
- Parse responses using dedicated mappers (`RepositoryMapper`).
- Handle network timeouts, HTTP 403 rate limits, and offline errors gracefully without crashing.

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
- `## スクリーンショット (Screenshots)`: Note UI changes or lack thereof
- `## テスト (Testing)`: Checklist of executed tests
- `## レビューレベルの設定 (Review Level)`
- `## レビュー観点 (Review Points)`
- `## 参考 (Reference)`: Links to guides and documentation

---

## 🤖 Available Specialized Skills
When performing complex tasks, refer to the skills located in `.agents/skills/`:
- `yumemi-issue-workflow`: Step-by-step TDD and implementation guide for Yumemi challenge issues.
- `yumemi-code-review`: Quality gate reviewing code against Yumemi Qiita evaluation criteria using review badges (`[must]`, `[imo]`, `[nits]`, `[memo]`).
