package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.search.R

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width

/**
 * Sort selector tabs matching mockup 03b:
 * - Best match, Most stars, Most forks
 * - Active tab: AppBlue text with 2dp line indicator (DarkActionBorder in dark mode)
 * - Inactive tab: Slate500 text with transparent underline (TextSecondaryDark in dark mode)
 */
@Composable
fun SortTabs(
    selectedSort: SearchSort,
    onSortSelected: (SearchSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val barBg = MaterialTheme.colorScheme.surface
    val barBorder = MaterialTheme.colorScheme.outline
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant

    val tabs = listOf(
        SearchSort.BEST_MATCH to stringResource(R.string.search_sort_best_match),
        SearchSort.STARS to stringResource(R.string.search_sort_most_stars),
        SearchSort.FORKS to stringResource(R.string.search_sort_most_forks)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(barBg)
    ) {
        // Bottom subtle border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(barBorder)
                .align(Alignment.BottomCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            tabs.forEach { (sort, title) ->
                val isSelected = selectedSort == sort
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Tab,
                            onClick = { onSortSelected(sort) }
                        )
                        .semantics { selected = isSelected },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) activeColor else inactiveColor,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        modifier = Modifier.padding(top = 14.dp, bottom = 12.dp)
                    )

                    // Active 2dp line indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (isSelected) activeColor else Color.Transparent)
                    )
                }
            }
        }
    }
}

@Preview(name = "SortTabs - Best Match Light", showBackground = true)
@Composable
private fun SortTabsBestMatchLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        SortTabs(
            selectedSort = SearchSort.BEST_MATCH,
            onSortSelected = {}
        )
    }
}

@Preview(name = "SortTabs - Most Stars Dark", showBackground = true)
@Composable
private fun SortTabsMostStarsDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        SortTabs(
            selectedSort = SearchSort.STARS,
            onSortSelected = {}
        )
    }
}

