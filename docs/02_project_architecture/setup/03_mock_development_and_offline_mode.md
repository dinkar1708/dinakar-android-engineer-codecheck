# Offline Mock Development & API-Independent Testing

This document details the architecture, product flavors, and execution guide for the **100% Offline Mock Development Mode** and **Gradle Build Flavors** (`dev`, `mock`, `stg`, `prod`).

---

## 1. Problem Statement & Motivation

During mobile application development and evaluator code review, relying exclusively on live external APIs introduces severe operational bottlenecks:

1. **GitHub API Rate Limiting (403 Forbidden)**:
   - The unauthenticated GitHub REST API is strictly rate-limited to **60 requests per hour per IP address**.
   - With real-time search debouncing, multi-page scrolling, and rapid test iterations, a developer or reviewer can exhaust this quota within 5 to 10 minutes.
2. **Network Flakiness & Offline Environments**:
   - Commuting, flights, or slow conference Wi-Fi can completely block development if the app requires live backend connectivity.
3. **Backend Service Disruption**:
   - Temporary GitHub API degradation or downtime should never halt frontend UI/UX feature development.
4. **Friction-Free Code Review**:
   - Hiring managers and code evaluators must be able to clone, run, and review all application features instantly without needing personal access tokens or worrying about shared corporate IP rate limits.
5. **Separation of Concerns (Zero UI Pollution)**:
   - Exposing debug mock switches or mode toggles in production user-facing screens degrades UX and violates clean software engineering principles. Mock mode is properly handled through **build flavors** and **runner configurations**.

---

## 2. Architecture & Clean Dependency Inversion

Offline mock mode leverages the project's **Clean Architecture** and Hilt's compile-time **Dependency Inversion**:

```
[UI Feature Layer] (SearchScreen, DetailScreen, SettingsScreen)
         │
         ▼
[GitHubRepository Contract] (:shared - domain/repository/GitHubRepository.kt)
         ▲
         ├───► DefaultGitHubRepository (Production/Dev: connects to live GitHub REST API via Ktor)
         └───► MockGitHubRepository    (Offline Mock: in-memory dataset, search filtering, 300ms delay)
```

In `RepositoryModule.kt`, Hilt binds the appropriate repository implementation based on `BuildConfig.FLAVOR_MODE` with zero framework boilerplate and zero production UI changes:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDefaultGitHubRepository(
        apiService: GitHubApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DefaultGitHubRepository = DefaultGitHubRepository(apiService, ioDispatcher)

    @Provides
    @Singleton
    fun provideMockGitHubRepository(): MockGitHubRepository =
        MockGitHubRepository(simulatedDelayMs = 300)

    @Provides
    @Singleton
    fun provideGitHubRepository(
        defaultRepo: DefaultGitHubRepository,
        mockRepo: MockGitHubRepository
    ): GitHubRepository {
        return if (BuildConfig.FLAVOR_MODE == "mock") {
            mockRepo
        } else {
            defaultRepo
        }
    }
}
```

> 💡 **Full Multiplatform Support**: `MockGitHubRepository` resides in `:shared/commonMain`, making it natively accessible to both Android and iOS targets without duplicate mock implementations.

---

## 3. Product Flavors Matrix

The project defines dedicated product flavors in `app/build.gradle`:

| Flavor | Dimension | Application ID Suffix | Data Source | Target Use Case |
| :--- | :--- | :--- | :--- | :--- |
| **`dev`** | `environment` | `.dev` | Live GitHub REST API | Daily feature development with live API connectivity |
| **`mock`** | `environment` | `.mock` | In-Memory `MockGitHubRepository` | CI test automation, offline development, UI snapshot testing |
| **`stg`** | `environment` | `.stg` | Live GitHub REST API / Staging | Pre-release staging validation and QA testing |
| **`prod`** | `environment` | *(none)* | Live GitHub REST API | Production release builds |

---

## 4. Mock Dataset & Stress-Testing Capabilities

`MockGitHubRepository` provides rich, pre-configured test scenarios:

* **Layout Stress Testing**:
  * **Massive Metrics**: Includes repositories with 999,999+ stars, forks, and watchers to verify number formatting, layout bounds, and prevent metric card overflow.
  * **Extreme Text Length**: Includes long repository titles to verify text wrapping, truncation, and card stability.
  * **Null/Optional Fields**: Tests repositories with `language = null`, zero forks, or empty avatar URLs to guarantee null safety.
* **Interactive Query Filtering**:
  * Filters results dynamically based on search query keywords across repository name, owner name, and programming language.
* **Simulated Network Latency**:
  * Configurable delay (default: 300ms) executed via `Dispatchers.IO` background coroutines allows developers to inspect loading spinners, state transitions, and debounce behavior under realistic network conditions.

---

## 5. Offline Error Handling vs. Mock Mode (Crucial Distinction)

Evaluators assess two distinct aspects of offline engineering:

1. **Mock Mode (Developer Tooling & Testing)**:
   - Configured at build time via the `mock` flavor.
   - Bypasses GitHub rate limits for reliable, repeatable UI and automated testing.
2. **Offline Network Resilience (Production User Experience)**:
   - When running the live app (`dev` or `prod`) and network connectivity is lost (e.g., Airplane mode):
     - The app never crashes, hangs, or silently fails.
     - Catches network exceptions (`IOException`, socket timeout) cleanly.
     - Displays an informative error view explaining the connectivity failure.
     - Offers an interactive **"Retry"** button allowing the user to seamlessly recover once reconnected.

---

## 6. How to Run

### Option A: Command Line (CLI)

```bash
# 1. Assemble and install the Mock variant (Offline, zero rate limits)
./gradlew installMockDebug

# 2. Assemble and install the Dev variant (Live GitHub API)
./gradlew installDevDebug

# 3. Run unit tests for specific flavors
./gradlew testMockDebugUnitTest
./gradlew testDevDebugUnitTest

# 4. Assemble release APK
./gradlew assembleProdRelease
```

### Option B: Android Studio (1-Click Variant Switcher)

1. Open the **Build Variants** tool window (left sidebar or **View &rarr; Tool Windows &rarr; Build Variants**).
2. Under module `:app`, locate the `Active Build Variant` dropdown.
3. Select **`mockDebug`** for 100% offline mock development.
4. Select **`devDebug`** for live GitHub API testing.
5. Click **Run** (`Shift+F10`) or **Debug** (`Shift+F9`).

### Option C: iOS Native Client (`iosApp`)

The native iOS SwiftUI reference project in `iosApp/` connects to the live GitHub REST API by default. To run in offline mock mode:

1. Open `iosApp.xcodeproj` in Xcode.
2. Click the Scheme dropdown &rarr; **Edit Scheme...** (`Cmd+<`).
3. Under **Run** &rarr; **Arguments**:
   - Add **`-mock`** to **Arguments Passed On Launch**, OR
   - Under **Environment Variables**, add **`APP_FLAVOR = mock`**.
4. Run the app (`Cmd+R`). The console will output:
   ```text
   🚀 [SearchViewModel] Running in MOCK Mode (Offline)
   ```

---

## 7. Adding New Mock Scenarios

To add custom test scenarios (e.g., simulating specific HTTP error edge cases or custom repository shapes), open `shared/src/commonMain/kotlin/.../MockGitHubRepository.kt` and append new `RepositoryItem` instances to the `mockData` list.
