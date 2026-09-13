# Screenshots & Media Assets

This directory contains all screenshots and media assets referenced in the project README.

## Required Screenshots

Add the following screenshots to make the README complete:

### Main Application Screenshots (Top of README)

Place these 4 images in this directory:

1. **`search.png`** - Search screen showing the repository search interface
2. **`details.png`** - Repository details screen with stats and owner info
3. **`dark.png`** - App in dark mode (any screen)
4. **`settings.png`** - Settings screen with theme/language toggles

### iOS Screenshots

5. **`ios_screenshot.png`** - iOS companion app search screen

### Architecture Diagrams

6. **`kmp_architecture.png`** - Kotlin Multiplatform architecture diagram (optional)

### Design Specifications

7. **`design_specs_collage.png`** - Collage showing Claude design HTML specs (optional)

## Video

### Demo Video

Add to `../videos/`:

- **`demo.mp4`** - Short demo video (30-60 seconds) showing:
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

- [ ] search.png
- [ ] details.png
- [ ] dark.png
- [ ] settings.png
- [ ] ios_screenshot.png
- [ ] kmp_architecture.png (optional)
- [ ] design_specs_collage.png (optional)
- [ ] demo.mp4

Once you add these files, the README will display them automatically!
