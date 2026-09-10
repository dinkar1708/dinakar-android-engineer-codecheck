# Testing Specification: Test Cases Traceability Matrix

**Scope:** Phase 3 (Iteration & Construction)  
**Target:** Requirements Traceability & Verification Mapping  

---

## 1. Overview

This matrix establishes complete bidirectional traceability between functional requirements, edge cases, stability objectives, and automated test suites across the repository.

---

## 2. Complete Test Traceability Matrix

| Requirement Area | Test Class & Location | Test Method / Assertion | Category |
| :--- | :--- | :--- | :--- |
| **API Deserialization** | [`GitHubApiServiceTest`](../../../core/network/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/api/GitHubApiServiceTest.kt) | `searchRepositories_whenApiReturns200_deserializesValidPayload`: Parses GitHub JSON into typed network/domain entities | Integration |
| **HTTP 403 Rate Limiting** | [`GitHubApiServiceTest`](../../../core/network/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/api/GitHubApiServiceTest.kt) | `searchRepositories_whenRateLimitExceeded_throwsHttpException`: Correctly catches and translates HTTP 403 status | Integration |
| **Blank Query Handling** | [`GitHubRepositoryImplTest`](../../../core/data/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImplTest.kt) | `searchRepositories_whenQueryIsBlank_returnsEmptyListWithoutNetwork`: Returns empty list immediately without network request | Integration |
| **In-Memory Caching** | [`GitHubRepositoryImplTest`](../../../core/data/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImplTest.kt) | `searchRepositories_whenQueryCached_returnsCachedResult`: Caches and returns previously fetched repositories for identical query strings | Integration |
| **Cold Start Idle State** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | `initial_state_is_Idle_when_no_saved_state_query_exists`: Verifies initial UI state is `SearchUiState.Idle` | Unit |
| **Search Query Flow** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | `searchRepositories_with_valid_query_transitions_to_Success`: Turbines `Idle` &rarr; `Loading` &rarr; `Success` | Unit |
| **Zero Results Handling** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | `searchRepositories_with_empty_results_transitions_to_Empty`: Turbines `Idle` &rarr; `Loading` &rarr; `Empty` | Unit |
| **Network Error Mapping** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | `searchRepositories_on_network_error_transitions_to_Error`: Turbines `Idle` &rarr; `Loading` &rarr; `Error(message)` | Unit |
| **Domain Model Immutability** | [`RepositoryItemTest`](../../../core/domain/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/domain/model/RepositoryItemTest.kt) | Verifies domain model immutability and fallback defaults | Unit |
| **UI 5 Visual States** | [`SearchScreenTest`](../../../feature/search/src/androidTest/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchScreenTest.kt) | Verifies Compose Semantics Tree for `Idle`, `Loading`, `Success`, `Empty`, and `Error` states via `SearchTestTags` | UI |
| **Search End-to-End Flow** | [`SearchE2ETest`](../../../app/src/androidTest/kotlin/jp/co/yumemi/android/codecheck/SearchE2ETest.kt) | Full instrumented flow: query entry &rarr; list rendering &rarr; item click navigation | Instrumentation |

---

## 3. Cross-References

- [Master Testing Overview](./readme.md)
- [Unit Testing Specification](./01_unit_testing.md)
- [Integration Testing Specification](./02_integration_testing.md)
- [Compose UI Testing Specification](./03_compose_ui_testing.md)
