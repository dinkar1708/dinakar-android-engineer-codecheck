# ADR-002: Gradle Convention Plugins

**Status:** 📋 Proposed  
**Deciders:** Dinakar Prasad Maurya  
**Technical Story:** [Execution Roadmap Sprint 1](../../03_sprint_execution/01_execution_roadmap.md)  
**Depends On:** ADR-001 (Multi-Module Architecture)  

---

## 1. Context & Problem Statement

Following the decomposition of the monolithic app into a multi-module architecture (ADR-001), each subproject requires its own `build.gradle.kts` configuration. 

In a naive multi-module setup, common build logic (Android SDK targets, Java 17 toolchains, Kotlin compiler options, Compose compiler flags, and test dependencies) is repeatedly copy-pasted across every module (`feature:search`, `feature:detail`, `feature:settings`, `core:data`, `core:network`, etc.).

### Systemic Risks of Inlined Build Scripts:
1. **Redundancy & Drift:** Updating Compose, Kotlin, or target SDK versions requires manual edits across 8+ distinct files, risking version divergence and subtle build anomalies.
2. **Maintenance Overhead:** New developers are burdened with 45+ lines of imperative Gradle DSL per module, obscuring actual module dependencies.
3. **Weak Governance:** Enforcing standardized linting, compiler warnings, or JVM targets across all modules requires manual review instead of automated platform enforcement.

---

## 2. Decision

We establish an enterprise **composite build** under `build-logic/` using Gradle's `includeBuild` mechanism to publish reusable **Convention Plugins**.

### Architecture & Plugin Taxonomy

Each plugin encapsulates a cohesive set of build conventions, composing base plugins to establish unified standards:

| Plugin ID | Implementation Class | Responsibilities | Target Modules |
|:---|:---|:---|:---|
| `codecheck.android.application` | `AndroidApplicationConventionPlugin` | Application SDKs, default configs, packaging rules | `:app` |
| `codecheck.android.library` | `AndroidLibraryConventionPlugin` | Common library compileSdk (34), minSdk (24), Java 17, and test runners | All `:core:*` libraries |
| `codecheck.android.compose` | `AndroidComposeConventionPlugin` | Jetpack Compose compiler extension, Compose BOM, and UI dependencies | UI modules |
| `codecheck.android.feature` | `AndroidFeatureConventionPlugin` | Composes Library + Compose + Hilt, adding standard lifecycle & test dependencies | All `:feature:*` modules |
| `codecheck.android.hilt` | `HiltConventionPlugin` | Applies Hilt plugin, KSP annotation processor, and DI dependencies | DI consumer modules |
| `codecheck.jvm.library` | `JvmLibraryConventionPlugin` | Pure Kotlin/JVM compilation without Android SDK overhead | Pure domain modules |
| `codecheck.detekt` | `DetektConventionPlugin` | Configures static analysis quality gates and baseline rules | All modules |

---

## 3. Impact Comparison: Before vs. After

### Before: Imperative Boilerplate (~45 lines per module)
Prior to convention plugins, every module duplicated plugins, compile options, Kotlin options, Compose settings, and common dependencies.

### After: Declarative Intent (~8 lines per module)
With convention plugins, subprojects declare *what* they are rather than *how* to configure the build:

```kotlin
// feature/search/build.gradle.kts
plugins {
    id("codecheck.android.feature")
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
}
```

*Result:* **Over 70% reduction in build script lines** with 100% centralized governance.

---

## 4. Consequences & Trade-offs

### Positive ✅
- **Single Source of Truth (SSOT):** Target SDK, Java toolchain, and compiler arguments are maintained once in `build-logic`.
- **Zero Configuration Drift:** Impossible for submodules to accidentally run mismatched compile SDK or Kotlin compiler versions.
- **Fast Module Scaffolding:** Adding a new feature module takes under 2 minutes—apply `codecheck.android.feature` and write code immediately.
- **Enterprise Build Caching:** Composite builds isolate plugin compilation into independent Gradle cache boundaries.

### Negative ❌
- **Initial Setup Cost:** Requires dedicated setup of `build-logic/settings.gradle.kts` and Version Catalog integration.
- **Gradle API Knowledge:** Maintaining plugins requires familiarity with Gradle's Kotlin DSL and plugin APIs.

### Neutral ⚖️
- **Configuration Time:** Composite builds add ~1-2 seconds to the initial configuration phase; however, this is offset by improved incremental build caching.

---

## 5. Alternatives Considered

| Alternative | Evaluation | Rationale for Rejection |
|:---|:---|:---|
| **`buildSrc` Directory** | Legacy Gradle approach | In modern Gradle, any change within `buildSrc` invalidates the entire project's build cache. Composite builds (`build-logic`) are recommended by Gradle and Google. |
| **Root Script `subprojects {}` Block** | Imperative centralized script | Strongly discouraged by Gradle because it violates project isolation, couples all subprojects, and inhibits Gradle Configuration Cache. |
| **Manual Duplication (Status Quo)** | Copy-paste across modules | Unacceptable in enterprise software; leads to version skew and high maintenance friction. |

---

## 6. Success Metrics

- **Boilerplate Reduction:** From ~45 lines to ~8 lines per module (82% reduction).
- **Maintenance Points:** Target SDK and Kotlin options updated in 1 central plugin rather than 8+ modules.
- **Onboarding Speed:** New engineers focus strictly on feature code and domain models without wrestling with build configuration.

---

## 7. Authoritative References

- [Gradle User Guide: Sharing Build Logic between Subprojects](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html)
- [Android Developers: Guide to App Modularization — Convention Plugins](https://developer.android.com/topic/modularization/patterns#convention-plugins)
- [Google "Now in Android" Architecture Repository](https://github.com/android/nowinandroid/tree/main/build-logic)
