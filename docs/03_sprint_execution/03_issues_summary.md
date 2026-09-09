# Issues Summary

All **9 Yumemi challenge issues** (mapped to **GitHub Issues #3 through #11**) will be addressed through incremental PRs.

---

## Issue Status & GitHub Mapping

> [!NOTE]
> **Issue Number Mapping & Offset Rationale (+2):**  
> This repository was initially configured as a **Private** repository. Due to GitHub Actions CI execution and billing limits on private repositories, PR #1 (build fixes) and PR #2 (foundational documentation) were created and merged before running the automated issue copying action (`copy-issues.yml`). Because GitHub allocates a single shared sequential counter for both PRs and Issues, Yumemi Challenge Issues #1 through #9 are tracked as **GitHub Issues #3 through #11**.

| Yumemi Challenge | GitHub Issue | Title | Level | Status |
|:---:|:---:|:------|:-----:|:------:|
| **#1** | [**#3**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | ソースコードの可読性の向上 | 初級 | 🚀 In Progress |
| **#2** | [**#4**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | ソースコードの安全性の向上 | 初級 | 📋 Planned |
| **#3** | [**#5**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | バグを修正 | 初級 | 📋 Planned |
| **#4** | [**#6**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | Fat Fragment の回避 | 初級 | 📋 Planned |
| **#5** | [**#7**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | プログラム構造をリファクタリング | 中級 | 📋 Planned |
| **#6** | [**#8**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | アーキテクチャを適用 | 中級 | 📋 Planned |
| **#7** | [**#9**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | テストを追加 | 中級 | 📋 Planned |
| **#8** | [**#10**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10) | UI をブラッシュアップ | ボーナス | 📋 Planned |
| **#9** | [**#11**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11) | 新機能を追加 | ボーナス | 📋 Planned |

**Legend**: 初級 = Beginner, 中級 = Intermediate, ボーナス = Bonus

---

## Issue #1: [GitHub #3 — ソースコードの可読性の向上](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3)

**Problems to fix:**
- File naming: `topActivity.kt` → `TopActivity.kt` (PascalCase)
- Type naming: `data class item` → `data class RepositoryItem`
- Fragment naming: `OneFragment` / `TwoFragment` → `RepositorySearchFragment` / `RepositoryDetailFragment`
- Hungarian notation: `_binding`, `_viewModel`, `_layoutManager`, etc.
- Variable naming: `diff_util` → `diffUtil` (lowerCamelCase)
- Wildcard imports: `io.ktor.client.*`, `java.util.*`
- Inconsistent indentation (tab/space mixing)

**Solution:**
- Proper PascalCase naming for classes and files
- Remove Hungarian prefixes
- Enforce `.editorconfig` (4-space indent)
- Explicit single-class imports

---

## Issue #2: [GitHub #4 — ソースコードの安全性の向上](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4)

**Problems to fix:**
- Forced unwraps: `context!!`, `arguments!!`
- `lateinit` crash on process death (`lastSearchDate`)
- Unsafe JSON parsing without validation

**Solution:**
- Use `requireContext()`, safe accessors
- Remove all `!!` operators
- Handle process death via `SavedStateHandle`

---

## Issue #3: [GitHub #5 — バグを修正](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5)

**Problems to fix:**
- Typo: `forks_conut` (forks count always displays 0)
- UI thread blocking: `runBlocking` in `searchResults()`
- Memory leaks: ViewBinding references, Context retention

**Solution:**
- Fix typo to `forks_count`
- Use structured coroutines on background dispatchers
- Clear ViewBinding in `onDestroyView()`

---

## Issue #4: [GitHub #6 — Fat Fragment の回避](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6)

**Problems to fix:**
- Business logic in Fragment
- Direct network calls in UI layer

**Solution:**
- Extract logic to `RepositorySearchViewModel`
- Separate UI observation from data fetching

---

## Issue #5: [GitHub #7 — プログラム構造をリファクタリング](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7)

**Problems to fix:**
- Single module monolith (`:app`)
- Mixed concerns between presentation and data

**Solution:**
- Multi-module architecture (`:core:model`, `:core:data`, `:core:domain`, `:feature:*`)
- Clear layer separation and dependency inversion

---

## Issue #6: [GitHub #8 — アーキテクチャを適用](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8)

**Problems to fix:**
- No dependency injection
- Tightly coupled components
- Unpredictable state mutations

**Solution:**
- Hilt DI implementation
- Clean Architecture (Domain -> Data -> UI)
- Unidirectional Data Flow (UDF) with `StateFlow`

---

## Issue #7: [GitHub #9 — テストを追加](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9)

**Problems to fix:**
- No automated tests
- Untestable monolithic code

**Solution:**
- Unit tests for ViewModels with `Turbine` and `MockEngine`
- Virtual time coroutine testing (`StandardTestDispatcher`)
- Repository & Mapper unit tests

---

## Issue #8: [GitHub #10 — UI をブラッシュアップ](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10)

**Problems to fix:**
- Legacy XML layouts
- No dark mode support
- Hardcoded strings without localization

**Solution:**
- 100% Declarative Jetpack Compose migration
- Material 3 theme & dynamic color support
- Dark mode + Bilingual Japanese & English localization

---

## Issue #9: [GitHub #11 — 新機能を追加](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11)

**Features to add:**
- Chrome Custom Tabs integration
- Repository sorting (Stars, Forks, Updated)
- Settings screen for language and theme toggling
- Offline cache & mock product flavor

---

## Progress Tracking

- [x] Foundation setup & Documentation architecture
- [x] CI/CD automated pipeline (`ci-pr-quality-gate.yml`)
- [ ] Issues #1–3 (Beginner level - Code health & readability)
- [ ] Issues #4–6 (Intermediate level - Architecture & modularity)
- [ ] Issues #7–8 (Testing & Jetpack Compose UI)
- [ ] Issue #9 (Bonus features & Headless KMP)
- [ ] Release v1.0.0
