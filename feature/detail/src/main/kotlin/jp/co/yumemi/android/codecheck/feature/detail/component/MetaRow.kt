package jp.co.yumemi.android.codecheck.feature.detail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme


/**
 * MetaRow card component matching MetaRow.dc.html:
 * - 8dp rounded card container with 1dp Slate200 / DarkBorder border
 * - 13sp Slate500 / TextSecondaryDark label on the left
 * - 13sp Slate800 / #E2E8F0 SemiBold value on the right (with optional monospace font for default branch)
 */
@Composable
fun MetaRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default
) {
    val cardBg = MaterialTheme.colorScheme.surface
    val cardBorder = MaterialTheme.colorScheme.outline
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val valueColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = cardBg,
        border = BorderStroke(1.dp, cardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = labelColor
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                color = valueColor
            )
        }
    }
}

@Preview(name = "MetaRow - Light", showBackground = true)
@Composable
private fun MetaRowLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        MetaRow(
            label = "Default branch",
            value = "main",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "MetaRow - Dark", showBackground = true)
@Composable
private fun MetaRowDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        MetaRow(
            label = "License",
            value = "Apache-2.0",
            modifier = Modifier.padding(16.dp)
        )
    }
}

