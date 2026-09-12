package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.search.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.component.CommonBottomSheet
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.SelectedBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate800
import jp.co.yumemi.android.codecheck.core.domain.model.SearchFilter
import java.util.Locale

/**
 * Filter sheet composable matching mockup 03c:
 * - Rounded top corners 16dp, drag handle 36x4dp
 * - Top header: "Filters" title and "Reset" button
 * - Language chips: Rust, Kotlin, Python, Go, TypeScript, C++
 * - Minimum stars segment: Any, 100+, 500+, 1K+
 * - Last updated segment: Any time, This year, This month
 * - Bottom primary action: "Show X repositories"
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    initialFilter: SearchFilter,
    onApplyFilter: (SearchFilter) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var draftFilter by remember(initialFilter) { mutableStateOf(initialFilter) }

    val languages = listOf("Rust", "Kotlin", "Python", "Go", "TypeScript", "C++")
    val starOptions = listOf(
        null to stringResource(R.string.search_filter_stars_any),
        100 to "100+",
        500 to "500+",
        1000 to "1K+"
    )
    val periodOptions = listOf(
        "any" to stringResource(R.string.search_filter_period_any_time),
        "year" to stringResource(R.string.search_filter_period_this_year),
        "month" to stringResource(R.string.search_filter_period_this_month)
    )

    CommonBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        title = stringResource(R.string.search_filter_title),
        actionLabel = stringResource(R.string.search_filter_reset),
        onActionClick = { draftFilter = SearchFilter() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Body Sections
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Section 1: Language
                Column {
                    Text(
                        text = stringResource(R.string.search_filter_section_language),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.05.em,
                        color = Slate500,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        languages.forEach { lang ->
                            val isSelected = draftFilter.language?.equals(lang, ignoreCase = true) == true
                            FilterPill(
                                label = lang,
                                isSelected = isSelected,
                                onClick = {
                                    draftFilter = draftFilter.copy(
                                        language = if (isSelected) null else lang
                                    )
                                }
                            )
                        }
                    }
                }

                // Section 2: Minimum stars
                Column {
                    Text(
                        text = stringResource(R.string.search_filter_section_min_stars),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.05.em,
                        color = Slate500,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        starOptions.forEach { (stars, label) ->
                            val isSelected = draftFilter.minStars == stars
                            SegmentButton(
                                label = label,
                                isSelected = isSelected,
                                onClick = {
                                    draftFilter = draftFilter.copy(minStars = stars)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Section 3: Last updated
                Column {
                    Text(
                        text = stringResource(R.string.search_filter_section_last_updated),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.05.em,
                        color = Slate500,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        periodOptions.forEach { (period, label) ->
                            val isSelected = draftFilter.updatedPeriod == period
                            SegmentButton(
                                label = label,
                                isSelected = isSelected,
                                onClick = {
                                    val updatedAfter = when (period) {
                                        "year" -> getStartOfYearDateString()
                                        "month" -> getStartOfMonthDateString()
                                        else -> null
                                    }
                                    draftFilter = draftFilter.copy(
                                        updatedPeriod = period,
                                        updatedAfter = updatedAfter
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Bottom Action: Apply Button
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(thickness = 1.dp, color = Slate200)
                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    onClick = {
                        onApplyFilter(draftFilter)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(4.dp),
                    color = AppBlue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.search_filter_show_results),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) SelectedBlueBg else AppWhite,
        border = BorderStroke(1.dp, if (isSelected) AppBlue else Slate200),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) AppBlue else Slate800
            )
        }
    }
}

@Composable
private fun SegmentButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        color = if (isSelected) SelectedBlueBg else AppWhite,
        border = BorderStroke(1.dp, if (isSelected) AppBlue else Slate200),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) AppBlue else Slate800
            )
        }
    }
}

internal fun getStartOfYearDateString(): String {
    val cal = java.util.Calendar.getInstance()
    val year = cal.get(java.util.Calendar.YEAR)
    return String.format(Locale.US, "%04d-01-01", year)
}

internal fun getStartOfMonthDateString(): String {
    val cal = java.util.Calendar.getInstance()
    val year = cal.get(java.util.Calendar.YEAR)
    val month = cal.get(java.util.Calendar.MONTH) + 1
    return String.format(Locale.US, "%04d-%02d-01", year, month)
}
