# Testing Specification: Unit Testing Strategy & Coroutines Infrastructure

**Scope:** Phase 3 (Iteration & Construction)  
**Target:** ViewModels, Domain Use Cases, StateFlow & Coroutines  

---

## 1. Overview & Scope

Unit testing forms the foundation (Level 1, ~60% of the suite) of our testing pyramid. Executing entirely on the local JVM in milliseconds without Android framework dependencies, unit tests guarantee deterministic verification of business logic, state machines, and asynchronous coroutines flows.

---

## 2. Presentation Layer Unit Tests (`SearchViewModelTest.kt`)

### Architecture & Test Doubles
- **Frameworks:** JUnit 4, Cash App Turbine, Kotlinx Coroutines `runTest`, `MainDispatcherRule`.
- **Test Double:** `FakeGitHubRepository` implementing the `GitHubRepository` interface with configurable responses, avoiding fragile mock verification.

### Core Test Cases & Assertions

#### 1. Initial State Verification
- **Test:** `initial_state_is_Idle_when_no_saved_state_query_exists`
- **Assertion:** ViewModel initialized with an empty `SavedStateHandle` exposes `SearchUiState.Idle`.

#### 2. Query Flow State Transitions (Turbine)
- **Test:** `searchRepositories_with_valid_query_transitions_to_Success`
- **Flow Assertions:**
  1. Initial emission: `SearchUiState.Idle`
  2. Action: `viewModel.searchRepositories("kotlin")`
  3. Intermediate emission: `SearchUiState.Loading`
  4. Final emission: `SearchUiState.Success(repositories)` with expected item count and attributes.

#### 3. Empty Search Result Handling
- **Test:** `searchRepositories_with_empty_results_transitions_to_Empty`
- **Flow Assertions:** Transitions cleanly from `Idle` &rarr; `Loading` &rarr; `SearchUiState.Empty`.

#### 4. Error Mapping & Graceful Recovery
- **Test:** `searchRepositories_on_network_error_transitions_to_Error`
- **Flow Assertions:** Transitions from `Idle` &rarr; `Loading` &rarr; `SearchUiState.Error(message)`, ensuring exceptions are caught and surfaced safely in UI state.

---

## 3. Coroutines Test Infrastructure: `MainDispatcherRule.kt`

Because Android's `Dispatchers.Main` relies on `Looper.getMainLooper()` (unavailable in pure JVM unit tests), `MainDispatcherRule` replaces `Dispatchers.Main` with a `TestDispatcher`:

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
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

## 4. Domain & Validator Unit Testing (`SearchQueryValidatorTest.kt`)

Validates core business rules before queries reach the network layer:
- **Trimming:** Leading and trailing whitespaces stripped.
- **Minimum Length:** Queries shorter than 2 characters rejected.
- **Blank Validation:** Pure whitespace queries rejected immediately without network emission.

---

## 5. Execution Commands

```bash
# Execute all unit tests across all modules
./gradlew testDebugUnitTest

# Execute unit tests in :feature:search only
./gradlew :feature:search:testDebugUnitTest

# Execute unit tests in :core:domain only
./gradlew :core:domain:testDebugUnitTest
```

---

## 6. Cross-References

- [Master Testing Overview](./readme.md)
- [Integration Testing Specification](./02_integration_testing.md)
- [Compose UI Testing Specification](./03_compose_ui_testing.md)
- [Test Cases Traceability Matrix](./04_test_cases_matrix.md)
