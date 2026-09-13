package jp.co.yumemi.android.codecheck.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme

/**
 * Modern chip component following Material 3 design principles.
 * Used for tags, labels, and badges throughout the app.
 */

/**
 * Primary chip with brand color styling.
 * Ideal for language tags, categories, and primary labels.
 */
@Composable
fun PrimaryChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 1.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * Secondary chip with subtle styling.
 * Ideal for secondary information and metadata.
 */
@Composable
fun SecondaryChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 1.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

/**
 * Tertiary chip with accent styling.
 * Ideal for stats, counts, and highlighted information.
 */
@Composable
fun TertiaryChip(
    text: String,
    modifier: Modifier = Modifier,
    alpha: Float = 0.5f
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = alpha)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

/**
 * Outline chip with border styling.
 * Ideal for optional filters, toggles, and non-primary actions.
 */
@Composable
fun OutlineChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Custom chip with configurable colors.
 * For special cases requiring custom styling.
 */
@Composable
fun CustomChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = textColor
        )
    }
}

@Preview(name = "Chips - Light", showBackground = true)
@Composable
private fun ChipsLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            PrimaryChip(text = "Kotlin")
            SecondaryChip(text = "Android")
            OutlineChip(text = "v1.0.0")
            TertiaryChip(text = "Active")
        }
    }
}

@Preview(name = "Chips - Dark", showBackground = true)
@Composable
private fun ChipsDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            PrimaryChip(text = "Kotlin")
            SecondaryChip(text = "Android")
            OutlineChip(text = "v1.0.0")
            TertiaryChip(text = "Active")
        }
    }
}

