plugins {
    alias(libs.plugins.codecheck.kotlin.multiplatform)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.core.domain"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
