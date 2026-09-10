# Testing Specification: Compose UI & Instrumented Testing

**Scope:** Phase 3 (Iteration & Construction)
**Target:** Jetpack Compose UI Components & End-to-End Instrumented Flows

---

## 1. Overview

Jetpack Compose UI tests verify declarative UI components using AndroidX Compose Testing rules (`createComposeRule`), querying the Compose Semantics Tree for visual rendering, accessibility, and user interactions.

Instrumented E2E tests validate complete navigation journeys using `CustomTestRunner` and Hilt dependency injection.

---

## 2. Testing the 5 Visual States of `SearchScreen`

Each state is tested in isolation by passing different `SearchUiState` values to the stateless `SearchContent` composable:

**States to verify:**
- **Idle:** Welcome message and empty search bar
- **Loading:** Progress indicator with test tag
- **Success:** Repository cards with data
- **Empty:** "No results" message
- **Error:** Error message with retry button

**Example test pattern:**

```kotlin
@Test
fun idleState_displays_welcomeMessage() {
    composeTestRule.setContent {
        SearchContent(uiState = SearchUiState.Idle, ...)
    }
    composeTestRule.onNodeWithText("Enter a search term").assertIsDisplayed()
}
```

---

## 3. End-to-End Instrumented Testing

Complete user journeys verified in `app/src/androidTest/SearchE2ETest.kt`:

**Flow:**
1. Launch app → `SearchScreen`
2. Input search term → verify results render
3. Click repository item → navigate to `DetailScreen`
4. Verify detail attributes and web preview button

**Setup:** `CustomTestRunner` with `HiltTestApplication` for DI

---

## 4. Best Practices

- **Test Stateless Composables:** Pass states/callbacks, not ViewModels
- **Semantics First:** Use `onNodeWithText()`, `hasContentDescription()`; `testTag()` only for non-labeled elements
- **Accessibility:** Verify 48x48dp touch targets and TalkBack content descriptions

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
