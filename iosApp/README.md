# 🍏 CodeCheck-iOS Demonstration App

> [!IMPORTANT]
> **Technical Demonstration Only — Not a Complete Application**  
> This iOS companion app is **not a full-featured iOS application**. It is strictly a **lightweight architectural demonstration** designed to validate and prove cross-platform logic reusability using **Kotlin Multiplatform (KMP)**.
>
> It demonstrates that our Kotlin business logic, repository contracts, caching layer, offline mock datasets, and Ktor HTTP networking (`:shared-core`) compile into a native Apple framework (`shared_core.framework`) and are seamlessly consumable by a native SwiftUI client using Swift Concurrency (`async`/`await`). The scope is intentionally restricted to a clean search list with console logging to showcase KMP interop without superfluous mobile UI complexity.

---

## 🏛️ Architecture: Headless Kotlin Multiplatform

The iOS companion app demonstrates a **Headless KMP** architecture: business logic, networking, data caching, and offline datasets are shared 100% in Kotlin, while the presentation tier is built natively in SwiftUI with Apple platform conventions.

```mermaid
graph TD
    subgraph iOS Native Presentation
        SwiftUI[SwiftUI Views<br/>ContentView.swift] --> VM[SearchViewModel.swift<br/>@MainActor / Swift Concurrency]
    end

    subgraph KMP Shared Engine [:shared-core]
        VM -->|Swift async/await| Facade[SharedCore Facade]
        Facade --> Domain[:core:domain<br/>RepositoryItem, Owner]
        Facade --> Data[:core:data<br/>DefaultGitHubRepository, MockGitHubRepository]
        Facade --> Network[:core:network<br/>Ktor Darwin Engine]
    end

    Data -->|Live REST| GitHubAPI[GitHub REST API]
    Data -->|Offline Fixture| MockData[Deterministic Datasets]
```

### 🔑 Key Engineering Highlights
- **Zero-Glue Interop:** Kotlin Native translates `suspend fun` directly into Swift `async throws`. Swift views and ViewModels interact with shared repositories natively using `try await`.
- **Identical Color Palette:** Native `ColorTheme.swift` matches the Android design system (`:core:designsystem`) tokens:
  - Header & Dark Surfaces: `AppNavy` (`#2D3545`)
  - Primary Action / Links: `AppBlue` (`#3B50DF`)
  - Positive / Match: `AppGreen` (`#2DB36C`)
  - Stars / Ratings: `AppAmber` (`#F59E0B`)
  - Slate Neutral Ramp: `Slate900` (`#0F172A`) through `Slate50` (`#F5F6F8`)
- **Flavor Switching (`mock`, `dev`, `stg`, `prod`):**
  - **`CodeCheck-iOS-Mock` Scheme:** 100% offline mode with bundled fixtures, 0 rate limits, instant responses.
  - **`CodeCheck-iOS-Dev` Scheme:** Development environment querying live REST API.
  - **`CodeCheck-iOS-Stg` Scheme:** Staging environment with pre-release validation.
  - **`CodeCheck-iOS-Prod` Scheme:** Production environment with standard GitHub REST API.
- **Automatic Code Signing:** Pre-configured with Automatic code signing and Development Team (`L77B9M38KX` / `DelQui inc.`) for instant execution on Simulators and physical devices.

---

## 🚀 Quick Start

### 1. Build KMP Shared Framework

Generate the Apple Silicon debug framework using Gradle:

```bash
# For iOS Simulator (M-series Apple Silicon Mac)
./gradlew :shared-core:linkDebugFrameworkIosSimulatorArm64

# For Physical iOS Device
./gradlew :shared-core:linkDebugFrameworkIosArm64
```

Framework output location:
`shared-core/build/bin/iosSimulatorArm64/debugFramework/shared_core.framework`

### 2. Open in Xcode

```bash
open iosApp/CodeCheck-iOS.xcodeproj
```

1. Select your target scheme (**`CodeCheck-iOS-Mock`**, **`CodeCheck-iOS-Dev`**, **`CodeCheck-iOS-Stg`**, or **`CodeCheck-iOS-Prod`**) from the Scheme selector.
2. Select any **iOS Simulator** (e.g. *iPhone 16* on iOS 18+).
3. Press **Run** (`⌘R`).

### 3. Run Native Unit Tests

Execute the automated Swift Testing test suite via Xcode (`⌘U`) or command line:

```bash
xcodebuild test \
  -project iosApp/CodeCheck-iOS.xcodeproj \
  -scheme "CodeCheck-iOS-Mock" \
  -destination "platform=iOS Simulator,name=iPhone 16,OS=18.4"
```

---

## 📁 Project Structure

```
iosApp/
├── CodeCheck-iOS.xcodeproj/             # Modern Xcode project (Xcode 15/16+)
│   ├── project.pbxproj                  # Direct framework search paths & linker flags
│   └── xcshareddata/xcschemes/          # Shared schemes (Mock, Dev, Stg, Prod)
├── CodeCheck-iOS/                       # SwiftUI source files
│   ├── CodeCheckApp.swift               # @main entry point
│   ├── ContentView.swift                # Simple, clean search list (tap prints console log)
│   ├── SearchViewModel.swift            # @MainActor ViewModel querying KMP SharedCore
│   ├── ColorTheme.swift                 # Brand tokens matching Android design system
│   └── Assets.xcassets/                 # App icon & brand accent color
├── CodeCheck-iOSTests/                  # Native Swift Testing unit test suite
│   └── CodeCheckTests.swift             # Automated tests validating KMP interop
└── README.md                            # Documentation
```
