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
./gradlew :shared-core:assembleDebug
```
- **Output Artifact**: `shared-core/build/outputs/aar/shared-core-debug.aar`

---

## 3. iOS Framework Artifacts

The `:shared-core` Kotlin Multiplatform module compiles native frameworks directly from macOS:

### 1. Build Apple Silicon Simulator Framework:
```bash
./gradlew :shared-core:linkDebugFrameworkIosSimulatorArm64
```
- **Output**: `shared-core/build/bin/iosSimulatorArm64/debugFramework/shared_core.framework`

### 2. Build Physical Device Framework:
```bash
./gradlew :shared-core:linkReleaseFrameworkIosArm64
```
- **Output**: `shared-core/build/bin/iosArm64/releaseFramework/shared_core.framework`

### 3. Build All iOS Frameworks:
```bash
./gradlew :shared-core:assemble
```
- Produces `shared_core.framework` across configured Apple targets (`iosSimulatorArm64`, `iosArm64`, `iosX64`).
- Can be dragged and dropped directly into Xcode or wrapped in an `XCFramework` for CocoaPods / Swift Package Manager distribution.
