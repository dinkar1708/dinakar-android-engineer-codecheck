# Android Engineer Code Check - Solution

Android GitHub repository search application with modern architecture and best practices.

📚 **Documentation:** See [docs/](./docs/) for complete project documentation

---

## 🚀 Quick Start

```bash
# Build debug APK
./gradlew clean assembleDebug

# Run tests
./gradlew test

# Run test coverage report
./gradlew koverHtmlReport
```

**Prerequisites:** JDK 17+, Android Studio Iguana+, Gradle 8.5

- **Test Suite:** 51 automated tests covering domain use cases/models (`:core:domain`), data cache/repositories (`:core:data`), Ktor network API/mappers (`:core:network`), presentation ViewModels, and Jetpack Compose UI components (`:core:ui`, `:feature:search`, `:feature:detail`).
- **Code Coverage:** 52.8% repository-wide line coverage via Kotlinx Kover (100% `:core:domain`, 94.9% `:core:ui`, 82.1% `:core:data`, 65.4% `:feature:detail`, 64.9% `:core:network`, 51.5% `:feature:search`).

📖 **Full guide:** See [docs/GETTING_STARTED.md](./docs/GETTING_STARTED.md)

---

## 📚 Documentation

- **[Start Here](./docs/00_START_HERE.md)** ⭐ - Solution overview and reviewer navigation
- **[Getting Started](./docs/GETTING_STARTED.md)** - Build, run, and prerequisite instructions
- **[Architecture Overview](./docs/ARCHITECTURE_OVERVIEW.md)** - System design and technology choices
- **[Issues Summary](./docs/03_sprint_execution/03_issues_summary.md)** - Breakdown and roadmap for all 9 Yumemi issues
- **[Contributing Guidelines](./CONTRIBUTING.md)** - Branch naming, commit conventions, and workflow
- **[Agent Guidelines & Context](./GEMINI.md)** 🤖 - AI pairing rules, guardrails, and architectural directives
- **[Technical References](./docs/references.md)** - External references and architecture benchmarks
- **[Documentation Portal](./docs/readme.md)** - Full documentation index

---

## 📌 Issue Number Mapping (GitHub Issues vs Yumemi Challenge)

> [!NOTE]
> **Issue Number Offset Rationale:**  
> This repository was initially configured as a **Private** repository. Due to GitHub Actions CI execution and billing limits on private repositories, PR #1 (build fixes) and PR #2 (foundational documentation) were created and merged before running the automated issue copying action (`copy-issues.yml`). Because GitHub allocates a single shared counter for both Pull Requests and Issues, the 9 Yumemi challenge issues are mapped to **GitHub Issues #3 through #11** (an offset of +2).

| Yumemi Challenge Task | GitHub Issue | Milestone | Category / Level | Status |
|:---|:---:|:---:|:---:|:---:|
| **#1: ソースコードの可読性の向上** *(Improve Code Readability)* | [**Issue #3**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | 課題 | 初級 (Beginner) | ✅ Done (PR #17) |
| **#2: ソースコードの安全性の向上** *(Improve Code Safety)* | [**Issue #4**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | 課題 | 初級 (Beginner) | ✅ Done (PR #18) |
| **#3: バグを修正** *(Fix Bugs)* | [**Issue #5**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | 課題 | 初級 (Beginner) | ✅ Done (PR #19) |
| **#4: Fat Fragment の回避** *(Avoid Fat Fragment)* | [**Issue #6**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | 課題 | 初級 (Beginner) | ✅ Done (PR #20) |
| **#5: プログラム構造をリファクタリング** *(Refactor Program Structure)* | [**Issue #7**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | 課題 | 中級 (Intermediate) | ✅ Done (PR #21) |
| **#6: アーキテクチャを適用** *(Apply Architecture)* | [**Issue #8**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | 課題 | 中級 (Intermediate) | ✅ Done (PRs 8.1, 8.2, 8.3) |
| **#7: テストを追加** *(Add Tests)* | [**Issue #9**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | 課題 | 中級 (Intermediate) | 🎯 Active / Next |
| **#8: UI をブラッシュアップ** *(Polish UI)* | [**Issue #10**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10) | 課題 | ボーナス (Bonus) | 📋 Planned |
| **#9: 新機能を追加** *(Add New Features)* | [**Issue #11**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11) | 課題 | ボーナス (Bonus) | 📋 Planned |

---

## 🏗️ Architecture

- **Pattern:** Clean Architecture + MVVM + UDF
- **UI:** Jetpack Compose + Material 3
- **DI:** Hilt
- **Network:** Ktor HTTP Client
- **Modules:** Multi-module architecture (feature + core modules)

---

## 🤖 AI Pairing & Agent Skills Framework

In alignment with the [Yumemi Code Challenge AI policy](https://github.com/yumemi-inc/android-engineer-codecheck#use-of-ai-services), this repository integrates structured **AI Agent Skills & Guardrails** adhering to the [Google Gemini Agent Skills standard](https://github.com/google-gemini/gemini-skills):

- **[GEMINI.md](./GEMINI.md)**: Always-on project context, architectural guidelines, and strict git safety rules (zero autonomous commits, Conventional Commits, branch naming policies from [CONTRIBUTING.md](./CONTRIBUTING.md)).
- **[`yumemi-issue-workflow`](./.agents/skills/yumemi-issue-workflow/SKILL.md)**: Standardized runbook for TDD implementation, layer separation (Clean Architecture), and quality gate verification.
- **[`yumemi-code-review`](./.agents/skills/yumemi-code-review/SKILL.md)**: Automated pre-PR review gate evaluating code against [Yumemi Qiita criteria](https://qiita.com/blendthink/items/aa70b8b3106fb4e3555f) using feedback badges (`[must]`, `[imo]`, `[nits]`, `[memo]`).

---

## 🧪 Features

### Implemented
- GitHub repository search
- Repository detail view
- Multi-Module Clean Architecture (`:core:*`, `:feature:*`, `:app`)
- Jetpack Compose UI (Baseline functional UI)
- Hilt Dependency Injection
- Unidirectional Data Flow (UDF) with StateFlow
- KMP Ktor Network Client with HTTP Logging
- Query in-memory caching and resilient error handling

### Planned
- Comprehensive unit and integration testing (Issue #9)
- UI Polish, Material 3 Dark theme & Japanese Localization (Issue #10)
- Bonus features: Pagination / Infinite Scrolling, Chrome Custom Tabs, Sorting (Issue #11)

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
