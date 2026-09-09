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
| **API Deserialization** | [`DefaultGitHubRepositoryTest`](../../../shared/src/commonTest/kotlin/jp/co/yumemi/android/code_check/shared/data/repository/DefaultGitHubRepositoryTest.kt) | `searchRepositories_returns_success_when_API_responds_200`: Parses GitHub JSON into typed `RepositoryItem` entities | Integration |
| **Blank Query Handling** | [`DefaultGitHubRepositoryTest`](../../../shared/src/commonTest/kotlin/jp/co/yumemi/android/code_check/shared/data/repository/DefaultGitHubRepositoryTest.kt) | `searchRepositories_returns_empty_when_query_is_blank`: Returns empty list immediately without network request | Integration |
| **HTTP 403 Rate Limiting** | [`DefaultGitHubRepositoryTest`](../../../shared/src/commonTest/kotlin/jp/co/yumemi/android/code_check/shared/data/repository/DefaultGitHubRepositoryTest.kt) | `searchRepositories_returns_failure_when_API_responds_403`: Maps non-2xx status code to `Result.failure` | Integration |
| **In-Memory Caching** | [`DefaultGitHubRepositoryTest`](../../../shared/src/commonTest/kotlin/jp/co/yumemi/android/code_check/shared/data/repository/DefaultGitHubRepositoryTest.kt) | Query caching returns previously fetched repositories for identical query strings | Integration |
| **Cold Start Idle State** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/code_check/feature/search/SearchViewModelTest.kt) | `initial_state_is_Idle_when_no_saved_state_query_exists`: Verifies initial UI state is `SearchUiState.Idle` | Unit |
| **Search Query Flow** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/code_check/feature/search/SearchViewModelTest.kt) | `searchRepositories_with_valid_query_transitions_to_Success`: Turbines `Idle` &rarr; `Loading` &rarr; `Success` | Unit |
| **Zero Results Handling** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/code_check/feature/search/SearchViewModelTest.kt) | `searchRepositories_with_empty_results_transitions_to_Empty`: Turbines `Idle` &rarr; `Loading` &rarr; `Empty` | Unit |
| **Network Error Mapping** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/code_check/feature/search/SearchViewModelTest.kt) | `searchRepositories_on_network_error_transitions_to_Error`: Turbines `Idle` &rarr; `Loading` &rarr; `Error(message)` | Unit |
| **Search Input Validation** | [`SearchQueryValidatorTest`](../../../core/domain/src/test/kotlin/jp/co/yumemi/android/code_check/core/domain/validator/SearchQueryValidatorTest.kt) | Validates query trimming, sanitization, and minimum search length constraints | Unit |
| **Settings State Management** | [`SettingsViewModelTest`](../../../feature/settings/src/test/kotlin/jp/co/yumemi/android/code_check/feature/settings/SettingsViewModelTest.kt) | Verifies theme switching (Light/Dark/System) and sort order preference persistence | Unit |
| **Offline Mock Strategy** | [`MockGitHubRepositoryTest`](../../../shared/src/commonTest/kotlin/jp/co/yumemi/android/code_check/shared/data/repository/MockGitHubRepositoryTest.kt) | Verifies offline mock flavor repository returns deterministic mock results without network | Integration |
| **Search End-to-End Flow** | [`SearchE2ETest`](../../../app/src/androidTest/kotlin/jp/co/yumemi/android/code_check/SearchE2ETest.kt) | Full instrumented flow: query entry &rarr; list rendering &rarr; item click navigation | Instrumentation |

---

## 3. Cross-References

- [Master Testing Overview](./readme.md)
- [Unit Testing Specification](./01_unit_testing.md)
- [Integration Testing Specification](./02_integration_testing.md)
- [Compose UI Testing Specification](./03_compose_ui_testing.md)
