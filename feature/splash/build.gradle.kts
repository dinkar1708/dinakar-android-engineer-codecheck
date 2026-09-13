plugins {
    alias(libs.plugins.codecheck.android.feature)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.feature.splash"
}

dependencies {
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
