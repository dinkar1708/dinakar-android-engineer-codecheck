# ADR-001: Multi-Module Architecture

**Status:** 📋 Proposed
**Deciders:** Dinakar Prasad Maurya
**Technical Story:** [Execution Roadmap Sprint 2](../../03_sprint_execution/01_execution_roadmap.md)

---

## Context

The current project has a single-module structure (`app/`) with all code (UI, business logic, data layer) in one module. As the codebase grows, this presents several challenges:

### Current Pain Points:
1. **Build Performance:** Full rebuild takes ~3 minutes; any change requires rebuilding entire app
2. **Tight Coupling:** UI code, business logic, and data layer are intertwined
3. **Testing Difficulty:** Hard to test domain logic in isolation (requires Android framework)
4. **Team Scalability:** Multiple developers frequently conflict on same files
5. **Code Organization:** Unclear boundaries between layers

### Industry Best Practice Findings:
- Google's official "Now in Android" architecture guidelines strongly recommend feature and core modularization
- **Gradle Enterprise** reports 60-70% build time savings with modularization and parallel compilation

---

## Decision

We will restructure the project into a **multi-module architecture** with the following modules:

```
android-engineer-codecheck/
├── app/                              # Android app entry point (10% of code)
├── core/
│   ├── data/                         # Repository implementations
│   ├── domain/                       # Business models & interfaces (pure Kotlin)
│   ├── network/                      # Ktor client, API services
│   ├── designsystem/                 # Material 3 theme system
│   └── ui/                           # Shared Composables
├── feature/
│   ├── search/                       # Search feature (ViewModel + Screen + Tests)
│   └── detail/                       # Detail feature (ViewModel + Screen + Tests)
└── shared/                           # Existing KMP module (unchanged)
```

### Module Dependency Rules:
1. **app/** depends on all `feature/*` modules
2. **feature/*** modules depend on `core/*` modules
3. **core/*** modules **cannot** depend on `feature/*` or `app/`
4. `core/domain` has **no Android dependencies** (pure Kotlin)
5. Circular dependencies are **forbidden** (enforced by Gradle)

---

## Consequences

### Positive ✅

1. **Build Performance**
   - Incremental builds: 3 min → ~30 sec (90% reduction)
   - Only changed modules rebuild
   - Gradle caching more effective

2. **Parallel Development**
   - 3 developers can work on search, detail, data simultaneously
   - Feature modules are isolated (no merge conflicts)

3. **Clear Separation of Concerns**
   - Domain logic is pure Kotlin (testable without Android framework)
   - UI and business logic clearly separated
   - Enforced by Gradle dependencies

4. **Improved Testability**
   - Test `core/domain` with pure Kotlin unit tests (fast)
   - Test `feature/*` modules in isolation with fake repositories
   - Easier to mock dependencies

5. **Code Ownership**
   - Each module can have a dedicated owner/team
   - Clear boundaries reduce cognitive load

6. **Preparation for KMP**
   - `core/domain` and `core/network` can be migrated to KMP later
   - Clean separation makes KMP migration easier

### Negative ❌

1. **Initial Migration Effort**
   - 3-5 days to restructure existing code
   - Risk of breaking changes during migration

2. **Learning Curve**
   - Team needs to understand module boundaries
   - New developers need onboarding on module structure

3. **Gradle Complexity**
   - More `build.gradle.kts` files to maintain
   - Module dependency graph must be managed
   - (Mitigated by convention plugins in ADR-002)

4. **Over-Engineering Risk**
   - For small projects, modules may be overkill
   - Requires discipline to maintain boundaries

### Neutral ⚖️

1. **IDE Navigation**
   - More folders to navigate
   - But clearer structure helps discoverability

2. **CI/CD Changes**
   - Need to update GitHub Actions to test all modules
   - But enables parallel CI jobs (faster overall)

---

## Alternatives Considered

### Alternative 1: Keep Single Module
**Why Rejected:**
- Doesn't scale for team growth
- Build times continue to degrade
- Fails to meet modern industry standards for scalable Android architecture
- Fails to demonstrate architectural thinking for Lead/EM role

### Alternative 2: Package-by-Feature (Without Modules)
**Example:**
```
app/src/main/kotlin/
  ├── feature/search/
  ├── feature/detail/
  └── core/
```

**Why Rejected:**
- No build performance benefits (still single module)
- Gradle can't enforce dependency rules (developers can violate boundaries)
- All code still recompiles on any change
- Doesn't demonstrate Gradle expertise

### Alternative 3: Extreme Modularization (20+ Modules)
**Example:** Separate modules for `core-utils`, `core-extensions`, `core-testing`, etc.

**Why Rejected:**
- Over-engineering for project size
- Maintenance burden too high
- Diminishing returns on build performance
- Harder to navigate
- Can evolve to this later if needed

---

## Implementation Strategy

### Phase 1: Core Layer (Day 1-2)
1. Create `core/domain` module
   - Move data classes (`Repository`, `Owner`, etc.)
   - Define repository interfaces
   - Pure Kotlin (no Android dependencies)

2. Create `core/network` module
   - Move Ktor client setup
   - Move `GitHubApiService`
   - Move DTOs and JSON serialization

3. Create `core/data` module
   - Move repository implementations
   - Depend on `core/domain` and `core/network`

### Phase 2: UI/Design Layer (Day 3)
4. Create `core/designsystem` module
   - Move `theme/` package
   - `Color.kt`, `Type.kt`, `Theme.kt`, `Spacing.kt`

5. Create `core/ui` module
   - Move shared Composables
   - `ErrorMessage`, `LoadingIndicator`, etc.

### Phase 3: Feature Modules (Day 4-5)
6. Create `feature/search` module
   - `SearchScreen`, `SearchViewModel`
   - Tests: `SearchViewModelTest`

7. Create `feature/detail` module
   - `DetailScreen`, `DetailViewModel`
   - Tests: `DetailViewModelTest`

8. Slim down `app/` module
   - Keep only `MainActivity`, `NavHost`, DI setup
   - Depend on all feature modules

### Phase 4: Validation (Day 5)
9. Verify all modules build independently
10. Run all tests
11. Measure build time improvement
12. Update documentation

---

## Success Metrics

| Metric | Before (Single Module) | Target (Multi-Module) |
|:-------|:----------------------:|:---------------------:|
| **Clean Build** | ~3 min | ~3 min (no change) |
| **Incremental Build** | ~3 min | ~30 sec (90% faster) |
| **Module Count** | 2 | 8+ |
| **Lines per Module** | 2000+ | <500 |
| **Dependency Depth** | N/A | Max 3 levels |
| **Circular Dependencies** | Unknown | 0 (enforced) |

---

## References

- [Guide to Android app modularization](https://developer.android.com/topic/modularization) - Google
- [Now in Android Architecture](https://github.com/android/nowinandroid/blob/main/docs/ArchitectureLearningJourney.md) - Google
- [Gradle Build Tool: Structuring Large Projects](https://docs.gradle.org/current/userguide/structuring_software_products.html)

