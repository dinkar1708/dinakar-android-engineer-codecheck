# Testing Guide 3: Integration & End-to-End (E2E) Testing

**Scope:** Level 3 & Level 4 — Data/Network Integration & On-Device E2E Validation  
**Target:** Ktor `MockEngine`, Repository Contracts, and Full On-Device User Journeys  

---

## 1. Overview & Architectural Roles

This guide covers two essential integration layers:
1. **Data & Network Integration Tests (JVM):** Offline HTTP engine testing with Ktor `MockEngine` in `:core:network` and `:core:data`.
2. **End-to-End (E2E) Instrumented Tests (Device/Emulator):** Live user journey tests in `:app/src/androidTest/`.

---

## 2. Directory & Folder Structure

```text
📁 Repository Root
│
├── 📁 app/                                                 ⭐ [ONLY MODULE WITH ANDROIDTEST]
│    └── 📁 src/androidTest/kotlin/jp/co/yumemi/android/codecheck/
│         └── 📄 SearchE2ETest.kt                           <-- Live Emulator E2E User Journey Test
│
├── 📁 core/network/
│    └── 📁 src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/network/api/
│         └── 📄 GitHubApiServiceTest.kt                    <-- Ktor MockEngine Network Integration Tests
│
└── 📁 core/data/
     └── 📁 src/commonTest/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/
          └── 📄 GitHubRepositoryImplTest.kt                <-- Repository & In-Memory Cache Tests
```

> [!IMPORTANT]
> ### 🚨 Why is `src/androidTest/` ONLY in `:app`?
> - **Single Source of Truth for APK Assembly:** The `:app` module is the single integration root that combines `:feature:search`, `:feature:detail`, Jetpack Compose Navigation (`AppNavHost`), and Hilt Dependency Injection into a runnable application.
> - **End-to-End Journey Verification:** An E2E test verifies real user actions moving **across multiple feature boundaries** (from Search &rarr; to Detail &rarr; back to Search). Placing this in `:app/src/androidTest/` tests the real application as assembled for production.
> - **Feature Modules Don't Need `androidTest/`:** Feature screens are already 100% verified using fast Robolectric Compose tests in `feature/*/src/test/` (see [Guide 2](./02_compose_ui_testing.md)).

---

## 3. End-to-End (E2E) Live User Journey (`SearchE2ETest.kt`)

The E2E test runs directly on an Android device or emulator (e.g. `emulator-5554`), exercising the complete user journey:

### E2E Step-by-Step Execution:
1. **Initial Screen Verification:** Launches `MainActivity` via `createAndroidComposeRule<MainActivity>()`. Verifies top bar title (`"GitHub Repository Search"`) and empty search guidance.
2. **Query Input:** Locates text field using semantic matcher `hasSetTextAction()` and enters query `"kotlin"`.
3. **Submit Search:** Clicks the search icon button (`onNodeWithContentDescription("Submit search")`).
4. **Wait for Network Results:** Uses `composeTestRule.waitUntil(timeoutMillis = 15000)` to wait for GitHub API responses to arrive and render repository cards.
5. **Click Repository Card:** Locates the first repository card via its avatar and triggers `performClick()`.
6. **Detail Screen Verification:** Confirms transition to the detail screen and verifies `"Repository Details"` top bar and repository statistics (stars, watchers, forks, open issues).
7. **Navigate Back:** Clicks the back navigation icon button (`onNodeWithContentDescription("Navigate back")`).
8. **Return Verification:** Confirms the app smoothly navigates back to the search results screen.

```kotlin
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class SearchE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullUserJourney_search_clickItem_openDetails_andNavigateBack() {
        // 1. Initial screen
        composeTestRule.onNodeWithText("GitHub Repository Search").assertIsDisplayed()

        // 2. Type search query
        composeTestRule.onNode(hasSetTextAction()).performTextInput("kotlin")

        // 3. Submit search
        composeTestRule.onNodeWithContentDescription("Submit search").performClick()

        // 4. Wait for API results
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule.onAllNodes(hasContentDescription("avatar", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }

        // 5. Click first repository card
        composeTestRule.onAllNodes(hasContentDescription("avatar", substring = true))
            .onFirst()
            .performClick()

        // 6. Verify details screen
        composeTestRule.onNodeWithText("Repository Details").assertIsDisplayed()

        // 7. Click back button
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()

        // 8. Verify return to search results
        composeTestRule.onNodeWithText("GitHub Repository Search").assertIsDisplayed()
    }
}
```

---

## 4. Data & Network Integration Testing with Ktor `MockEngine`

All HTTP contracts and repository caching logic are tested offline using Ktor's native `MockEngine` without third-party mocking libraries:

### Key Integration Scenarios:
1. **HTTP 200 Deserialization:** Verifies valid JSON responses correctly deserialize into `RepositoryItem` entities.
2. **HTTP 403 Rate Limit Handling:** Verifies GitHub API rate limit responses are translated into explicit `NetworkException.RateLimitExceeded`.
3. **Blank Query Short-Circuit:** Verifies whitespace queries return an empty list immediately without emitting an HTTP request.
4. **In-Memory Cache:** Verifies identical consecutive queries return cached results immediately, conserving bandwidth.

---

## 5. Execution Commands

```bash
# Run End-to-End (E2E) instrumented test on connected emulator or device
./gradlew :app:connectedAndroidTest

# Run Ktor MockEngine network integration tests (JVM)
./gradlew :core:network:testDebugUnitTest

# Run repository & cache integration tests (JVM)
./gradlew :core:data:testDebugUnitTest
```

---

## 6. Testing Documentation Index

- **[Master Testing Overview](./readme.md)**: Architecture, test pyramid, and directory layout.
- **[Guide 1: Unit & ViewModel Testing](./01_unit_testing.md)**: StateFlow UDF & Turbine testing in `src/test/`.
- **[Guide 2: Compose View Testing](./02_compose_ui_testing.md)**: Declarative Compose UI tests with Robolectric in `src/test/`.
- **[Guide 3: Integration & E2E Testing](./03_integration_and_e2e_testing.md)**: *(Current document)*
- **[Traceability Matrix](./04_test_cases_matrix.md)**: Complete mapping from requirements to test classes.
