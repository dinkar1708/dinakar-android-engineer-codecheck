package jp.co.yumemi.android.codecheck.core.ui.util

import androidx.compose.ui.graphics.Color
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppGreen
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import kotlin.math.abs

/**
 * Maps programming languages to one of the 4 data colors (Blue, Green, Amber, Slate)
 * defined in the design system palette.
 */
fun getLanguageColor(language: String?): Color {
    val actionBlue = AppBlue
    return when (language?.lowercase()) {
        "rust", "go", "kotlin", "typescript", "ruby" -> actionBlue
        "shell", "python", "vue", "html", "php" -> AppGreen
        "c++", "java", "javascript", "swift" -> AppAmber
        "c", "c#", "css" -> Slate500
        else -> {
            val hash = abs((language ?: "").hashCode())
            when (hash % 4) {
                0 -> actionBlue
                1 -> AppGreen
                2 -> AppAmber
                else -> Slate500
            }
        }
    }
}
