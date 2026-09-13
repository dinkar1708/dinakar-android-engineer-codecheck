# Testing Guide 2: Jetpack Compose View & Screen Testing (Robolectric JVM)

**Scope:** Level 2 & 3 — Compose UI Component & Screen Testing  
**Target:** Declarative Jetpack Compose UI Screens, Rows, Tokens & Error/Empty/Loading Views  

---

## 1. Overview & Architectural Philosophy

Jetpack Compose UI tests verify the visual rendering, user interactions, and semantic state trees of our design system components and feature screens.

> [!IMPORTANT]
> ### 🚨 Critical Directory Clarification: Where do Compose UI Tests Live?
> In this repository, **all Compose component and screen UI tests live in `src/test/`** using **Robolectric on the local JVM**, **NOT** in `src/androidTest/`.
> 
> **Why `src/test/` (Robolectric) instead of `src/androidTest/` for feature modules?**
> 1. **Execution Speed:** Robolectric executes Compose UI tests on the local JVM in **~2 to 3 seconds**. Running on-device `androidTest` takes minutes to start up an emulator.
> 2. **Headless CI/CD Compatibility:** JVM tests run seamlessly on GitHub Actions runners without requiring hardware acceleration (HAXM / KVM).
> 3. **Code Coverage Integration:** Robolectric test executions feed directly into **Kover**, enabling our automated test suite to achieve **81.0% line coverage**.
> 4. **Module Decoupling:** Feature modules (`feature:search`, `feature:detail`, `core:ui`) do NOT require an `androidTest` directory or an application context. They test pure composables in isolation.

---

## 2. Directory & Folder Structure (Compose UI Tests)

```text
📁 Repository Root
├── 📁 feature/search/
│    └── 📁 src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/
│         ├── 📄 SearchScreenTest.kt        <-- 6 Screen-level tests (Idle, Loading, Empty, Error, Success)
│         └── 📄 RepositoryItemRowTest.kt    <-- 3 Card component tests (Title, Owner, Click interaction)
│
├── 📁 feature/detail/
│    └── 📁 src/test/kotlin/jp/co/yumemi/android/codecheck/feature/detail/
│         ├── 📄 DetailScreenTest.kt        <-- 3 Screen-level tests (Loading, Error, Success + Back click)
│         └── 📄 DetailContentTest.kt       <-- 3 Content layout tests (Stars, Forks, Issues, Language)
│
└── 📁 core/ui/
     └── 📁 src/test/kotlin/jp/co/yumemi/android/codecheck/core/ui/component/
          ├── 📄 EmptyViewTest.kt           <-- 3 Empty state view tests (Icon, Title, Message)
          ├── 📄 ErrorViewTest.kt           <-- 3 Error state view tests (Message, Retry button action)
          └── 📄 LoadingViewTest.kt         <-- 2 CircularProgressIndicator tests
```

---

## 3. Screen-Level State Hoisting Architecture

To make `SearchScreen` and `DetailScreen` 100% testable in JVM unit tests without needing Hilt dependency injection, each screen follows the **Stateless Hoisted Composable Pattern**:

```kotlin
// 1. Stateful container (wires Hilt ViewModel & Navigation)
@Composable
fun SearchScreen(
    onNavigateToDetail: (RepositoryItem) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        uiState = uiState,
        searchQuery = viewModel.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSearch = viewModel::onSearch,
        onClearQuery = viewModel::clearQuery,
        onItemClick = onNavigateToDetail
    )
}

// 2. Stateless Composable (Tested in SearchScreenTest.kt without ViewModel)
@Composable
internal fun SearchScreen(
    uiState: SearchUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    onItemClick: (RepositoryItem) -> Unit
) {
    // Pure Compose UI layout rendering
}
```

---

## 4. Key Test Scenarios Covered

### A. `SearchScreenTest.kt` (6 Tests)
1. `idleState_displaysGuidanceMessage`: Verifies welcome title and "Type a search query above" guidance.
2. `loadingState_displaysLoadingIndicator`: Verifies `LoadingView` appears during active search requests.
3. `emptyState_displaysNoResultsMessage`: Verifies empty state illustration and guidance when GitHub returns 0 results.
4. `errorState_displaysErrorMessage`: Verifies error message banner and retry button on network failures.
5. `successState_displaysRepositoryList`: Verifies repository cards render with names, stars, and language badges.
6. `searchQueryInput_invokesCallbacks`: Verifies text input changes, clear button visibility, and submission triggers.

### B. `DetailScreenTest.kt` (3 Tests)
1. `loadingState_displaysLoadingIndicator`: Verifies loading spinner while fetching repository detail.
2. `errorState_displaysErrorMessage`: Verifies error message if repository detail fails to resolve.
3. `successState_displaysRepositoryDetails_andBackClickInvokesCallback`: Verifies full repository statistics (stars, watchers, forks, open issues) and back arrow navigation click callback.

### C. `core:ui` Components (8 Tests)
- `EmptyViewTest`: Verifies title, optional description, and search/inbox icons.
- `ErrorViewTest`: Verifies error message rendering and callback execution when clicking "Retry".
- `LoadingViewTest`: Verifies indeterminate progress bar visibility.

---

## 5. Execution Commands

```bash
# Run all Compose UI tests across features and core:ui
./gradlew :feature:search:testDebugUnitTest :feature:detail:testDebugUnitTest :core:ui:testDebugUnitTest

# Run search screen UI tests only
./gradlew :feature:search:testDebugUnitTest --tests "*.SearchScreenTest"

# Run detail screen UI tests only
./gradlew :feature:detail:testDebugUnitTest --tests "*.DetailScreenTest"
```

---

## 6. Testing Documentation Index

- **[Master Testing Overview](./readme.md)**: Architecture, test pyramid, and directory layout.
- **[Guide 1: Unit & ViewModel Testing](./01_unit_testing.md)**: StateFlow UDF & Turbine testing in `src/test/`.
- **[Guide 2: Compose View Testing](./02_compose_ui_testing.md)**: *(Current document)*
- **[Guide 3: Integration & E2E Testing](./03_integration_and_e2e_testing.md)**: Ktor MockEngine & live device E2E in `app/src/androidTest/`.
- **[Traceability Matrix](./04_test_cases_matrix.md)**: Complete mapping from requirements to test classes.
