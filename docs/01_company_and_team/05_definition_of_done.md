# Definition of Done (DoD)

**Scope:** Pull Request & Feature Completion Quality Gate  
**Target:** Mobile Engineering Team & Platform Delivery  

---

## 1. Purpose & Governance

The Definition of Done (DoD) establishes the non-negotiable quality criteria that every Pull Request must fulfill before being merged into the primary branch (`main` / `develop`). It ensures platform stability, architectural consistency, test reproducibility, and seamless production deployability.

---

## 2. Quality Gate Checklist

### A. Code Quality & Defensive Architecture
- [ ] **Kotlin Standards:** Strict adherence to official Kotlin conventions; no force unwraps (`!!`) or unchecked casts.
- [ ] **Architectural Boundary:** Follows MVVM + Repository pattern with Unidirectional Data Flow (UDF). Business logic resides strictly in ViewModels and Domain layers; UI components remain passive.
- [ ] **State Management:** UI state exposed exclusively via immutable `StateFlow` / `SharedFlow` with lifecycle-aware collection.
- [ ] **Dependency Injection:** All dependencies injected via Hilt; no manual singleton instantiation or service locators.
- [ ] **Lifecycle & Resource Discipline:** Coroutines scoped to `viewModelScope`; ViewBinding references cleared in `onDestroyView()` to prevent memory leaks.

### B. Automated Testing & Verification
- [ ] **Coverage Threshold:** Minimum 80% line/branch coverage across newly added or modified business logic and ViewModels.
- [ ] **Deterministic Suites:** Unit tests pass consistently (`./gradlew testDevDebugUnitTest`) with zero flaky tests.
- [ ] **Scenario Coverage:** Suites validate happy path, negative path, API network exceptions (4xx, 5xx, rate limits), empty states, and boundary conditions.
- [ ] **State Restoration:** Stateful UI logic verified against configuration changes (rotation) and process recreation.

### C. Static Analysis & Continuous Integration
- [ ] **Static Analysis (Detekt):** `./gradlew detekt` passes with 0 rule violations.
- [ ] **Android Lint:** `./gradlew lintDebug` passes with 0 errors and zero unaddressed warnings.
- [ ] **Code Formatting:** Code is formatted via KtLint / Spotless standards before review.
- [ ] **CI Pipeline:** All automated GitHub Actions checks pass with green status.

### D. Accessibility & UI/UX Standards
- [ ] **WCAG 2.1 AA Compliance:** Minimum contrast ratio of 4.5:1 for standard text and 3:1 for large text and interactive components.
- [ ] **Touch Targets:** All clickable/interactive elements meet the minimum 48x48dp touch target threshold.
- [ ] **Screen Reader Semantics:** Semantic `contentDescription` attributes supplied for all non-decorative visual elements.
- [ ] **Dynamic Scaling:** UI layouts accommodate text scaling up to 200% without clipping or content overlap.
- [ ] **Theme & Configuration:** Layouts verified across Light and Dark themes, and both Portrait and Landscape orientations.

### E. Localization & Documentation
- [ ] **String Externalization:** Zero hardcoded strings in layouts or Kotlin files; all strings externalized in `res/values/strings.xml`.
- [ ] **Bilingual Support:** Japanese translations supplied in `res/values-ja/strings.xml` for all new UI strings.
- [ ] **API Documentation:** Public APIs, interfaces, and complex algorithms documented with clear KDoc comments explaining *why*, not just *what*.
- [ ] **Architectural Decisions:** Any major architectural or dependency modifications are accompanied by an Architecture Decision Record (ADR) in the project's architecture documentation.

### F. Pull Request & Review Hygiene
- [ ] **Atomic Scope:** PR is scoped to a single logical change (recommended < 300 LOC) to facilitate rigorous peer review.
- [ ] **PR Context:** Description details Problem Context, Architectural Approach, and Verification Steps.
- [ ] **Visual Evidence:** Before/after screenshots or screen recordings attached for all UI/UX modifications.
- [ ] **Peer Sign-off:** Minimum of 1 approving review from a team peer; all reviewer comments resolved.

---

## 3. Enforcement & Escalation

- **Merge Blocking:** GitHub branch protection rules strictly block merging if CI quality gates or peer approval requirements are unmet.
- **Waivers:** Any emergency bypass or temporary rule waiver requires explicit sign-off from the Mobile Platform Lead and a tracked follow-up remediation ticket.
