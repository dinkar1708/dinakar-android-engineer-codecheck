plugins {
    alias(libs.plugins.codecheck.android.feature)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.feature.starred"
}

dependencies {
    implementation(libs.coil.compose)
}
