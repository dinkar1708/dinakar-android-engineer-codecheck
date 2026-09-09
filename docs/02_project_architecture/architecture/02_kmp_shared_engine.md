# Architecture Specification: Kotlin Multiplatform (KMP) Shared Engine

## 1. Executive Summary & Design Pattern

The core data and business logic of the application is packaged in a standalone Kotlin Multiplatform module: **`:shared`**.

Following JetBrains' official **Pattern 2: "Share logic, keep UI native" (`logic-native-ui`)**, all networking, DTO serialization, domain modeling, and data caching are shared across platforms, while leaving Android UI 100% native in Jetpack Compose and iOS UI native in SwiftUI.

```mermaid
graph TD
    subgraph Multiplatform Shared Engine (:shared)
        CM[commonMain: Entities, Repository, Ktor, Serialization]
        CT[commonTest: Ktor MockEngine Unit Tests]
    end

    subgraph Android Native Consumer (:app)
        AAR[shared.aar]
        COMP[Jetpack Compose UI & Hilt DI]
    end

    subgraph iOS Native Consumer (SwiftUI)
        FW[shared.framework / XCFramework]
        SWIFT[SwiftUI Views & Swift Concurrency]
    end

    CM --> AAR
    CM --> FW
    AAR --> COMP
    FW --> SWIFT
```

---

## 2. Multiplatform Targets & Build Configuration (`shared/build.gradle`)

The `:shared` module configures both Android library and Apple iOS targets:

```groovy
plugins {
    id 'org.jetbrains.kotlin.multiplatform'
    id 'com.android.library'
    id 'kotlinx-serialization'
}

android {
    namespace 'jp.co.yumemi.android.code_check.shared'
    compileSdk 34
    defaultConfig {
        minSdk 24
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = '17' }
        }
    }

    // Apple Silicon & Intel iOS Framework Targets
    [iosX64(), iosArm64(), iosSimulatorArm64()].each { target ->
        target.binaries.framework {
            baseName = 'shared'
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"
            implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2"
            implementation "io.ktor:ktor-client-core:2.3.7"
            implementation "io.ktor:ktor-client-content-negotiation:2.3.7"
            implementation "io.ktor:ktor-serialization-kotlinx-json:2.3.7"
        }
        commonTest.dependencies {
            implementation kotlin('test')
            implementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
            implementation "io.ktor:ktor-client-mock:2.3.7"
        }
    }
}
```

---

## 3. Directory Layout & Shared Source Files

All multiplatform code lives under `shared/src/`:

```text
shared/
├── build.gradle                                # Multiplatform build configuration
└── src/
    ├── commonMain/kotlin/.../shared/
    │   ├── domain/
    │   │   ├── model/
    │   │   │   └── RepositoryItem.kt           # Shared domain entity (@Serializable)
    │   │   └── repository/
    │   │       └── GitHubRepository.kt         # Shared repository interface contract
    │   └── data/
    │       ├── network/
    │       │   ├── dto/
    │       │   │   └── SearchRepositoriesResponseDto.kt # Type-safe network DTOs
    │       │   └── GitHubApiService.kt         # Multiplatform Ktor HTTP client
    │       └── repository/
    │           └── DefaultGitHubRepository.kt  # Implementation with memory caching
    │
    └── commonTest/kotlin/.../shared/
        └── data/repository/
            └── DefaultGitHubRepositoryTest.kt  # 3 tests running on Ktor MockEngine
```

---

## 4. Binary Outputs Produced

Running `./gradlew :shared:assemble` produces:
1. **Android AAR**: `shared/build/outputs/aar/shared-debug.aar` (consumed by `:app`)
2. **iOS Frameworks**:
   - `shared/build/bin/iosArm64/debugFramework/shared.framework` (Physical iPhone / iPad)
   - `shared/build/bin/iosSimulatorArm64/debugFramework/shared.framework` (Apple Silicon Simulator)
   - `shared/build/bin/iosX64/debugFramework/shared.framework` (Intel Mac Simulator)

---

## 5. Defense & Justification for Technical Leadership
- **Zero Framework Leaks**: Business logic is completely isolated from the Android SDK.
- **Cross-Platform Readiness**: If the company decides to build an iOS app, the network client, caching, and repository logic can be dropped directly into Xcode via `shared.framework`.
- **Test Velocity**: Unit tests in `commonTest` execute on the local JVM via Ktor's `MockEngine` in milliseconds without launching the Android OS or emulators.
