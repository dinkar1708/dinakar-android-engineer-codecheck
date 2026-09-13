package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.feature.search.R

/**
 * Recent searches grouped card section displaying up to 7 recent queries (MRU order).
 * Clicking an item executes a 1-tap re-search; clicking the remove icon deletes the query.
 * Supports light and dark theme palettes.
 */
@Composable
fun RecentSearchesSection(
    history: List<String>,
    onQueryClick: (String) -> Unit,
    onRemoveQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) return

    val sectionTitleColor = MaterialTheme.colorScheme.onSurfaceVariant
    val containerBg = MaterialTheme.colorScheme.surface
    val containerBorder = MaterialTheme.colorScheme.outline
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.search_recent_title),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.05.em,
            color = sectionTitleColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerBg, shape = RoundedCornerShape(8.dp))
                .border(1.dp, containerBorder, shape = RoundedCornerShape(8.dp))
        ) {
            history.forEachIndexed { index, recentQuery ->
                if (index > 0) {
                    HorizontalDivider(thickness = 1.dp, color = dividerColor)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onQueryClick(recentQuery) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = iconTint
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = recentQuery,
                        fontSize = 14.sp,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onRemoveQuery(recentQuery) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.search_remove_recent, recentQuery),
                            modifier = Modifier.size(16.dp),
                            tint = iconTint
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Recent Searches - Light", showBackground = true)
@Composable
private fun RecentSearchesSectionLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        RecentSearchesSection(
            history = listOf("kotlin", "compose", "retrofit", "ktor client"),
            onQueryClick = {},
            onRemoveQuery = {}
        )
    }
}

@Preview(name = "Recent Searches - Dark", showBackground = true)
@Composable
private fun RecentSearchesSectionDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        RecentSearchesSection(
            history = listOf("kotlin", "compose", "retrofit", "ktor client"),
            onQueryClick = {},
            onRemoveQuery = {}
        )
    }
}

