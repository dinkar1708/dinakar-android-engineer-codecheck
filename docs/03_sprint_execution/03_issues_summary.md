# Issues Summary

All **9 Yumemi challenge issues** (mapped to **GitHub Issues #3 through #11**) will be addressed through incremental PRs.

---

## Issue Status & GitHub Mapping

> [!NOTE]
> **Issue Number Mapping & Offset Rationale (+2):**  
> This repository was initially configured as a **Private** repository. Due to GitHub Actions CI execution and billing limits on private repositories, PR #1 (build fixes) and PR #2 (foundational documentation) were created and merged before running the automated issue copying action (`copy-issues.yml`). Because GitHub allocates a single shared sequential counter for both PRs and Issues, Yumemi Challenge Issues #1 through #9 are tracked as **GitHub Issues #3 through #11**.

| Yumemi Challenge | GitHub Issue | Title | Level | Status |
|:---:|:---:|:------|:-----:|:------:|
| **#1** | [**#3**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | ソースコードの可読性の向上 | 初級 | ✅ Done (PR #17) |
| **#2** | [**#4**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | ソースコードの安全性の向上 | 初級 | ✅ Done (PR #18) |
| **#3** | [**#5**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | バグを修正 | 初級 | ✅ Done (PR #19) |
| **#4** | [**#6**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | Fat Fragment の回避 | 初級 | ✅ Done (PR #20) |
| **#5** | [**#7**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | プログラム構造をリファクタリング | 中級 | ✅ Done (PR #21) |
| **#6** | [**#8**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | アーキテクチャを適用 | 中級 | ✅ Done (PRs 8.1, 8.2, 8.3) |
| **#7** | [**#9**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | テストを追加 | 中級 | 🎯 Active / Next |
| **#8** | [**#10**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10) | UI をブラッシュアップ | ボーナス | 📋 Planned |
| **#9** | [**#11**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11) | 新機能を追加 | ボーナス | 📋 Planned |

**Legend**: 初級 = Beginner, 中級 = Intermediate, ボーナス = Bonus

---

## Issue #1: [GitHub #3 — ソースコードの可読性の向上](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3)

**Code Readability**

The codebase has naming convention issues including `topActivity.kt` (should be PascalCase), Hungarian notation prefixes (`_binding`, `_viewModel`), and generic names like `OneFragment`. Also includes wildcard imports and inconsistent indentation. Needs improvement following Kotlin coding conventions and consistent formatting standards.

---

## Issue #2: [GitHub #4 — ソースコードの安全性の向上](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4)

**Code Safety**

The code uses forced unwraps (`context!!`, `arguments!!`) which can crash, and `lateinit` variables that don't survive process death. Needs safer null handling and process death resilience.

---

## Issue #3: [GitHub #5 — バグを修正](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5)

**Bug Fixes**

There's a typo in the API parsing (`forks_conut` instead of `forks_count`) causing incorrect data display. The code also blocks the UI thread with `runBlocking` causing ANRs, and has ViewBinding memory leaks. These bugs need to be identified and fixed.

---

## Issue #4: [GitHub #6 — Fat Fragment の回避](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6)

**Avoid Fat Fragment**

All business logic and network calls are directly in the Fragment, violating separation of concerns. The Fragment handles too many responsibilities and needs refactoring.

---

## Issue #5: [GitHub #7 — プログラム構造をリファクタリング](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7)

**Program Structure**

The app is a single monolithic `:app` module with mixed presentation and data concerns. Needs better modularization and layer separation.

---

## Issue #6: [GitHub #8 — アーキテクチャを適用](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8)

**Architecture**

No dependency injection framework, tightly coupled components, and unpredictable state management. Needs proper architecture pattern implementation.

---

## Issue #7: [GitHub #9 — テストを追加](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9)

**Testing**

Zero automated tests and untestable monolithic code structure. Needs comprehensive test coverage and testable architecture.

---

## Issue #8: [GitHub #10 — UI をブラッシュアップ](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10)

**UI Polish**

Uses legacy XML layouts with no dark mode or localization support. UI needs modernization with better theming and internationalization.

---

## Issue #9: [GitHub #11 — 新機能を追加](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11)

**Bonus Features**

Opportunity to add production-ready enhancements: in-app browsing, repository sorting, user preferences, offline support, and testing infrastructure improvements.

---

## Progress Tracking

- [x] Foundation setup & Documentation architecture
- [x] CI/CD automated pipeline
- [x] Sprint 1: Code health & readability (GitHub #3, #4, #5)
- [x] Sprint 2: Architecture & modularity (GitHub #6, #7, #8 — PRs 8.1, 8.2, 8.3)
- [ ] Sprint 3: Testing & UI polish (GitHub #9, #10)
- [ ] Sprint 4: Bonus features & Release v1.0.0 (GitHub #11)
