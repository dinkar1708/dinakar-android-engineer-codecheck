# Testing Guide 1: Unit & ViewModel Testing (JVM)

**Scope:** Level 1 — Pure Unit & ViewModel Testing  
**Target:** ViewModels, Domain Use Cases, StateFlow UDF & Coroutines Infrastructure  

---

## 1. Overview & Scope

Unit testing forms the foundation (Level 1) of our testing pyramid. Executing entirely on the local JVM in milliseconds without Android framework or emulator dependencies, unit tests guarantee deterministic verification of business logic, state machines, and asynchronous coroutines flows.

---

## 2. Directory & Folder Structure (100% JVM Execution)

All Unit and ViewModel tests reside in **`src/test/`** (for Android modules) or **`src/commonTest/`** (for Kotlin Multiplatform modules).

```text
📁 Repository Root
├── 📁 feature/search/
│    └── 📁 src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/
│         └── 📄 SearchViewModelTest.kt             <-- Search ViewModel Unit Tests
│
├── 📁 feature/detail/
│    └── 📁 src/test/kotlin/jp/co/yumemi/android/codecheck/feature/detail/
│         └── 📄 DetailViewModelTest.kt             <-- Detail ViewModel Unit Tests
│
├── 📁 core/domain/
│    └── 📁 src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/domain/model/
│         └── 📄 RepositoryItemTest.kt              <-- Domain Model Immutability Tests
│
├── 📁 core/data/
│    └── 📁 src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/util/
│         └── 📄 RetryUtilTest.kt                   <-- Exponential Backoff Utility Tests
│
├── 📁 core/network/
│    └── 📁 src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/error/
│         └── 📄 NetworkExceptionTest.kt            <-- HTTP Error Mapping Tests
│
└── 📁 app/
     └── 📁 src/test/kotlin/jp/co/yumemi/android/codecheck/
          ├── 📁 navigation/
          │    ├── 📄 ExtractOwnerAndRepoTest.kt    <-- Deep Link URL Parser Tests
          │    └── 📄 ScreenRouteTest.kt            <-- Navigation Route Builder Tests
          └── 📁 di/
               └── 📄 DependencyInjectionTest.kt    <-- Hilt Module Binding Tests
```

> [!NOTE]
> **Why `src/test/` instead of `src/androidTest/`?**  
> Unit and ViewModel tests contain no device-specific code. Running them in `src/test/` executes the entire suite in **~2-3 seconds on the host JVM**, allowing instant feedback in Android Studio and fast headless CI builds.

---

## 3. ViewModel Testing with Cash App Turbine (`SearchViewModelTest.kt`)

### Architecture & Test Doubles
- **Frameworks:** JUnit 4, Cash App Turbine, Kotlinx Coroutines `runTest`, MockK.
- **Test Double:** Mocked `GitHubRepository` interface with configurable responses, avoiding fragile mock verification.

### Core Test Cases & Assertions

#### 1. Initial State Verification
- **Test:** `initial state is Idle when no query is saved`
- **Assertion:** ViewModel initialized with an empty `SavedStateHandle` exposes `SearchUiState.Idle`.

#### 2. Query Flow State Transitions (Turbine)
- **Test:** `searchRepositories with valid query emits Loading then Success`
- **Flow Assertions:**
  1. Initial emission: `SearchUiState.Idle`
  2. Action: `viewModel.searchRepositories("kotlin")`
  3. Intermediate emission: `SearchUiState.Loading`
  4. Final emission: `SearchUiState.Success(repositories)` with expected item count and attributes.

#### 3. Empty Search Result Handling
- **Test:** `searchRepositories with empty results emits Loading then Empty`
- **Flow Assertions:** Transitions cleanly from `Idle` &rarr; `Loading` &rarr; `SearchUiState.Empty`.

#### 4. Error Mapping & Graceful Recovery
- **Test:** `searchRepositories with network failure emits Loading then Error`
- **Flow Assertions:** Transitions from `Idle` &rarr; `Loading` &rarr; `SearchUiState.Error(message)`, ensuring exceptions are caught and surfaced safely in UI state.

---

## 4. Coroutines Test Infrastructure: `MainDispatcherRule.kt`

Because Android's `Dispatchers.Main` relies on `Looper.getMainLooper()` (which is unavailable in standard JVM unit tests), `MainDispatcherRule` replaces `Dispatchers.Main` with a `TestDispatcher`:

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

---

## 5. Execution Commands

```bash
# Run all unit tests across all modules
./gradlew testDebugUnitTest

# Run unit tests for :feature:search only
./gradlew :feature:search:testDebugUnitTest

# Run unit tests for :feature:detail only
./gradlew :feature:detail:testDebugUnitTest

# Run unit tests for :app only (Navigation & DI)
./gradlew :app:testDebugUnitTest
```

---

## 6. Testing Documentation Index

- **[Master Testing Overview](./readme.md)**: Architecture, test pyramid, and directory layout.
- **[Guide 1: Unit & ViewModel Testing](./01_unit_testing.md)**: *(Current document)*
- **[Guide 2: Compose View Testing](./02_compose_ui_testing.md)**: Declarative Compose UI tests with Robolectric in `src/test/`.
- **[Guide 3: Integration & E2E Testing](./03_integration_and_e2e_testing.md)**: Ktor MockEngine & live device E2E in `app/src/androidTest/`.
- **[Traceability Matrix](./04_test_cases_matrix.md)**: Complete mapping from requirements to test classes.
