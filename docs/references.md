# Architecture, Platform & Technical References

## 1. Overview & Architectural Philosophy
This document compiles the catalog of authoritative technical documentation, official platform standards, and enterprise engineering publications followed throughout the architecture, design, and implementation of this project.

Every reference included below has been verified for availability (HTTP 200 OK) and is accompanied by the specific architectural rationale explaining how and why it is applied in this codebase.

---

## 2. Google Android Official Guidelines & Jetpack

### 2.1 Architecture & Layering
- **Guide to App Architecture**: [https://developer.android.com/topic/architecture](https://developer.android.com/topic/architecture)  
  *Application in Project:* Defines the strict three-tier separation between the UI Layer, Domain Layer, and Data Layer, following the Dependency Inversion Principle.
- **UI Layer & Unidirectional Data Flow (UDF)**: [https://developer.android.com/topic/architecture/ui-layer](https://developer.android.com/topic/architecture/ui-layer)  
  *Application in Project:* UI state flows down as immutable sealed states (`Loading`, `Success`, `Error`, `Empty`), and user events flow up to the ViewModel.
- **Data Layer & Repository Pattern**: [https://developer.android.com/topic/architecture/data-layer](https://developer.android.com/topic/architecture/data-layer)  
  *Application in Project:* The repository coordinates data sources (remote GitHub API and in-memory cache) acting as the Single Source of Truth (SSOT).  
  *Scoping Rationale:* In-memory caching is implemented for fast session navigation. Persistent offline database synchronization (e.g., Room) was intentionally scoped out because search queries require live API freshness and caching arbitrary search indices locally leads to stale results.
- **Preferences DataStore**: [https://developer.android.com/topic/libraries/architecture/datastore](https://developer.android.com/topic/libraries/architecture/datastore)  
  *Application in Project:* Asynchronous, thread-safe persistence for user settings (app theme mode: Light/Dark/System, and language preference) using Kotlin Coroutines and Flow.
- **Android App Modularization**: [https://developer.android.com/topic/modularization](https://developer.android.com/topic/modularization)  
  *Application in Project:* Enforces high cohesion and low coupling across feature modules (`:feature:search`, `:feature:detail`, `:feature:settings`) and core infrastructure modules (`:core:network`, `:core:model`, `:core:designsystem`).
- **Common Modularization Patterns**: [https://developer.android.com/topic/modularization/patterns](https://developer.android.com/topic/modularization/patterns)  
  *Application in Project:* Guides public API exposure through interface modules, preventing cyclical dependencies and reducing incremental build times.
- **Now in Android Reference Architecture**: [https://github.com/android/nowinandroid](https://github.com/android/nowinandroid)  
  *Application in Project:* Official benchmark repository used to structure Gradle convention plugins, Compose theming tokens, and unit test patterns.

### 2.2 Jetpack Compose & Design System
- **Thinking in Compose**: [https://developer.android.com/develop/ui/compose/mental-model](https://developer.android.com/develop/ui/compose/mental-model)  
  *Application in Project:* Replaces imperative XML layout inflation and `ViewBinding` with declarative composables and deterministic recomposition.
- **Material 3 Design Guidelines**: [https://m3.material.io/](https://m3.material.io/)  
  *Application in Project:* Adheres to Google's Material 3 tokenized color palette, dynamic theming, surface elevation, and typography scales.
- **Compose Design Systems & Theming**: [https://developer.android.com/jetpack/compose/designsystems/material3](https://developer.android.com/jetpack/compose/designsystems/material3)  
  *Application in Project:* Centralized theme wrapper (`AppTheme`) providing consistent colors, shapes, and typography across all feature screens.
- **Compose State Hoisting**: [https://developer.android.com/develop/ui/compose/state](https://developer.android.com/develop/ui/compose/state)  
  *Application in Project:* Screen content composables are stateless, accepting plain state data classes and lambda event handlers, making them easy to preview and UI test.
- **Compose Lifecycle & Flow Collection**: [https://developer.android.com/jetpack/compose/lifecycle](https://developer.android.com/jetpack/compose/lifecycle)  
  *Application in Project:* Employs `collectAsStateWithLifecycle()` to automatically suspend Flow collection when composables enter the background, conserving device battery and CPU resources.
- **Android Accessibility Guide**: [https://developer.android.com/guide/topics/ui/accessibility](https://developer.android.com/guide/topics/ui/accessibility)  
  *Application in Project:* Implements TalkBack content descriptions, minimum 48dp touch targets, and scalable Dynamic Type support.

### 2.3 Concurrency, State & Dependency Injection
- **Kotlin Coroutines on Android**: [https://developer.android.com/kotlin/coroutines](https://developer.android.com/kotlin/coroutines)  
  *Application in Project:* Structured concurrency using `viewModelScope`, ensuring background operations are tied to ViewModel lifecycles and automatically canceled upon teardown.
- **Kotlin Flow & Reactive Streams**: [https://developer.android.com/kotlin/flow](https://developer.android.com/kotlin/flow)  
  *Application in Project:* Reactive asynchronous data pipelines with operators (`map`, `debounce`, `catch`, `flowOn`) for search queries and network streaming.
- **StateFlow and SharedFlow**: [https://developer.android.com/kotlin/flow/stateflow-and-sharedflow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)  
  *Application in Project:* `StateFlow` for state retention across configuration changes; `SharedFlow` for one-shot UI navigation and snackbar events.
- **ViewModel Overview**: [https://developer.android.com/topic/libraries/architecture/viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel)  
  *Application in Project:* Decouples presentation logic from UI life-cycles, retaining state across orientation flips and fold/unfold events.
- **SavedStateHandle for Process Death Survival**: [https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)  
  *Application in Project:* Persists active search query and repository selection to restore UI state when the app is recreated after system process termination.
- **Hilt Dependency Injection**: [https://developer.android.com/training/dependency-injection/hilt-android](https://developer.android.com/training/dependency-injection/hilt-android)  
  *Application in Project:* Compile-time dependency injection providing `@Singleton` repository bindings and `@ViewModelScoped` dependencies.

### 2.4 Testing & Reliability
- **Testing Android Applications**: [https://developer.android.com/training/testing](https://developer.android.com/training/testing)  
  *Application in Project:* Guides test pyramid distribution: fast local JVM unit tests for business logic and instrumented tests for platform integrations.
- **Testing Coroutines with TestDispatcher**: [https://developer.android.com/kotlin/coroutines/test](https://developer.android.com/kotlin/coroutines/test)  
  *Application in Project:* Uses `runTest`, `StandardTestDispatcher`, and `advanceUntilIdle()` to test asynchronous code deterministically without real delays.
- **Turbine Flow Testing (Cash App)**: [https://github.com/cashapp/turbine](https://github.com/cashapp/turbine)  
  *Application in Project:* Step-by-step assertions on Flow emissions (`awaitItem()`, `awaitComplete()`) for ViewModel and Repository tests.
- **Testing Compose Layouts**: [https://developer.android.com/develop/ui/compose/testing](https://developer.android.com/develop/ui/compose/testing)  
  *Application in Project:* UI interaction assertions verifying node visibility, text rendering, and click actions via Compose testing semantics.
- **Detekt Static Code Analysis for Kotlin**: [https://detekt.dev/](https://detekt.dev/)  
  *Application in Project:* Automated static analysis verifying complexity metrics, naming conventions, formatting, and potential code smells in CI.
- **EditorConfig Specification**: [https://editorconfig.org/](https://editorconfig.org/)  
  *Application in Project:* Cross-IDE consistency for indentation, character encodings, and line endings.

---

## 3. Kotlin & Multiplatform (KMP)

- **Kotlin Multiplatform Official Documentation**: [https://kotlinlang.org/docs/multiplatform.html](https://kotlinlang.org/docs/multiplatform.html)  
  *Application in Project:* Foundational reference for the shared headless core module (`:shared`), compiling common business logic for multiple target platforms.
- **Get Started with Kotlin Multiplatform**: [https://kotlinlang.org/docs/multiplatform-get-started.html](https://kotlinlang.org/docs/multiplatform-get-started.html)  
  *Application in Project:* Project configuration guide for targeting Android JVM and iOS Native runtimes.
- **Ktor Client Multiplatform Engine**: [https://ktor.io/docs/client-engines.html](https://ktor.io/docs/client-engines.html)  
  *Application in Project:* Multiplatform networking abstraction utilizing `OkHttp` engine on Android and `Darwin` (`URLSession`) on iOS.
- **Kotlinx Serialization Guide**: [https://github.com/Kotlin/kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)  
  *Application in Project:* Reflectionless, compile-time JSON decoding for GitHub REST API response schemas across platforms.
- **Kotlinx Coroutines Multiplatform**: [https://github.com/Kotlin/kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)  
  *Application in Project:* Cross-platform asynchronous execution on JVM and Apple Native Darwin runtimes.

---

## 4. Apple iOS & Human Interface Guidelines

- **Apple Human Interface Guidelines (HIG)**: [https://developer.apple.com/design/human-interface-guidelines](https://developer.apple.com/design/human-interface-guidelines)  
  *Application in Project:* Ensures the iOS client feels 100% native with Apple platform styling, safe area compliance, and standard navigation behaviors.
- **SwiftUI Declarative Interface Framework**: [https://developer.apple.com/documentation/swiftui](https://developer.apple.com/documentation/swiftui)  
  *Application in Project:* Declarative UI presentation on iOS consuming models provided by the shared KMP core.
- **Swift Modern Concurrency (async/await, Actor)**: [https://docs.swift.org/swift-book/documentation/the-swift-programming-language/concurrency/](https://docs.swift.org/swift-book/documentation/the-swift-programming-language/concurrency/)  
  *Application in Project:* Asynchronous UI dispatch using `@MainActor` to guarantee thread safety when observing shared state.
- **Apple Accessibility Standards**: [https://developer.apple.com/accessibility/](https://developer.apple.com/accessibility/)  
  *Application in Project:* Native VoiceOver accessibility labels and Dynamic Type font scaling on iOS.

---

## 5. Software Architecture & Design Patterns

- **Robert C. Martin: The Clean Architecture**: [https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)  
  *Application in Project:* Enforces the Dependency Rule where inner domain logic remains completely agnostic of UI frameworks and external libraries.
- **Martin Fowler: Strangler Fig Application Pattern**: [https://martinfowler.com/bliki/StranglerFigApplication.html](https://martinfowler.com/bliki/StranglerFigApplication.html)  
  *Application in Project:* Applied during migration to safely replace legacy XML layouts and monolithic fragments with modern Compose screens incrementally without service interruption.
- **Michael Nygard: Architecture Decision Records (ADRs)**: [https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)  
  *Application in Project:* Adopted in `docs/02_inception/adr/` to systematically record architectural trade-offs, rationale, and consequences for each technical choice.
- **Google SRE Book: Service Level Objectives**: [https://sre.google/sre-book/service-level-objectives/](https://sre.google/sre-book/service-level-objectives/)  
  *Application in Project:* Informs mobile reliability metrics (crash-free user sessions target >= 99.9% and cold-start latency budgets).

---

## 6. Build Systems & Dependency Management

- **Gradle Build Tool: Structuring Large Projects**: [https://docs.gradle.org/current/userguide/structuring_software_products.html](https://docs.gradle.org/current/userguide/structuring_software_products.html)  
  *Application in Project:* Establishes modular build isolation, project graph boundaries, and parallel task execution.
- **Gradle: Sharing Build Logic with Convention Plugins**: [https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html)  
  *Application in Project:* Implemented in `build-logic` using Kotlin DSL plugins (`buildlogic.android.library`, `buildlogic.android.compose`) to eliminate duplicated Gradle scripts.
- **Gradle Version Catalogs**: [https://docs.gradle.org/current/userguide/platforms.html](https://docs.gradle.org/current/userguide/platforms.html)  
  *Application in Project:* Single source of truth for all dependencies and plugin versions located in `gradle/libs.versions.toml`.

---

## 7. GitHub API & Assessment Standards

- **GitHub REST API Documentation**: [https://docs.github.com/en/rest](https://docs.github.com/en/rest)  
  *Application in Project:* Base specification for REST endpoints, HTTP status handling, and payload models.
- **GitHub Search Repositories API Endpoint**: [https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-repositories](https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-repositories)  
  *Application in Project:* Detailed schema for query syntax, repository items, fork counts, star metrics, and pagination.
- **GitHub API Rate Limiting Specifications**: [https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api)  
  *Application in Project:* Rationale for handling HTTP 403 rate limits gracefully and notifying the user with friendly actionable UI states.
- **Yumemi Coding Test Evaluation Criteria (Qiita)**: [https://qiita.com/blendthink/items/aa70b8b3106fb4e3555f](https://qiita.com/blendthink/items/aa70b8b3106fb4e3555f)  
  *Application in Project:* Baseline evaluation standard created by Yumemi's lead reviewer detailing readability, null-safety, architecture, testing, and lifecycle hygiene.
- **Yumemi Code Check Base Repository**: [https://github.com/yumemi-inc/android-engineer-codecheck](https://github.com/yumemi-inc/android-engineer-codecheck)  
  *Application in Project:* The initial problem specification that was systematically refactored, modernized, and stabilized in this repository.

---

## 8. Continuous Integration, Delivery & Observability

- **Firebase App Distribution**: [https://firebase.google.com/docs/app-distribution](https://firebase.google.com/docs/app-distribution)  
  *Application in Project:* Automated delivery channel for QA and internal test builds directly from CI.
- **Firebase Crashlytics**: [https://firebase.google.com/docs/crashlytics](https://firebase.google.com/docs/crashlytics)  
  *Application in Project:* Real-time crash reporting and unhandled exception monitoring for production builds.
- **Fastlane Mobile Automation**: [https://fastlane.tools/](https://fastlane.tools/)  
  *Application in Project:* Automated lane execution for running tests, incrementing build versions, and signing artifacts.
- **Google Play Developer Publishing API**: [https://developers.google.com/android-publisher](https://developers.google.com/android-publisher)  
  *Application in Project:* Programmatic deployment of release bundles to Google Play Internal and Production tracks.
- **Apple App Store Connect API**: [https://developer.apple.com/app-store-connect/api/](https://developer.apple.com/app-store-connect/api/)  
  *Application in Project:* Automated provisioning, certificate management, and TestFlight deployment.
- **Apple TestFlight**: [https://developer.apple.com/testflight/](https://developer.apple.com/testflight/)  
  *Application in Project:* iOS beta testing platform for external stakeholders.
- **Square LeakCanary**: [https://square.github.io/leakcanary/](https://square.github.io/leakcanary/)  
  *Application in Project:* Automatic memory leak detection integrated into debug builds to ensure zero retained Activity/Fragment references.

---

## 9. Real-World Enterprise Engineering Case Studies

The architectural patterns implemented in this repository mirror published engineering architectures from leading mobile technology organizations:

### 9.1 Slack: Enterprise Mobile Modularization
- **Scaling Slack's Mobile Codebases (Overview)**: [https://slack.engineering/stabilize-modularize-modernize-scaling-slacks-mobile-codebases-2/](https://slack.engineering/stabilize-modularize-modernize-scaling-slacks-mobile-codebases-2/)  
  *Application in Project:* Demonstrates how multi-module separation and strict dependency boundaries enable parallel development, isolate build caching, and prevent monolithic merge conflicts.
- **Scaling Slack's Mobile Codebases: Modernization**: [https://slack.engineering/scaling-slacks-mobile-codebases-modernization/](https://slack.engineering/scaling-slacks-mobile-codebases-modernization/)  
  *Application in Project:* Real-world validation for incremental modernization from legacy view patterns to modern declarative UI.

### 9.2 Netflix: Production Kotlin Multiplatform
- **Netflix Studio Apps Powered by Kotlin Multiplatform**: [https://netflixtechblog.com/netflix-android-and-ios-studio-apps-kotlin-multiplatform-d6d4d8d25d23](https://netflixtechblog.com/netflix-android-and-ios-studio-apps-kotlin-multiplatform-d6d4d8d25d23)  
  *Application in Project:* Real-world validation of the **"Share logic, keep UI native"** paradigm—centralizing domain and networking logic in shared Kotlin while maintaining 100% native platform UI on Android and iOS.

### 9.3 Block / Cash App: Kotlin Multiplatform & Reactive Tooling
- **Cash App: Native UI and Multiplatform Redwood**: [https://code.cash.app/native-ui-and-multiplatform-compose-with-redwood](https://code.cash.app/native-ui-and-multiplatform-compose-with-redwood)  
  *Application in Project:* Demonstrates the power of shared Kotlin business models driving native mobile platform presentation.
- **Cash App Code Engineering Blog**: [https://code.cash.app/](https://code.cash.app/)  
  *Application in Project:* Reference engineering portal for modern Kotlin testing utilities (Turbine) and multiplatform architectures.

### 9.4 Square: Android Core Engineering
- **Square Engineering Blog**: [https://developer.squareup.com/blog/](https://developer.squareup.com/blog/)  
  *Application in Project:* Authoritative source on modern Android networking best practices, OkHttp/Retrofit engineering, and memory leak profiling (LeakCanary).

### 9.5 Japanese Engineering Benchmarks: Mercari & LINE
- **Mercari Engineering Portal**: [https://engineering.mercari.com/](https://engineering.mercari.com/)  
  *Application in Project:* Widely recognized standard in Japan for multi-module Android application design, micro-feature decoupling, and continuous delivery.
- **LINE / LY Corporation Engineering Portal**: [https://engineering.linecorp.com/](https://engineering.linecorp.com/)  
  *Application in Project:* Benchmark for large-scale enterprise Android super-app modularization and code quality guardrails.
