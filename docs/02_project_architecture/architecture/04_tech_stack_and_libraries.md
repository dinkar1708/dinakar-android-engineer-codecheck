# Architecture Specification: Technical Stack & Libraries (Bill of Materials)

## 1. Overview
The platform utilizes modern, production-hardened libraries aligning with official Google Android and JetBrains Kotlin Multiplatform recommendations.

---

## 2. Bill of Materials (BOM) & Versions

| Layer | Library / Framework | Version | Purpose & Rationale |
| :--- | :--- | :--- | :--- |
| **Build & Toolchain** | Gradle Wrapper | 8.5 | First Gradle release with native Java 21 support |
| **Build & Toolchain** | Android Gradle Plugin (AGP) | 8.2.2 | Compiles against API 34 natively without deprecation flags |
| **Build & Toolchain** | Kotlin Compiler | 1.9.22 | Powers multiplatform language features & fast incremental builds |
| **UI Framework** | Jetpack Compose BOM | 2024.02.00 | Modern declarative UI engine replacing legacy XML Views |
| **Design System** | Material 3 (`androidx.compose.material3`) | 1.2.0 | Material Design 3 tokens, dynamic theming, and accessibility |
| **Image Loading** | Coil Compose | 2.5.0 | High-performance asynchronous image loading with caching |
| **Navigation** | Navigation Compose | 2.7.7 | Single-Activity declarative Compose navigation |
| **Multiplatform Core**| Kotlinx Coroutines | 1.7.3 | Structured concurrency across Android and iOS |
| **Multiplatform Core**| Kotlinx Serialization | 1.6.2 | Compile-time type-safe JSON serialization/deserialization |
| **Networking** | Ktor Client | 2.3.7 | Multiplatform HTTP client with ContentNegotiation & MockEngine |
| **Dependency Injection**| Dagger Hilt | 2.50 | Compile-time dependency injection and ViewModel scoping |
| **Browser Integration**| AndroidX Browser (Custom Tabs)| 1.7.0 | In-app browser sessions preserving user cookies |
| **Crash & Bug Tracking**| Firebase Crashlytics | 18.6.0 | Real-time crash telemetry, non-fatal reporting, breadcrumbs, R8 symbolication |
| **Dev Diagnostics** | LeakCanary | 2.12 | Automated memory leak detection in debug variants |
| **Dev Diagnostics** | Android StrictMode | Platform API | Enforces main-thread disk/network IO violation detection |
| **CI/CD & Delivery** | Fastlane | 2.219.0 | Automated iOS Match signing, TestFlight & App Store upload automation |
| **Testing** | Kotlin Test | 1.9.22 | Multiplatform common unit testing assertions |
| **Testing** | Ktor MockEngine | 2.3.7 | In-memory simulated HTTP server for deterministic network tests |
| **Testing** | Turbine | 1.0.0 | CashApp coroutine Flow testing library |
| **Testing** | Coroutines Test | 1.7.3 | TestDispatchers and virtual time management (`runTest`) |
| **Testing** | Compose UI Test | 1.6.2 | Declarative composable verification and semantics querying |
| **Testing** | Hilt Android Testing | 2.50 | Dependency injection testing runner for instrumented tests |

---

## 3. Why These Libraries?
- **Ktor over Retrofit**: Retrofit is strictly JVM-only and relies on OkHttp. Ktor is 100% Kotlin Multiplatform, enabling our networking and data mapping to compile to iOS native binaries.
- **Kotlinx Serialization over Moshi/Gson**: Moshi and Gson rely heavily on Java reflection, which is slow and incompatible with Kotlin Multiplatform. Kotlinx Serialization generates reflectionless code at compile-time.
- **Jetpack Compose over XML**: Jetpack Compose eliminates ViewBinding nullability leaks, custom view boilerplate, and XML inflation overhead while enabling declarative state-driven UI.
- **Firebase Crashlytics & Developer Tooling**: Combines local breadcrumb logging and non-fatal exception capturing during development and dogfooding with enterprise real-time alerting and R8 symbolicated triage in production.
