# Screenshots Documentation Guide

This document explains what screenshots to capture and where to store them for project documentation and presentations.

## Directory Structure

```
docs/screenshots/
├── app/                        # Production Android App UI screenshots
│   ├── light/                  # Light theme screens (Search, Detail, Starred, Settings)
│   ├── dark/                   # Dark theme screens
│   ├── landscape/              # Phone landscape orientation (Search, Detail, Starred, Settings)
│   └── tablet/                 # Pixel Tablet landscape orientation (2560x1600)
├── claude-design/              # 🎨 Claude Design / AI prototyping concept mockups
│   ├── screens/                # Full screen concept exports
│   └── components/             # Atomic component previews (RepoCard, StatCard, Palette)
├── comparisons/                # 🔄 Side-by-Side Visual Fidelity (Claude Design vs Compose)
├── compose_preview/            # ⚡ Compose @Preview fast development captures
├── tools/                      # 🛠️ Android Studio Performance & Profiler telemetry captures
├── ios/                        # 🍏 Native iOS SwiftUI Companion App Screenshots
│   ├── ios-app-demo-using-kmp.png # Live SwiftUI app on iPhone 17 Pro simulator
│   └── ios-app-using-kmp.png      # Xcode project tree and shared_core integration
├── demos/                      # 🎥 Walkthrough videos (MP4/MOV) and animated GIFs (e.g. app.gif)
├── github-project/             # 📋 GitHub Project Delivery Kanban Board captures
├── coverage/                   # 📊 Test coverage reports (Kotlinx Kover / Jacoco)
└── ci/                         # 🚀 GitHub Actions CI pipeline execution proofs
```

## Required Screenshots for Documentation

### 1. Application UI Screenshots

Capture ALL implemented screens in BOTH themes and orientations:

#### Search Screen
**Files to capture:**
- `app/light/01-search-empty.png` - Empty search state (light theme)
- `app/dark/01-search-empty.png` - Empty search state (dark theme)
- `app/light/02-search-loading.png` - Loading state
- `app/dark/02-search-loading.png` - Loading state
- `app/light/03-search-results.png` - Search results displayed
- `app/dark/03-search-results.png` - Search results displayed
- `app/light/04-search-error.png` - Error state
- `app/dark/04-search-error.png` - Error state

**Landscape versions:**
- `app/landscape/01-search-results-landscape.png`

#### Repository Detail Screen
**Files to capture:**
- `app/light/05-detail-screen.png` - Repository details (light)
- `app/dark/05-detail-screen.png` - Repository details (dark)
- `app/landscape/02-detail-landscape.png` - Detail in landscape

#### Settings Screen
**Files to capture:**
- `app/light/06-settings-screen.png` - Settings (light)
- `app/dark/06-settings-screen.png` - Settings (dark)
- `app/light/07-settings-language.png` - Language options
- `app/light/08-settings-theme.png` - Theme options

#### Splash Screen
**Files to capture:**
- `app/light/00-splash.png` - Splash screen animation

### 2. Code Coverage Screenshots

Capture test coverage report to demonstrate testing quality:

**How to generate:**
```bash
./gradlew testDevDebugUnitTest
./gradlew jacocoTestReport
```

**Screenshot to capture:**
- `coverage/code-coverage.png` - HTML coverage report showing percentage
- Highlight: Overall coverage, package coverage, class coverage

**What to show:**
- Overall line coverage percentage
- Coverage of ViewModels (should be 80%+)
- Coverage of Repositories (should be 90%+)
- Coverage breakdown by package

### 3. CI/CD Pipeline Screenshots

**Files to capture:**
- `ci/github-actions-success.png` - Green CI pipeline
- `ci/workflow-steps.png` - Detailed workflow steps
- `ci/test-results.png` - Test execution results

### 4. Special Features Screenshots

#### Multi-Language Support
**Files to capture:**
- `app/light/09-english.png` - App in English
- `app/light/10-japanese.png` - App in Japanese

#### Product Flavors
**Files to capture:**
- `app/light/11-dev-flavor.png` - Settings showing DEV environment
- `app/light/12-mock-flavor.png` - Settings showing MOCK environment

### 5. Compose Previews: Fast Inner-Loop Development

Jetpack Compose `@Preview` annotations dramatically accelerate mobile engineering velocity:
- **Sub-Second Iteration:** Renders UI instantly inside Android Studio without assembling APKs, launching emulators, or deploying to devices.
- **Simultaneous Multi-State Validation:** Visualizes all states (`Success Light`, `Success Dark`, `Loading`, `Empty`, `Error`) side-by-side in real time.
- **Zero API Dependency:** Powered by deterministic preview models, decoupling UI construction from backend availability.

**Files to capture in `compose_preview/`:**
- `compose_preview/01-search-states-preview.png` - SearchScreen previews in Android Studio split view
- `compose_preview/02-detail-states-preview.png` - DetailScreen previews (Light, Dark, Loading, Error)
- `compose_preview/03-components-preview.png` - Atomic component previews (RepositoryCard, Chips, EmptyView)

### 6. Android Studio Profiler & Performance Telemetry

Capture empirical telemetry proving performance and responsiveness (see [Performance Profiling Guide](../02_project_architecture/testing/05_performance_profiling_and_benchmarks.md)):

**Files captured in `tools/`:**
- `tools/profiler_overview.png` & `tools/profile_home.png` - Android Studio complete profiling dashboard and process initialization
- `tools/cpu-zero_leaks.png` - System Trace proving 0 main-thread blocks (<16ms)
- `tools/network.png` & `tools/network_thread_view.png` - Network Inspector showing 300ms debounce, background coroutine worker threads, and 0ms in-memory cache hits
- `tools/memory_heap_dump.png` & `tools/memory.png` - Java Heap Dump & allocation tracking proving 0 memory leaks across 10+ screen transitions

## How to Capture Screenshots

### On Android Emulator

1. **Start emulator** with desired configuration:
   - Phone: Pixel 6 (1080 x 2400)
   - Tablet: Pixel Tablet (2560 x 1600) for landscape

2. **Set theme:**
   - Light theme: Settings → Display → Light
   - Dark theme: Settings → Display → Dark

3. **Capture screenshot:**
   - Click camera icon in emulator toolbar
   - Or use: `Ctrl + S` (Windows/Linux) or `Cmd + S` (Mac)

4. **Crop if needed:**
   - Remove system status bar if not relevant
   - Keep app bar and content area

### Recommended Screenshot Specs

- **Format**: PNG (lossless)
- **Resolution**: Keep original device resolution
- **Size**: Compress to under 500KB per image
- **Naming**: Use descriptive names with numbers for ordering

## Screenshot Organization Tips

### File Naming Convention

Use this format:
```
[number]-[feature]-[state]-[variant].png
```

Examples:
- `01-search-empty-light.png`
- `02-search-loading-dark.png`
- `03-search-results-landscape.png`

### Compression

Before committing, compress images:

**Using ImageOptim (Mac):**
```bash
brew install imageoptim-cli
imageoptim docs/screenshots/app/*.png
```

**Using pngquant (Cross-platform):**
```bash
pngquant --quality=65-80 --ext .png --force docs/screenshots/app/*.png
```

## Usage in Documentation

### In readme.md

Show key features with screenshots:

```markdown
## Features

### Search Repositories
![Search Screen](docs/screenshots/app/light/03-search-results.png)

### Dark Theme Support
| Light Theme | Dark Theme |
|-------------|------------|
| ![Light](docs/screenshots/app/light/03-search-results.png) | ![Dark](docs/screenshots/app/dark/03-search-results.png) |

### Landscape Mode
![Landscape](docs/screenshots/app/landscape/01-search-results-landscape.png)
```

### In Pull Requests

Show before/after for UI changes:

```markdown
## Screenshots

| Before | After |
|--------|-------|
| ![Before](docs/screenshots/pr/before-search.png) | ![After](docs/screenshots/pr/after-search.png) |
```

### In Executive Design Reviews

For executive stakeholder reviews and design presentations, create a slide deck with:

1. **Slide 1**: All screens overview (4-6 screenshots in grid)
2. **Slide 2**: Light vs Dark theme comparison
3. **Slide 3**: Portrait vs Landscape responsiveness
4. **Slide 4**: Code coverage metrics
5. **Slide 5**: CI/CD pipeline success

## Checklist: Complete Screenshot Set

Use this checklist when preparing documentation:

### Application Screens
- [x] Splash screen (`00-splash.png`)
- [x] Search screen (empty state) (`01-search-empty.png`)
- [x] Search screen (loading state) (`02-search-loading.png`)
- [x] Search screen (results) (`03-search-results.png`)
- [x] Search screen (error state) (`04-search-error.png`)
- [x] Detail screen (`05-detail-screen.png`)
- [x] Settings screen (`06-settings-screen.png`)
- [x] Settings (language selection) (`07-settings-language.png`)
- [x] Settings (theme selection) (`08-settings-theme.png`)
- [x] Starred screen (`14-starred-screen.png`)

### Theme Coverage
- [x] All screens in light theme (`app/light/`)
- [x] All screens in dark theme (`app/dark/`)

### Orientation Coverage
- [x] Key screens in portrait (`app/light/`, `app/dark/`)
- [x] Key screens in phone landscape (`app/landscape/`)
- [x] Key screens in tablet landscape (`app/tablet/`)

### Localization Coverage
- [x] App in English (`09-english.png`)
- [x] App in Japanese (`10-japanese.png`)

### Build Flavors
- [x] DEV flavor indicator (`11-dev-flavor.png`)
- [x] MOCK flavor indicator (`12-mock-flavor.png`)
- [x] PROD / Build variant selector (`dev/build_variants.png`)

### Quality & Tooling Metrics
- [x] Code coverage report (`coverage/ci_coverage.png` — 81.2% line coverage)
- [x] CI pipeline success (`ci/ci_pipeline.png` — GitHub Actions build/test/detekt)
- [x] Android Studio Profiler Telemetry (`tools/` — CPU 0 ANRs, Memory 0 Leaks, Network 300ms debounce)
- [x] Fast Inner-Loop Compose Previews (`compose_preview/preview.png`)
- [x] Native iOS Companion App (`ios/ios-app-demo-using-kmp.png`, `ios-app-using-kmp.png`)
- [x] GitHub Agile Project Delivery Board (`github-project/project-board.png`)
- [x] Claude AI Design Prototype Specs & Video (`claude-design/`)

## Screenshot Maintenance

**Update screenshots when:**
- UI design changes significantly
- New features are added
- Material 3 theme tokens are modified
- Fixing bugs that affect visual appearance

**Review schedule:**
- Before major releases
- Before milestone releases or executive demonstrations
- Quarterly to keep documentation fresh

## Tools and Apps for Screenshots

### Android Studio
- Built-in emulator screenshot tool
- Device File Explorer for accessing screenshots

### Third-Party Tools
- **Screenshot Framer**: Add device frames for professional look
- **Figma**: Combine screenshots into presentation layouts
- **ImageOptim**: Compress PNG files without quality loss

### Automation (Advanced)

For automated screenshot testing:

```kotlin
// In androidTest
@Test
fun captureSearchScreen() {
    composeTestRule.setContent {
        SearchScreen(viewModel = viewModel)
    }
    composeTestRule.onNodeWithTag("SearchScreen")
        .captureToImage()
        .asAndroidBitmap()
        .save("app/light/03-search-results.png")
}
```

## Best Practices

1. **Consistency**: Use same device type for all screenshots
2. **Realistic data**: Use meaningful repository names, not "Test1", "Test2"
3. **Clean state**: Clear notifications, set time to 10:00 AM
4. **Focus**: Highlight the feature being demonstrated
5. **Accessibility**: Ensure text is readable in compressed images

## Example Screenshot Set

Here's an example of a complete screenshot set structure:

```
docs/screenshots/
├── app/
│   ├── light/
│   │   ├── 00-splash.png
│   │   ├── 01-search-empty.png
│   │   ├── 02-search-loading.png
│   │   ├── 03-search-results.png
│   │   ├── 04-search-error.png
│   │   ├── 05-detail-screen.png
│   │   ├── 06-settings-screen.png
│   │   ├── 07-settings-language.png
│   │   ├── 08-settings-theme.png
│   │   ├── 09-english.png
│   │   ├── 10-japanese.png
│   │   ├── 11-dev-flavor.png
│   │   ├── 12-mock-flavor.png
│   │   └── 14-starred-screen.png
│   ├── dark/
│   │   ├── 01-search-empty.png
│   │   ├── 02-search-loading.png
│   │   ├── 03-search-results.png
│   │   ├── 04-search-error.png
│   │   ├── 05-detail-screen.png
│   │   ├── 06-settings-screen.png
│   │   └── 14-starred-screen.png
│   ├── landscape/
│   │   ├── 01-search-results-landscape.png
│   │   ├── 02-detail-landscape.png
│   │   ├── 03-starred-landscape.png
│   │   └── 04-settings-landscape.png
│   └── tablet/
│       ├── 01-search-empty-tablet.png
│       ├── 02-search-results-tablet.png
│       ├── 03-detail-tablet.png
│       ├── 04-starred-tablet.png
│       └── 05-settings-tablet.png
├── coverage/
│   ├── code-coverage.png
│   ├── viewmodel-coverage.png
│   └── repository-coverage.png
└── ci/
    ├── github-actions-success.png
    ├── workflow-steps.png
    └── test-results.png
```

## For Executive & Client Demonstrations

When preparing for executive stakeholder walkthroughs, client demos, or technical architecture reviews, create a presentation-ready set:

### Slide 1: Application Overview
- Grid of 6 screenshots showing main features
- Both light and dark themes
- Professional device frames

### Slide 2: Engineering Excellence
- Code coverage report (aim for 80%+)
- CI/CD pipeline green status
- Architecture diagram

### Slide 3: Cross-Platform Capability
- Android app screenshots
- iOS app screenshots (if KMP demo available)
- Shared code statistics

### Slide 4: Team Impact
- Before/after comparison showing improvements
- PR review process screenshots
- Documentation quality examples

---

**Note:** Always update this guide when adding new screenshot categories or changing the organization structure.
