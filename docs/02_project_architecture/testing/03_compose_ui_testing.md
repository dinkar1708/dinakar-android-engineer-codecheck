# Testing Specification: Compose UI & Instrumented Testing

**Scope:** Phase 3 (Iteration & Construction)  
**Target:** Jetpack Compose UI Components & End-to-End Instrumented Flows  

---

## 1. Overview

Jetpack Compose UI tests verify declarative UI components without requiring full application launch overhead. Tests leverage AndroidX Compose Testing rules (`createComposeRule` or `createAndroidComposeRule`), querying the Compose Semantics Tree to assert visual rendering, accessibility properties, and user interaction callbacks.

Complementing component UI tests, instrumented end-to-end (E2E) tests validate complete navigation journeys using `CustomTestRunner` and Hilt dependency injection.

---

## 2. Testing the 5 Visual States of `SearchScreen`

Using `ComposeContentTestRule`, each visual state of `SearchContent` is verified in isolation using stateless composable injection:

```kotlin
@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun idleState_displays_welcomeMessage_and_searchBar() {
        composeTestRule.setContent {
            AppTheme {
                SearchContent(
                    uiState = SearchUiState.Idle,
                    searchInput = "",
                    onSearchInputChanged = {},
                    onSearchSubmit = {},
                    onRepositoryClick = {},
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Enter a search term to find GitHub repositories")
            .assertIsDisplayed()
    }

    @Test
    fun loadingState_displays_progressIndicator() {
        composeTestRule.setContent {
            AppTheme {
                SearchContent(
                    uiState = SearchUiState.Loading,
                    searchInput = "android",
                    onSearchInputChanged = {},
                    onSearchSubmit = {},
                    onRepositoryClick = {},
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("LoadingIndicator")
            .assertIsDisplayed()
    }

    @Test
    fun successState_displays_repositoryCards() {
        val repos = listOf(
            RepositoryItem(id = 1, name = "android", fullName = "google/android", language = "Kotlin")
        )

        composeTestRule.setContent {
            AppTheme {
                SearchContent(
                    uiState = SearchUiState.Success(repos),
                    searchInput = "android",
                    onSearchInputChanged = {},
                    onSearchSubmit = {},
                    onRepositoryClick = {},
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("google/android")
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_displays_noResultsMessage() {
        composeTestRule.setContent {
            AppTheme {
                SearchContent(
                    uiState = SearchUiState.Empty,
                    searchInput = "nonexistent_term",
                    onSearchInputChanged = {},
                    onSearchSubmit = {},
                    onRepositoryClick = {},
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("No repositories found")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displays_message_and_retryButton() {
        composeTestRule.setContent {
            AppTheme {
                SearchContent(
                    uiState = SearchUiState.Error("API rate limit exceeded"),
                    searchInput = "android",
                    onSearchInputChanged = {},
                    onSearchSubmit = {},
                    onRepositoryClick = {},
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("API rate limit exceeded")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Retry")
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}
```

---

## 3. End-to-End Instrumented Testing (`SearchE2ETest.kt`)

Complete user journeys are verified via instrumented tests in `app/src/androidTest/`:
- **Runner Configuration:** `CustomTestRunner` initializes `HiltTestApplication` for instrumented test dependency graphs.
- **Verification Flow:**
  1. App launches into `SearchScreen`.
  2. Test inputs `"kotlin"` into the search input.
  3. Search results render asynchronously.
  4. First item is clicked, navigating to `DetailScreen`.
  5. Detail screen verifies repository attributes and Custom Tabs web preview button.

---

## 4. Best Practices for Compose & UI Testing

1. **Test Stateless Composables:** Pass primitive states and callback lambdas into `SearchContent` rather than instantiating ViewModels in UI tests.
2. **Semantics Matching First:** Prefer matching by text or content description (`onNodeWithText`, `hasContentDescription`); reserve `Modifier.testTag()` for unlabelled loading indicators.
3. **Accessibility Assertions:** Verify touch targets meet 48x48dp and content descriptions exist for TalkBack screen readers.

---

## 5. Execution Commands

```bash
# Run Compose UI and instrumented tests on connected device/emulator
./gradlew connectedDevDebugAndroidTest
```

---

## 6. Cross-References

- [Master Testing Overview](./readme.md)
- [Unit Testing Specification](./01_unit_testing.md)
- [Integration Testing Specification](./02_integration_testing.md)
- [Test Cases Traceability Matrix](./04_test_cases_matrix.md)
