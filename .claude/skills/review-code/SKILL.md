---
name: review-code
description: Use this skill when performing code reviews against Android, Kotlin, SOLID principles, and Yumemi evaluation standards.
---

# Code Review Guidelines

Review checklist aligned with Yumemi's Qiita evaluation criteria and our codebase architecture.

## 📱 Current Tech Stack Context
- **Build Infrastructure:** Composite `build-logic` Convention Plugins + Version Catalog (`libs.versions.toml`)
- **Module Structure:** Multi-module architecture (`:core:domain`, `:core:network`, `:core:data`, `:shared-core`, `:core:designsystem`, `:core:ui`, `:app`, with future `:core:database` in Issue #9)
- **UI:** ViewBinding (migrating to Jetpack Compose in PR 8.3)
- **Architecture:** MVVM with Repository pattern (migrating to StateFlow UDF & Hilt in PR 8.3)
- **Async:** Coroutines + LiveData (transitioning to StateFlow)
- **Network:** Ktor HTTP Client (migrating to KMP Ktor in PR 8.2)

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
- [ ] **ViewBinding Cleanup**: Is `_binding = null` set in Fragment `onDestroyView()`? (Current codebase uses Activities with ViewBinding, but this applies if Fragments are used)
- [ ] **Coroutine Scope**: Are jobs launched within `viewModelScope` or `lifecycleScope`? No `GlobalScope` or blocking `runBlocking`.
- [ ] **LiveData Observation**: Are LiveData observers removed properly or using lifecycle-aware observers?

### 3. Code Hygiene & Kotlin Idioms
- [ ] No Hungarian notation (`m_var`, `_var` except for private backing properties).
- [ ] No wildcard imports (`import foo.bar.*`).
- [ ] Favor immutability (`val` over `var`, immutable collections).
- [ ] Strings and dimensions extracted into resources (`R.string`, `R.dimen`).

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
- [ ] `./gradlew test` passes 100%.
- [ ] No unhandled exceptions or swallowed error states.
- [ ] Follows project conventions in `docs/CONTRIBUTING.md`.
- [ ] Skills and documentation updated if new designs were added (`docs/01_company_and_team/10_ai_agent_skills_guide.md`).

## 📂 Related Relative Paths
- **API Client**: `app/src/main/kotlin/jp/co/yumemi/android/code_check/api/GitHubApiClient.kt`
- **JSON Mapper**: `app/src/main/kotlin/jp/co/yumemi/android/code_check/api/RepositoryMapper.kt`
- **Repository Interface**: `app/src/main/kotlin/jp/co/yumemi/android/code_check/repository/GitHubRepository.kt`
- **Repository Implementation**: `app/src/main/kotlin/jp/co/yumemi/android/code_check/repository/GitHubRepositoryImpl.kt`
- **Search ViewModel**: `app/src/main/kotlin/jp/co/yumemi/android/code_check/RepositorySearchViewModel.kt`
- **Review Guidelines**: `docs/01_company_and_team/04_code_review_guidelines.md`
- **Yumemi Review Culture**: `docs/01_company_and_team/09_yumemi_review_culture.md`
- **Engineering Guardrails**: `docs/01_company_and_team/06_engineering_guardrails.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`

