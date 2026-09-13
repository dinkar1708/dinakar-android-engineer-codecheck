# Screenshots & Media Assets

This directory contains all screenshots and media assets referenced in the project README.

## Required Screenshots

Add the following screenshots to make the README complete:

### Main Application Screenshots (Top of README)

Referenced directly from `app/light/` and `app/dark/`:

1. **`app/light/03-search-results.png`** - Search screen showing repository search interface
2. **`app/light/05-detail-screen.png`** - Repository details screen with stats and owner info
3. **`app/dark/03-search-results.png`** - App in dark mode (search results)
4. **`app/light/06-settings-screen.png`** - Settings screen with theme/language toggles

### iOS Screenshots (Kotlin Multiplatform Companion App)

5. **`ios/ios-app-demo-using-kmp.png`** - Native SwiftUI app running on iPhone 17 Pro simulator with Xcode scheme selector, executing live repository search.
6. **`ios/ios-app-using-kmp.png`** - Xcode project tree and architecture showing Swift views and `shared_core.framework` integration.

### Design Specifications & AI Prototypes

7. **`claude-design/`** - Interactive design demo video (`design_demo.mov`) and HTML/CSS component/screen exports (`screens/`).

### Compose Previews (Fast Development Velocity)

8. **`compose_preview/preview.png`** - Android Studio `@Preview` split-editor canvas showing sub-second visual feedback loops without emulator lag. Instant verification of all UI states (`Light`, `Dark`, `Loading`, `Empty`, `Error`). See [**`docs/screenshots/compose_preview/README.md`**](./compose_preview/README.md).

### Android Studio Profiler & Developer Inspection Suite

9. **`tools/`** - Android Studio performance telemetry captures. See [**`docs/screenshots/tools/README.md`**](./tools/README.md).
   - **`profiler_overview.png`** & **`profile_home.png`**: Unified telemetry dashboard & process initialization
   - **`cpu-zero_leaks.png`**: CPU Profiler & System Trace verifying 0 main-thread blocks (<16ms frame time)
   - **`memory_heap_dump.png`** & **`memory.png`**: Heap Dump & allocation tracking verifying 0 memory leaks
   - **`network.png`** & **`network_thread_view.png`**: Network Inspector tracing 300ms debounce & cache hits

### Developer Tooling & Build Variants

10. **`dev/build_variants.png`** - Android Studio Build Variants window showing seamless 1-click switching across `dev`, `mock`, `stg`, and `prod` configurations.

### CI Quality Gates & Coverage Proofs

11. **`ci/ci_pipeline.png`** - GitHub Actions automated build, test, and Detekt verification pipeline.
12. **`coverage/ci_coverage.png`** - Kotlinx Kover test coverage report showing 81.2% repository-wide line coverage.

### GitHub Project & Delivery Board

13. **`github-project/project-board.png`** - GitHub Projects Kanban board tracking agile sprint delivery across issues #3–#11.

## Video

### Demo Video

Add to `../videos/`:

- **`demo.mov`** (or `demo.mp4`) - Short demo video (30-60 seconds) showing:
  - App launch (splash screen)
  - Search for repositories
  - View repository details
  - Toggle dark mode
  - Navigate between screens

## Screenshot Guidelines

### Recommended Specifications

- **Format:** PNG (for screenshots), MP4 (for video)
- **Resolution:** 1080p or higher (Android), Retina display (iOS)
- **Device:** Use a modern Android device or emulator (Pixel 6/7)
- **Content:**
  - Show real GitHub search results
  - Use the `mock` flavor to avoid rate limits
  - Capture in both light and dark modes
  - Include varied screen states (loading, error, success)

### Taking Screenshots

#### Android Studio

1. Run the app on an emulator or device
2. Use Android Studio's screenshot tool (camera icon in device controls)
3. Or use `adb shell screencap -p /sdcard/screenshot.png`

#### From Device

- **Android:** Volume Down + Power button
- **iOS Simulator:** Cmd + S

### Video Recording

#### Android Studio

1. Run app on emulator
2. Click "Record" button in device controls (screen record icon)
3. Perform demo actions
4. Stop recording and save as `demo.mp4`

#### Using ADB

```bash
adb shell screenrecord /sdcard/demo.mp4
# Perform demo actions (max 3 minutes)
# Press Ctrl+C to stop
adb pull /sdcard/demo.mp4 docs/videos/demo.mp4
```

## Design Screenshots from Claude

To capture design specification screenshots:

1. Open `docs/02_project_architecture/design/html/00 Index.html` in a browser
2. Navigate through different screens
3. Take screenshots or create a collage showing:
   - Color palette
   - Typography
   - Component library
   - Screen designs
4. Store in `docs/screenshots/claude-design/` (and side-by-side comparisons in `docs/screenshots/comparisons/`).

### In-Depth Technical Guide
For complete capture instructions, ADB shell commands, and image compression guidelines, see [**`screenshots_guide.md`**](./screenshots_guide.md).

### Creating a Collage

Use an image editor (Figma, Photoshop, or online tool) to create a grid collage showing multiple design screens side-by-side.

## Status

- [x] app/light/03-search-results.png
- [x] app/light/05-detail-screen.png
- [x] app/dark/03-search-results.png
- [x] app/light/06-settings-screen.png
- [x] ios/ios-app-demo-using-kmp.png & ios/ios-app-using-kmp.png (Native iOS SwiftUI companion app via KMP)
- [x] claude-design/ (Design demo video and screen specs)
- [x] compose_preview/preview.png (Compose @Preview canvas screenshot)
- [x] tools/ (Android Studio CPU, Network, Memory Profiler, and Telemetry captures)
- [x] coverage/ci_coverage.png (Kotlinx Kover code coverage report)
- [x] ci/ci_pipeline.png (GitHub Actions CI workflow verification)
- [x] dev/build_variants.png (Product flavors & build variant selector)
- [x] github-project/project-board.png (GitHub Projects delivery Kanban board)
- [x] demo.mov (Phone Walkthrough) & tablet_demo.mov (Tablet Walkthrough) in `docs/videos/`


Once you add these files, the README will display them automatically!
