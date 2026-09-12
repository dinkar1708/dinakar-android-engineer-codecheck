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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.domain.model.SearchSort

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width

/**
 * Sort tabs sitting directly under the search bar adhering to mockup 03b:
 * - 3 orderings: "Best match", "Most stars", "Most forks"
 * - Active tab: 2dp solid #3b50df underline indicator with 14sp SemiBold text
 * - Inactive tab: Slate500 text with transparent underline
 */
@Composable
fun SortTabs(
    selectedSort: SearchSort,
    onSortSelected: (SearchSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        SearchSort.BEST_MATCH to "Best match",
        SearchSort.STARS to "Most stars",
        SearchSort.FORKS to "Most forks"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AppWhite)
    ) {
        // Bottom subtle border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Slate200)
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
                        color = if (isSelected) AppBlue else Slate500,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        modifier = Modifier.padding(top = 14.dp, bottom = 12.dp)
                    )

                    // Active 2dp line indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (isSelected) AppBlue else Color.Transparent)
                    )
                }
            }
        }
    }
}
