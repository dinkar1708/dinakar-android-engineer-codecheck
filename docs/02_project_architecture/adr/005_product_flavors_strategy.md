# ADR-005: Product Flavors & Multi-Environment Strategy

**Status:** ✅ Implemented
**Deciders:** Dinakar Prasad Maurya
**Technical Story:** Offline Mock Development, Environment Isolation, CI/CD Packaging
**Implementation Date:** 2026-09-09  

---

## Context

Mobile engineers frequently face external constraints during development:
1. **API Rate Limiting:** The unauthenticated GitHub Search API limits requests to 10 per minute per IP address. Developers and automated test suites quickly exhaust this quota, causing HTTP 403 errors and blocking UI/feature work.
2. **Environment Contamination:** Installing test or development builds on physical test devices overwrites existing production builds if application IDs collide.
3. **Flaky UI Tests:** Tests that rely on live network responses are inherently fragile and nondeterministic.

---

## Decision

We introduce a formal multi-flavor strategy governed by an `environment` dimension across both Android and iOS:

### 1. Android Flavor Matrix (`app/build.gradle.kts`)
- **`dev`**:
  - `applicationIdSuffix = ".dev"` (`jp.co.yumemi.android.codecheck.dev`)
  - `versionNameSuffix = "-dev"`
  - `buildConfigField("String", "FLAVOR_MODE", "\"dev\"")`
  - Targets live GitHub REST API with debug logging enabled.
- **`mock`**:
  - `applicationIdSuffix = ".mock"` (`jp.co.yumemi.android.codecheck.mock`)
  - `versionNameSuffix = "-mock"`
  - `buildConfigField("String", "FLAVOR_MODE", "\"mock\"")`
  - Injects `MockGitHubRepository` via Hilt with simulated 300ms network latency and deterministic realistic datasets. 100% offline.
- **`stg`**:
  - `applicationIdSuffix = ".stg"` (`jp.co.yumemi.android.codecheck.stg`)
  - `versionNameSuffix = "-stg"`
  - `buildConfigField("String", "FLAVOR_MODE", "\"stg\"")`
  - Targets live GitHub REST API with staging/QA configuration for pre-release validation.
- **`prod`**:
  - Base application ID: `jp.co.yumemi.android.codecheck`
  - `buildConfigField("String", "FLAVOR_MODE", "\"prod\"")`
  - Production-ready live GitHub API with release signing and ProGuard/R8 optimizations.

### 2. iOS Xcode Scheme Equivalents
We mirror this environment separation in `iosApp.xcodeproj/xcshareddata/xcschemes/`:
- **`iosApp-Dev`**: Debug build with `APP_FLAVOR = dev`
- **`iosApp-Mock`**: Debug build with `APP_FLAVOR = mock` (launches `MockGitHubRepository`)
- **`iosApp-Stg`**: Staging build with `APP_FLAVOR = stg`
- **`iosApp-Prod`**: Release build with `APP_FLAVOR = prod`

### 3. Alignment with Git Branching Strategy
The 4 flavors map directly to the promotion pipeline defined in [Contributing Guidelines](../../CONTRIBUTING.md#branching-strategy-multi-environment-branching-model):
- **`dev` branch / Topic branches:** Targets `devDebug` (live development) and `mockDebug` (offline testing & CI).
- **`stg` branch:** Targets `stgRelease` (QA validation & staging telemetry).
- **`main` branch:** Targets `prodRelease` (production release packaging & distribution).

---

## Dependency Injection Wiring (`RepositoryModule.kt`)

```kotlin
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
```

---

## Consequences

### Positive ✅
- **Zero API Quota Blockers:** Developers and automated UI test suites can work 100% offline without hitting GitHub API limits.
- **Side-by-Side Device Installation:** Developers and QA testers can install `dev`, `mock`, `stg`, and `prod` APKs on the same device simultaneously without uninstalling.
- **Clean Architecture:** No UI code contains mock switches or debug toggles. Dependency injection swizzles implementations at build time.

### Negative ⚠️
- **Combinatorial Build Variants:** With 4 flavors (`dev`, `mock`, `stg`, `prod`) and 2 build types (`Debug`, `Release`), there are 8 build variants. CI pipelines explicitly build designated targets (`assembleDevDebug` for pull requests, `assembleStgRelease` for staging, `assembleProdRelease` for main).

---

## Alternatives Considered

1. **In-App Secret Debug Menu / Toggle Switch:**
   - *Rejected:* Increases APK size, risks exposing debug controls in production releases, and pollutes production UI code with conditional logic.
2. **MockWebServer Interceptor:**
   - *Rejected:* Requires maintaining duplicate HTTP response fixtures and configuring local loopback ports; less lightweight and clean than DI-injected mock repository classes.

---

## References
- [Android Developers: Configure Product Flavors](https://developer.android.com/build/build-variants#product-flavors)
- [Docs: Mock Development & Offline Mode Guide](../setup/03_mock_development_and_offline_mode.md)
- [Docs: Getting Started Guide](../../GETTING_STARTED.md)
