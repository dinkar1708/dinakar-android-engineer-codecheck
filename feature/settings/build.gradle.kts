plugins {
    alias(libs.plugins.codecheck.android.feature)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.feature.settings"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
}
