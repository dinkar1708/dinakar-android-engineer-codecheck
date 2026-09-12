package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.search.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.SelectedBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate300
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate800
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter

/**
 * Filter bar displaying the Filters launcher button and active filter chips matching mockup 03b:
 * - Filters button: SlidersHorizontal icon + "Filters"
 * - Removable chips: #3b50df17 background, 1dp #3b50df border, and 'x' icon
 */
@Composable
fun FilterBar(
    filter: SearchFilter,
    onOpenFilterSheet: () -> Unit,
    onRemoveLanguage: () -> Unit,
    onRemoveMinStars: () -> Unit,
    onRemoveUpdatedPeriod: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Filters trigger button
            Surface(
                modifier = Modifier.clickable(
                    role = Role.Button,
                    onClick = onOpenFilterSheet
                ),
                shape = RoundedCornerShape(20.dp),
                color = AppWhite,
                border = BorderStroke(1.dp, if (filter.isActive) AppBlue else Slate300)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    SlidersHorizontalIcon(
                        modifier = Modifier.size(14.dp),
                        tint = if (filter.isActive) AppBlue else Slate800
                    )
                    Text(
                        text = stringResource(R.string.search_filter_button),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (filter.isActive) AppBlue else Slate800
                    )
                }
            }

            // Language active filter chip
            if (!filter.language.isNullOrBlank()) {
                ActiveFilterChip(
                    label = filter.language ?: "",
                    onRemove = onRemoveLanguage
                )
            }

            // Min stars active filter chip
            val minStars = filter.minStars
            if (minStars != null && minStars > 0) {
                val label = if (minStars >= 1000) {
                    stringResource(R.string.search_filter_stars_k_suffix, minStars / 1000)
                } else {
                    stringResource(R.string.search_filter_stars_suffix, minStars)
                }
                ActiveFilterChip(
                    label = label,
                    onRemove = onRemoveMinStars
                )
            }

            // Updated period active filter chip
            if (filter.updatedPeriod != "any") {
                val label = if (filter.updatedPeriod == "year") {
                    stringResource(R.string.search_filter_period_this_year)
                } else {
                    stringResource(R.string.search_filter_period_this_month)
                }
                ActiveFilterChip(
                    label = label,
                    onRemove = onRemoveUpdatedPeriod
                )
            }
        }
    }
}

@Composable
private fun ActiveFilterChip(
    label: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SelectedBlueBg,
        border = BorderStroke(1.dp, AppBlue),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = AppBlue
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.search_filter_remove_content_description, label),
                tint = AppBlue,
                modifier = Modifier
                    .size(14.dp)
                    .clickable(onClick = onRemove)
            )
        }
    }
}

/**
 * Clean vector rendering of Lucide's sliders-horizontal icon.
 */
@Composable
fun SlidersHorizontalIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()

        // Top line & handle
        val y1 = h * 0.25f
        drawLine(tint, Offset(0f, y1), Offset(w, y1), strokeWidth = stroke, cap = StrokeCap.Round)
        drawCircle(AppWhite, radius = 2.5.dp.toPx(), center = Offset(w * 0.35f, y1))
        drawCircle(tint, radius = 2.5.dp.toPx(), center = Offset(w * 0.35f, y1), style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))

        // Middle line & handle
        val y2 = h * 0.5f
        drawLine(tint, Offset(0f, y2), Offset(w, y2), strokeWidth = stroke, cap = StrokeCap.Round)
        drawCircle(AppWhite, radius = 2.5.dp.toPx(), center = Offset(w * 0.70f, y2))
        drawCircle(tint, radius = 2.5.dp.toPx(), center = Offset(w * 0.70f, y2), style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))

        // Bottom line & handle
        val y3 = h * 0.75f
        drawLine(tint, Offset(0f, y3), Offset(w, y3), strokeWidth = stroke, cap = StrokeCap.Round)
        drawCircle(AppWhite, radius = 2.5.dp.toPx(), center = Offset(w * 0.45f, y3))
        drawCircle(tint, radius = 2.5.dp.toPx(), center = Offset(w * 0.45f, y3), style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
    }
}
