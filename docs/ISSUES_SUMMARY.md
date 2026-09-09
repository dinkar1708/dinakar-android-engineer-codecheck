# Issues Summary

All **9 Yumemi challenge issues** will be addressed through incremental PRs.

---

## Issue Status

| # | Title | Level | Status |
|:---:|:------|:-----:|:------:|
| #1 | ソースコードの可読性の向上 | 初級 | 📋 Planned |
| #2 | ソースコードの安全性の向上 | 初級 | 📋 Planned |
| #3 | バグを修正 | 初級 | 📋 Planned |
| #4 | Fat Fragment の回避 | 初級 | 📋 Planned |
| #5 | プログラム構造をリファクタリング | 中級 | 📋 Planned |
| #6 | アーキテクチャを適用 | 中級 | 📋 Planned |
| #7 | テストを追加 | 中級 | 📋 Planned |
| #8 | UI をブラッシュアップ | ボーナス | 📋 Planned |
| #9 | 新機能を追加 | ボーナス | 📋 Planned |

**Legend**: 初級 = Beginner, 中級 = Intermediate, ボーナス = Bonus

---

## Issue #1: Code Readability

**Problems to fix:**
- File naming: `topActivity.kt` → `TopActivity.kt`
- Hungarian notation: `_binding`, `_viewModel`
- Wildcard imports
- Inconsistent code style

**Solution:**
- Proper PascalCase naming
- Remove Hungarian prefixes
- Add `.editorconfig`
- Explicit imports

---

## Issue #2: Code Safety

**Problems to fix:**
- Forced unwraps: `context!!`, `arguments!!`
- `lateinit` crash on process death
- Unsafe JSON parsing

**Solution:**
- Use `requireContext()`, safe accessors
- Remove all `!!` operators
- Handle process death properly

---

## Issue #3: Bug Fixes

**Problems to fix:**
- Typo: `forks_conut` (forks always 0)
- UI thread blocking: `runBlocking`
- Memory leaks: ViewBinding, Context

**Solution:**
- Fix typo to `forks_count`
- Use coroutines properly
- Clear ViewBinding in `onDestroyView()`

---

## Issue #4: Avoid Fat Fragment

**Problems to fix:**
- Business logic in Fragment
- Network calls in UI layer

**Solution:**
- Extract logic to ViewModel
- Separate concerns properly

---

## Issue #5: Program Structure

**Problems to fix:**
- Single module monolith
- Mixed concerns

**Solution:**
- Multi-module architecture
- Clear layer separation

---

## Issue #6: Architecture

**Problems to fix:**
- No dependency injection
- Tightly coupled components

**Solution:**
- Hilt DI implementation
- Clean Architecture
- Unidirectional Data Flow

---

## Issue #7: Testing

**Problems to fix:**
- No tests
- Untestable code

**Solution:**
- Unit tests for ViewModels
- Integration tests
- UI tests

---

## Issue #8: UI Polish

**Problems to fix:**
- XML layouts
- No dark mode
- No localization

**Solution:**
- Jetpack Compose migration
- Material 3 design
- Dark mode + i18n

---

## Issue #9: New Features (Bonus)

**Features to add:**
- Chrome Custom Tabs
- Repository sorting
- Settings screen
- Offline mode
- Product flavors

---

## Progress Tracking

- [ ] Foundation setup (documentation)
- [ ] Issues #1-3 (Beginner level - Code health)
- [ ] Issues #4-6 (Intermediate level - Architecture)
- [ ] Issues #7-8 (Testing & UI modernization)
- [ ] Issue #9 (Bonus features)
- [ ] Release v1.0.0

**Note:** Each issue will be completed through one or more focused PRs.

---

**Current Status**: Planning complete, ready to begin implementation
