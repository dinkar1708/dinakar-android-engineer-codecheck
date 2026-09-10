# Operations Guide: Build & Export Guide (Android & iOS)

## 1. Overview
This operational guide provides step-by-step instructions for packaging production and debug artifacts for both Android and iOS platforms.

---

## 2. Android Artifacts

### Product Flavors
This project uses three product flavors in the `environment` dimension:
- **dev**: Development flavor with `.dev` suffix for parallel installation
- **mock**: Mock flavor with `.mock` suffix for offline development
- **prod**: Production flavor (no suffix)

### 1. Build Flavor-Specific Debug APKs:

#### Dev Debug APK:
```bash
./gradlew assembleDevDebug
```
- **Output Artifact**: `app/build/outputs/apk/dev/debug/app-dev-debug.apk`
- **Application ID**: `jp.co.yumemi.android.codecheck.dev`
- **Version Suffix**: `-dev`

#### Prod Debug APK:
```bash
./gradlew assembleProdDebug
```
- **Output Artifact**: `app/build/outputs/apk/prod/debug/app-prod-debug.apk`
- **Application ID**: `jp.co.yumemi.android.codecheck`

#### Mock Debug APK:
```bash
./gradlew assembleMockDebug
```
- **Output Artifact**: `app/build/outputs/apk/mock/debug/app-mock-debug.apk`
- **Application ID**: `jp.co.yumemi.android.codecheck.mock`
- **Version Suffix**: `-mock`

#### Build All Debug Variants:
```bash
./gradlew assembleDebug
```
- Builds debug APKs for all three flavors (dev, mock, prod)

### 2. Build Flavor-Specific Release APKs:

#### Dev Release APK:
```bash
./gradlew assembleDevRelease
```
- **Output Artifact**: `app/build/outputs/apk/dev/release/app-dev-release-unsigned.apk`

#### Prod Release APK:
```bash
./gradlew assembleProdRelease
```
- **Output Artifact**: `app/build/outputs/apk/prod/release/app-prod-release-unsigned.apk`

### 3. Install APKs to Device:

#### Install Dev Debug:
```bash
./gradlew installDevDebug
```

#### Install Prod Debug:
```bash
./gradlew installProdDebug
```

#### Manual Installation via ADB:
```bash
adb install -r app/build/outputs/apk/dev/debug/app-dev-debug.apk
adb install -r app/build/outputs/apk/prod/debug/app-prod-debug.apk
```

### 4. List All Available Build Variants:
```bash
./gradlew app:variants
```

### 5. Build Shared Library AAR:
```bash
./gradlew :shared:assembleDebug
```
- **Output Artifact**: `shared/build/outputs/aar/shared-debug.aar`

---

## 3. iOS Framework Artifacts

The `:shared` Kotlin Multiplatform module compiles native frameworks directly from macOS:

### 1. Build Apple Silicon Simulator Framework:
```bash
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```
- **Output**: `shared/build/bin/iosSimulatorArm64/debugFramework/shared.framework`

### 2. Build Physical Device Framework:
```bash
./gradlew :shared:linkReleaseFrameworkIosArm64
```
- **Output**: `shared/build/bin/iosArm64/releaseFramework/shared.framework`

### 3. Build All iOS Frameworks:
```bash
./gradlew :shared:assemble
```
- Produces `shared.framework` across all 3 configured Apple targets (`iosArm64`, `iosSimulatorArm64`, `iosX64`).
- Can be dragged and dropped directly into Xcode or wrapped in an `XCFramework` for CocoaPods / Swift Package Manager distribution.
