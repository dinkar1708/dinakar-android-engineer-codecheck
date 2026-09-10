plugins {
    alias(libs.plugins.codecheck.kotlin.multiplatform)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.core.domain"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Pure Kotlin - no dependencies
        }
    }
}
