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
```

**Prerequisites:** JDK 17+, Android Studio Iguana+, Gradle 8.5

📖 **Full guide:** See [docs/GETTING_STARTED.md](./docs/GETTING_STARTED.md)

---

## 📚 Documentation

- **[Start Here](./docs/00_START_HERE.md)** ⭐ - Solution overview and reviewer navigation
- **[Getting Started](./docs/GETTING_STARTED.md)** - Build, run, and prerequisite instructions
- **[Architecture Overview](./docs/ARCHITECTURE_OVERVIEW.md)** - System design and technology choices
- **[Issues Summary](./docs/03_sprint_execution/03_issues_summary.md)** - Breakdown and roadmap for all 9 Yumemi issues
- **[Contributing Guidelines](./docs/CONTRIBUTING.md)** - Branch naming, commit conventions, and workflow
- **[Technical References](./docs/references.md)** - External references and architecture benchmarks
- **[Documentation Portal](./docs/readme.md)** - Full documentation index

---

## 📌 Issue Number Mapping (GitHub Issues vs Yumemi Challenge)

> [!NOTE]
> **Issue Number Offset Rationale:**  
> This repository was initially configured as a **Private** repository. Due to GitHub Actions CI execution and billing limits on private repositories, PR #1 (build fixes) and PR #2 (foundational documentation) were created and merged before running the automated issue copying action (`copy-issues.yml`). Because GitHub allocates a single shared counter for both Pull Requests and Issues, the 9 Yumemi challenge issues are mapped to **GitHub Issues #3 through #11** (an offset of +2).

| Yumemi Challenge Task | GitHub Issue | Milestone | Category / Level | Status |
|:---|:---:|:---:|:---:|:---:|
| **#1: ソースコードの可読性の向上** *(Improve Code Readability)* | [**Issue #3**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/3) | 課題 | 初級 (Beginner) | 🚀 In Progress |
| **#2: ソースコードの安全性の向上** *(Improve Code Safety)* | [**Issue #4**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/4) | 課題 | 初級 (Beginner) | 📋 Planned |
| **#3: バグを修正** *(Fix Bugs)* | [**Issue #5**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/5) | 課題 | 初級 (Beginner) | 📋 Planned |
| **#4: Fat Fragment の回避** *(Avoid Fat Fragment)* | [**Issue #6**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/6) | 課題 | 初級 (Beginner) | 📋 Planned |
| **#5: プログラム構造をリファクタリング** *(Refactor Program Structure)* | [**Issue #7**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/7) | 課題 | 中級 (Intermediate) | 📋 Planned |
| **#6: アーキテクチャを適用** *(Apply Architecture)* | [**Issue #8**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/8) | 課題 | 中級 (Intermediate) | 📋 Planned |
| **#7: テストを追加** *(Add Tests)* | [**Issue #9**](https://github.com/dinkar1708/dinakar-android-engineer-codecheck/issues/9) | 課題 | 中級 (Intermediate) | 📋 Planned |
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

## 🧪 Features

### Implemented
- GitHub repository search
- Repository detail view
- Offline support

### Planned
- Modern Android architecture
- Jetpack Compose UI
- Comprehensive testing
- Dark mode & Internationalization

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
