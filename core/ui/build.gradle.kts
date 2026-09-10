plugins {
    alias(libs.plugins.codecheck.android.library)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.core.ui"
}

dependencies {
    // Core modules
    implementation(project(":core:designsystem"))

    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
