# Architecture Overview

**Pattern**: Clean Architecture + MVVM + UDF (Unidirectional Data Flow)
**Target**: Multi-module, testable, maintainable architecture

---

## Current State

This project starts as a single-module application and will evolve through PRs:

### Initial State (Starting Point)
- Single `:app` module
- XML layouts with Fragments
- Direct API calls in UI layer
- No dependency injection
- No tests

### Final State (After All PRs)
- Multi-module architecture (multiple feature & core modules)
- 100% Jetpack Compose + Material 3
- Clean Architecture with Hilt DI
- Comprehensive test coverage
- Kotlin Multiplatform ready

---

## Target Architectural Layers

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│   Jetpack Compose (Material 3) + StateFlow UDF         │
└───────────────────────────┬────────────────────────────┘
                            │ observes state / sends actions
┌───────────────────────────▼────────────────────────────┐
│                      Domain Layer                      │
│   Entities + Repository Interfaces (Pure Kotlin)       │
└───────────────────────────▲────────────────────────────┘
                            │ implements interface
┌───────────────────────────┴────────────────────────────┐
│                       Data Layer                       │
│   Ktor HTTP Client + kotlinx.serialization + Cache     │
└────────────────────────────────────────────────────────┘
```

### 1. Presentation Layer
- **UI Framework**: 100% Jetpack Compose with Material 3 design tokens.
- **Pattern**: Unidirectional Data Flow (UDF). ViewModels expose immutable `StateFlow<UiState>` and consume user actions (`UiIntent`).
- **Lifecycle Resilience**: Uses `SavedStateHandle` to preserve search state across process recreation.
- **Responsive & Multi-Device Design**: Engineered and tested on Phone and Tablet viewports across both Vertical (Portrait) and Horizontal (Landscape) screens, utilizing adaptive `FlowRow` content wrapping and bounded scroll containers.

### 2. Domain Layer (Pure Kotlin)
- **Zero Framework Coupling**: Completely free from Android framework dependencies (`android.*`).
- **Core Entities**: Immutable domain models (`RepositoryItem`, `Owner`).
- **Repository Abstractions**: Interface definitions that specify contracts for data access.

### 3. Data Layer
- **Networking**: Ktor 3.x HTTP client with reflection-free `kotlinx.serialization`.
- **Resilience**: Exponential backoff with full jitter and HTTP 403 rate-limit detection.
- **Dependency Injection**: Hilt provides singleton repository instances and scoped dependencies.

---

## Architectural Transition Plan

Rather than a big-bang rewrite, the architecture evolves incrementally across PRs:

1. **Decouple UI from Logic**: Extract business logic and network calls from Fragments into ViewModels
2. **Domain & Data Separation**: Separate pure domain entities from Ktor data implementations
3. **Clean Architecture & DI**: Wire all layers using Hilt and enforce UDF with `StateFlow`
4. **UI Modernization**: Migrate XML layouts to Jetpack Compose

> **See [Issues Summary](./03_sprint_execution/03_issues_summary.md) for complete implementation roadmap**

---

## Key Technologies

| Technology | Purpose |
|:-----------|:--------|
| Kotlin | Primary language |
| Gradle 8.5 | Build system |
| Jetpack Compose | UI framework |
| Hilt | Dependency injection |
| Ktor | HTTP client |
| Kotlin Multiplatform (KMP) | Headless logic sharing (`:shared-core`) with native iOS SwiftUI app |
| Detekt | Static analysis |

---

## Incremental Approach

This project demonstrates **incremental refactoring** - taking a legacy codebase and modernizing it step-by-step through focused changes.

Each change:
- ✅ Small and reviewable
- ✅ Addresses specific issues
- ✅ Maintains working state
- ✅ Adds value incrementally

---

## Kotlin Multiplatform & Native iOS Companion Demonstration

The architecture includes a headless KMP core (`:shared-core`) that packages domain models, repository contracts, Ktor networking, and query caching as a universal Apple native binary (`shared_core.framework`).

A companion **native SwiftUI iOS application** (`iosApp/CodeCheck-iOS.xcodeproj`) is included in `iosApp/`:
- **Role**: *Architectural demonstration only* (not a full-fledged iOS product) showcasing cross-platform logic reuse from Kotlin Multiplatform.
- **Features**: Single search list screen with instant query clearing, 4 flavor schemes (`mock`, `dev`, `stg`, `prod`), and design token parity (`ColorTheme.swift`) matching Android `:core:designsystem`.
- **Reference**: See [`iosApp/README.md`](../iosApp/README.md) for full architectural documentation, Xcode setup, and build instructions.

---

## Project Management

**GitHub Project Board:** [Mobile Platform Engineering - Issue Tracker](https://github.com/users/dinkar1708/projects/1/views/1)

The project board tracks all issues, milestones, and implementation progress across the complete SDLC lifecycle. All 9 code challenge issues plus bonus features are managed through this centralized board (see [Technical References](./references.md#7-github-api--assessment-standards)).

---

**Current Phase**: Implementation Complete
**Status**: All core architecture, multi-module structure, product flavors (dev/mock/stg/prod), and KMP shared module implemented
