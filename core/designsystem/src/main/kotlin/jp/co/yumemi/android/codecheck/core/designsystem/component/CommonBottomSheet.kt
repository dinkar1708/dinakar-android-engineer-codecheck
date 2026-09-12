package jp.co.yumemi.android.codecheck.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.ScrimOverlay
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate300
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate900

/**
 * Common bottom sheet component with consistent styling across the app.
 * - Rounded top corners 16dp, drag handle 36x4dp
 * - Optional top header with title and action button
 * - Customizable content area
 * - Follows design system specifications from mockup 03c
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    title: String? = null,
    actionLabel: String? = null,
    actionEnabled: Boolean = true,
    onActionClick: (() -> Unit)? = null,
    showHeaderDivider: Boolean = true,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = AppWhite,
        contentColor = Slate900,
        tonalElevation = 0.dp,
        scrimColor = ScrimOverlay,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 14.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .background(Slate300, RoundedCornerShape(2.dp))
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Optional Header: Title & Action
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    if (actionLabel != null && onActionClick != null) {
                        Text(
                            text = actionLabel,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (actionEnabled) AppBlue else Slate300,
                            modifier = Modifier
                                .then(
                                    if (actionEnabled) {
                                        Modifier.clickable(role = Role.Button) { onActionClick() }
                                    } else {
                                        Modifier
                                    }
                                )
                                .padding(vertical = 4.dp)
                        )
                    }
                }

                if (showHeaderDivider) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = Slate200)
                }
            }

            // Content Area
            content()
        }
    }
}
