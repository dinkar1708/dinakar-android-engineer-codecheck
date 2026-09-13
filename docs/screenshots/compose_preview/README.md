# ⚡ Jetpack Compose `@Preview`: Fast Inner-Loop Development

This directory is dedicated to **Jetpack Compose `@Preview` screenshots**, showcasing how declarative UI enables rapid development velocity inside Android Studio.

---

## 🚀 Why Compose Previews Make Development So Fast (In Short)

1. ⚡ **Sub-Second Feedback (Zero Build Wait Time)**
   - Edit UI code and see changes **instantly** on the design canvas.
   - Eliminates waiting minutes for full APK builds (`./gradlew assembleDebug`), DEXing, or launching an emulator.

2. 🔄 **All States Side-by-Side on One Canvas**
   - View every UI scenario simultaneously in a single split-view window:
     - ☀️ **Light Mode** & 🌙 **Dark Mode**
     - ⏳ **Loading State** (shimmer skeleton cards)
     - 📭 **Empty State** (no results / initial state)
     - ⚠️ **Error State** (actionable retry buttons)

3. 🔌 **100% Offline & Decoupled from Backend**
   - Previews use deterministic fake data models (`PreviewData`).
   - UI engineers can build, polish, and review complex screens completely offline without network latency, backend availability, or GitHub API rate limits.

4. 📱 **Multi-Device & Responsive Verification**
   - Inspect Phone (`412x892dp`) vs. Tablet (`800x1280dp`) layouts and Japanese vs. English typography side-by-side without device rotation or app reinstall.

5. 🖱️ **Interactive Sandbox**
   - Test button clicks, ripple feedback, and micro-interactions directly inside Android Studio using Compose Interactive Mode.

---

## 📸 Previews Gallery

> 💡 **Drop your preview screenshot files here!**  
> You can save individual state captures or full split-editor canvas screenshots:

<div align="center">

### Search Screen Previews
| Success (Light & Dark) | Loading & Empty | Error State |
|:---:|:---:|:---:|
| ![Search Success Previews](./01-search-states-preview.png) | ![Search Loading & Empty](./02-search-empty-loading-preview.png) | ![Search Error](./03-search-error-preview.png) |

### Detail & Navigation Previews
| Repository Details | Bottom Navigation & Tabs | Reusable Components |
|:---:|:---:|:---:|
| ![Detail Previews](./04-detail-states-preview.png) | ![Navigation Previews](./05-navigation-preview.png) | ![Components Preview](./06-components-preview.png) |

### Main Compose Canvas Overview
![Compose Studio Canvas Overview](./compose_preview.png)

</div>

---

### 📋 Supported Screenshot Naming:
- `compose_preview.png` - Full Android Studio canvas overview
- `01-search-states-preview.png` - SearchScreen split previews
- `02-search-empty-loading-preview.png` - Search loading shimmer & empty states
- `03-search-error-preview.png` - Search error state with retry
- `04-detail-states-preview.png` - DetailScreen light & dark states
- `05-navigation-preview.png` - Bottom navigation & app bar previews
- `06-components-preview.png` - RepositoryCard, FilterBar, and atomic components

