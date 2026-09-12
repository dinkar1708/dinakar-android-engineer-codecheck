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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate100
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate400
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate900
import jp.co.yumemi.android.codecheck.feature.search.R

/**
 * Recent searches grouped card section displaying up to 7 recent queries (MRU order).
 * Clicking an item executes a 1-tap re-search; clicking the remove icon deletes the query.
 */
@Composable
fun RecentSearchesSection(
    history: List<String>,
    onQueryClick: (String) -> Unit,
    onRemoveQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) return

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
            color = Slate500,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppWhite, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Slate200, shape = RoundedCornerShape(8.dp))
        ) {
            history.forEachIndexed { index, recentQuery ->
                if (index > 0) {
                    HorizontalDivider(thickness = 1.dp, color = Slate100)
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
                        tint = Slate400
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = recentQuery,
                        fontSize = 14.sp,
                        color = Slate900,
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
                            tint = Slate400
                        )
                    }
                }
            }
        }
    }
}
