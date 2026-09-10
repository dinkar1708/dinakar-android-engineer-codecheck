plugins {
    alias(libs.plugins.codecheck.kotlin.multiplatform)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.shared"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Umbrella module - exports all core modules as single framework for iOS
            api(project(":core:domain"))
            api(project(":core:network"))
            api(project(":core:data"))
        }
    }
}
