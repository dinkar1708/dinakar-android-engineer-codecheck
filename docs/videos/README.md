# Demo Videos

This directory contains demo videos referenced in the project README.

## Required Video

- **`demo.mp4`** - Main application demo video

## Video Specifications

### Content Requirements

The demo video should showcase:

1. **App Launch** (5s)
   - Splash screen with animated logo
   - Smooth transition to main screen

2. **Repository Search** (15s)
   - Type search query (e.g., "android")
   - Show debounced search in action
   - Display repository list results
   - Scroll through multiple results

3. **Repository Details** (10s)
   - Tap on a repository card
   - Show detail screen with:
     - Owner profile image
     - Repository stats (stars, forks, watchers, issues)
     - Language badge
     - Description

4. **Navigation** (10s)
   - Navigate to Starred/Bookmarks tab
   - Star a repository
   - Navigate to Settings

5. **Theme Toggle** (10s)
   - Open Settings
   - Switch between Light/Dark mode
   - Show instant theme change without restart

6. **Bonus Features** (10s) - Optional
   - Language switcher (English ↔ 日本語)
   - Offline mode handling
   - Error state display

### Technical Specifications

- **Format:** MP4 (H.264 codec)
- **Resolution:** 1080p (1920x1080) or higher
- **Frame Rate:** 30 fps minimum
- **Duration:** 30-60 seconds (max 2 minutes)
- **Audio:** Optional (silent is fine for technical demo)
- **File Size:** Try to keep under 50MB for GitHub

## Recording the Video

### Option 1: Android Studio Emulator

```bash
# Start recording
1. Run app on emulator
2. Click screen record button (video camera icon) in device toolbar
3. Perform demo actions
4. Click stop button
5. Save as demo.mp4
```

### Option 2: ADB Command Line

```bash
# Start recording (max 3 minutes)
adb shell screenrecord /sdcard/demo.mp4

# Perform your demo actions in the app

# Stop recording (Ctrl+C)
# Pull the video file
adb pull /sdcard/demo.mp4 docs/videos/demo.mp4
```

### Option 3: Physical Device

**Android:**
- Use built-in screen recorder (varies by manufacturer)
- Or use apps like AZ Screen Recorder
- Transfer file via USB or cloud storage

**iOS Simulator (for KMP companion app):**
```bash
# Start recording
xcrun simctl io booted recordVideo demo.mov

# Perform demo actions

# Stop recording (Ctrl+C)

# Convert to MP4 if needed
ffmpeg -i demo.mov -vcodec h264 -acodec aac docs/videos/demo.mp4
```

## Editing Tips

### Trim/Compress Video

If your video is too large:

```bash
# Using ffmpeg to compress
ffmpeg -i demo.mp4 -vcodec h264 -crf 28 -preset fast docs/videos/demo.mp4

# Or trim to specific duration
ffmpeg -i demo.mp4 -ss 00:00:00 -t 00:01:00 -c copy docs/videos/demo_trimmed.mp4
```

### Add Speed-Up Effect

To show more features in less time:

```bash
# Speed up 1.5x
ffmpeg -i demo.mp4 -filter:v "setpts=0.66*PTS" docs/videos/demo_fast.mp4
```

## Best Practices

1. **Practice First** - Run through the demo a few times before recording
2. **Smooth Interactions** - Avoid rapid tapping, use smooth gestures
3. **Realistic Data** - Use the `mock` flavor with realistic repository names
4. **Clean State** - Start from a clean app state (clear navigation stack)
5. **Steady Frame** - If using physical device, keep it steady or use a phone stand
6. **Good Lighting** - If recording physical device screen, ensure good lighting
7. **No Personal Data** - Don't show personal accounts or sensitive information

## Status

- [ ] demo.mp4

Once you add this file, the README video link will work!
