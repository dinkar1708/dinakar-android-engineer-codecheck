# Issues Summary

All **9 code challenge issues** (mapped to **GitHub Issues #3 through #11**, see [Technical References](../references.md#7-github-api--assessment-standards)) are tracked and delivered through incremental PRs.

---

## Issue Status & GitHub Mapping

> [!NOTE]
> **Issue Number Mapping & Offset Rationale (+2):**  
> This repository was initially configured as a **Private** repository. Due to GitHub Actions CI execution and billing limits on private repositories, PR #1 (build fixes) and PR #2 (foundational documentation) were created and merged before running the automated issue copying action (`copy-issues.yml`). Because GitHub allocates a single shared sequential counter for both PRs and Issues, Challenge Issues #1 through #9 are tracked as **GitHub Issues #3 through #11**.

| Challenge Task | GitHub Issue | Title | Level | Status | Merged PR(s) |
|:---:|:---:|:------|:-----:|:------:|:---|
| **#1** | [**#3**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | ソースコードの可読性の向上 | 初級 | ✅ Closed | [#14](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/14) (`close #3`) |
| **#2** | [**#4**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | ソースコードの安全性の向上 | 初級 | ✅ Closed | [#15](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/15) (`close #4`) |
| **#3** | [**#5**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | バグを修正 | 初級 | ✅ Closed | [#18](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/18) (`close #5`) |
| **#4** | [**#6**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | Fat Fragment の回避 | 初級 | ✅ Closed | [#19](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/19) (`close #6`) |
| **#5** | [**#7**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | プログラム構造をリファクタリング | 中級 | ✅ Closed | [#21](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/21) (`close #7`) |
| **#6** | [**#8**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | アーキテクチャを適用 | 中級 | ✅ Closed | [#25](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/25), [#26](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/26), [#27](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/27) (`closes #8`) |
| **#7** | [**#9**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | テストを追加 | 中級 | ✅ Closed | [#28](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/28), [#29](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/29), [#30](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/30), [#31](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/31), [#32](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/32) (`close #9`) |
| **#8** | [**#10**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/10) | UI をブラッシュアップ | ボーナス | ✅ Closed | [#33](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/33), [#34](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/34), [#39](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/39), [#40](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/40) (`refs #10`) |
| **#9** | [**#11**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11) | 新機能を追加 | ボーナス | ✅ Closed | [#35](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/35), [#37](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/37), [#38](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/38), [#41](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/41), [#42](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/42) (`refs #11`) |

**Legend**: 初級 = Beginner, 中級 = Intermediate, ボーナス = Bonus

> 💡 **Atomic PR Strategy & Complete 33 Merged PRs Delivery:**  
> All **33 pull requests** in this repository (spanning PR #1 through #42, accounting for shared GitHub issue counters #3–#11) are active, merged into `dev`, and validated through CI quality gates. Following Yumemi's agile review guidelines, complex tasks were intentionally broken down into **atomic PRs (< 300 diff lines)** rather than risky monolithic merges:
> - **22 Challenge Feature PRs:** Incrementally delivered and closed Challenge Issues #3–#11 (exhaustively listed above).
> - **11 Foundation & Governance PRs:** Established Gradle 8.5 builds, CI test/Detekt pipelines, 8 ADRs, and AI skills ([#1](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/1), [#2](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/2), [#12](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/12), [#13](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/13), [#16](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/16), [#17](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/17), [#20](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/20), [#22](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/22), [#23](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/23), [#24](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/24), [#36](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/pull/36)).

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

**UI Polish & Responsive Multi-Device Design**

Migrated legacy XML layouts to 100% Jetpack Compose with Material 3 theming, dark mode support, and dynamic runtime bilingual localization (English/日本語). Thoroughly engineered and validated for **responsive design across Mobile Phones and Tablets** in both **Vertical (Portrait) and Horizontal (Landscape)** orientations with adaptive `FlowRow` content wrapping and landscape scroll defense.

---

## Issue #9: [GitHub #11 — 新機能を追加](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/11)

**Bonus Features**

Added production-ready enhancements: persistent repository bookmarking (Starred), Chrome Custom Tabs for external GitHub URLs, bottom navigation tabs, dynamic runtime theme and language settings, 100% offline mock flavor (with 12 deterministic edge cases), and a native iOS SwiftUI companion app consuming shared KMP business logic.

---

## Progress Tracking

- [x] Foundation setup & Documentation architecture
- [x] CI/CD automated pipeline
- [x] Sprint 1: Code health & readability (GitHub #3, #4, #5)
- [x] Sprint 2: Architecture & modularity (GitHub #6, #7, #8 — PRs 8.1, 8.2, 8.3)
- [x] Sprint 3: Testing & UI polish (GitHub #9, #10)
- [x] Sprint 4: Bonus features & Release v1.0.0 (GitHub #11)
