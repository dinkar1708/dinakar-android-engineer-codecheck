plugins {
    alias(libs.plugins.codecheck.android.feature)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.feature.detail"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.compose.material.icons.extended)
}
