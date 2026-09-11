# Testing Specification: Test Cases Traceability Matrix

**Scope:** Level 1 through Level 4 Verification  
**Target:** Requirements Traceability & Verification Mapping across All Modules  

---

## 1. Overview

This matrix establishes complete bidirectional traceability between functional requirements, edge cases, stability objectives, and automated test suites across the repository.

All **81 automated unit and Robolectric UI tests** plus the **live on-device E2E test** are mapped below.

---

## 2. Complete Test Traceability Matrix

| Requirement Area | Test Class & Location | Verification Scope / Assertions | Test Category | Execution Environment |
| :--- | :--- | :--- | :--- | :--- |
| **API Deserialization** | [`GitHubApiServiceTest`](../../../core/network/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/api/GitHubApiServiceTest.kt) | Parses GitHub JSON into typed entities; verifies optional fields like language/description | Network Integration | Local JVM (Ktor MockEngine) |
| **HTTP 403 Rate Limiting** | [`GitHubApiServiceTest`](../../../core/network/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/api/GitHubApiServiceTest.kt) | Catches HTTP 403 Forbidden and maps to domain `NetworkException.RateLimitExceeded` | Network Integration | Local JVM (Ktor MockEngine) |
| **Network Exceptions** | [`NetworkExceptionTest`](../../../core/network/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/error/NetworkExceptionTest.kt) | Validates exception hierarchy: Offline, RateLimit, ServerError, Unknown | Unit | Local JVM |
| **Blank Query Short-Circuit** | [`GitHubRepositoryImplTest`](../../../core/data/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImplTest.kt) | Returns empty list immediately without making a network request | Data Integration | Local JVM |
| **In-Memory Caching** | [`GitHubRepositoryImplTest`](../../../core/data/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImplTest.kt) | Caches query responses and returns cached results on identical queries | Data Integration | Local JVM |
| **Exponential Backoff Retry** | [`RetryUtilTest`](../../../core/data/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/util/RetryUtilTest.kt) | Verifies exponential backoff delay, max retry limit, and CancellationException preservation | Data Integration | Local JVM |
| **Domain Immutability** | [`RepositoryItemTest`](../../../core/domain/src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/domain/model/RepositoryItemTest.kt) | Verifies domain model immutability, data class copy semantics, and fallback defaults | Domain Unit | Local JVM |
| **Search ViewModel Idle** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | Initial state is `SearchUiState.Idle` when no saved query exists | ViewModel Unit | Local JVM (Turbine) |
| **Search Query Transitions** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | Transitions cleanly: `Idle` &rarr; `Loading` &rarr; `Success(repositories)` | ViewModel Unit | Local JVM (Turbine) |
| **Zero Results Handling** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | Transitions: `Idle` &rarr; `Loading` &rarr; `Empty` | ViewModel Unit | Local JVM (Turbine) |
| **Network Error Recovery** | [`SearchViewModelTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchViewModelTest.kt) | Transitions: `Idle` &rarr; `Loading` &rarr; `Error(message)` | ViewModel Unit | Local JVM (Turbine) |
| **Detail ViewModel Success** | [`DetailViewModelTest`](../../../feature/detail/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/detail/DetailViewModelTest.kt) | Successfully resolves repository detail from SavedStateHandle | ViewModel Unit | Local JVM (Turbine) |
| **Search Screen UI (5 States)** | [`SearchScreenTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/SearchScreenTest.kt) | Verifies Semantics Tree for `Idle`, `Loading`, `Success`, `Empty`, `Error` states and query input | Compose UI | Local JVM (Robolectric) |
| **Repository Card Item** | [`RepositoryItemRowTest`](../../../feature/search/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/search/RepositoryItemRowTest.kt) | Verifies card layout, repository title, owner avatar description, and item click callbacks | Compose UI | Local JVM (Robolectric) |
| **Detail Screen UI** | [`DetailScreenTest`](../../../feature/detail/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/detail/DetailScreenTest.kt) | Verifies Loading, Error, and Success state rendering with Back arrow navigation click | Compose UI | Local JVM (Robolectric) |
| **Detail Content Card** | [`DetailContentTest`](../../../feature/detail/src/test/kotlin/jp/co/yumemi/android/codecheck/feature/detail/DetailContentTest.kt) | Verifies stats layout: stars, watchers, forks, open issues, language badge | Compose UI | Local JVM (Robolectric) |
| **Empty State View** | [`EmptyViewTest`](../../../core/ui/src/test/kotlin/jp/co/yumemi/android/codecheck/core/ui/component/EmptyViewTest.kt) | Verifies empty state illustration, title, and optional description text | Compose UI | Local JVM (Robolectric) |
| **Error State View** | [`ErrorViewTest`](../../../core/ui/src/test/kotlin/jp/co/yumemi/android/codecheck/core/ui/component/ErrorViewTest.kt) | Verifies error message banner and retry button click action | Compose UI | Local JVM (Robolectric) |
| **Loading View** | [`LoadingViewTest`](../../../core/ui/src/test/kotlin/jp/co/yumemi/android/codecheck/core/ui/component/LoadingViewTest.kt) | Verifies indeterminate circular progress indicator rendering | Compose UI | Local JVM (Robolectric) |
| **URL Deep Link Parser** | [`ExtractOwnerAndRepoTest`](../../../app/src/test/kotlin/jp/co/yumemi/android/codecheck/navigation/ExtractOwnerAndRepoTest.kt) | Validates deep link parser extracting owner and repo from URL strings | Navigation Unit | Local JVM |
| **Navigation Route Builder** | [`ScreenRouteTest`](../../../app/src/test/kotlin/jp/co/yumemi/android/codecheck/navigation/ScreenRouteTest.kt) | Verifies Compose Navigation route strings and URL parameter encoding | Navigation Unit | Local JVM |
| **Hilt DI Bindings** | [`DependencyInjectionTest`](../../../app/src/test/kotlin/jp/co/yumemi/android/codecheck/di/DependencyInjectionTest.kt) | Validates singleton repository and network service injection graph | DI Unit | Local JVM |
| **Live E2E User Journey** | [`SearchE2ETest`](../../../app/src/androidTest/kotlin/jp/co/yumemi/android/codecheck/SearchE2ETest.kt) | Full live user journey: search "kotlin" &rarr; list render &rarr; click card &rarr; view detail &rarr; back navigation | E2E Instrumented | Android Device / Emulator |

---

## 3. Cross-References

- **[Master Testing Overview](./readme.md)**
- **[Guide 1: Unit & ViewModel Testing](./01_unit_testing.md)**
- **[Guide 2: Compose View Testing](./02_compose_ui_testing.md)**
- **[Guide 3: Integration & E2E Testing](./03_integration_and_e2e_testing.md)**
