plugins {
    alias(libs.plugins.codecheck.android.feature)
}

android {
    namespace = "jp.co.yumemi.android.codecheck.feature.bookmarks"
}

dependencies {
    implementation(libs.coil.compose)
}
