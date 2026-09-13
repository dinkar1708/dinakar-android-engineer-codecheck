package jp.co.yumemi.android.codecheck.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.codecheck.core.designsystem.R
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.SelectedBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500

/**
 * Top-level application tabs for bottom navigation bar.
 */
enum class MainTab {
    SEARCH,
    STARRED,
    SETTINGS
}

/**
 * Application bottom navigation bar following the design system palette.
 * Highlights the active tab with a pill indicator ([SelectedBlueBg]) and primary blue text/icon.
 */
@Composable
fun AppBottomBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = AppWhite,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = Slate200
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppBottomBarItem(
                    label = stringResource(R.string.tab_search),
                    icon = Icons.Default.Search,
                    isSelected = currentTab == MainTab.SEARCH,
                    onClick = { onTabSelected(MainTab.SEARCH) },
                    modifier = Modifier.weight(1f)
                )
                AppBottomBarItem(
                    label = stringResource(R.string.tab_starred),
                    icon = Icons.Default.Star,
                    isSelected = currentTab == MainTab.STARRED,
                    onClick = { onTabSelected(MainTab.STARRED) },
                    modifier = Modifier.weight(1f)
                )
                AppBottomBarItem(
                    label = stringResource(R.string.tab_settings),
                    icon = Icons.Default.Settings,
                    isSelected = currentTab == MainTab.SETTINGS,
                    onClick = { onTabSelected(MainTab.SETTINGS) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AppBottomBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .semantics { selected = isSelected }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSelected) SelectedBlueBg else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = if (isSelected) AppBlue else Slate500
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) AppBlue else Slate500
        )
    }
}

@Preview(name = "AppBottomBar - Search Selected", showBackground = true)
@Composable
private fun AppBottomBarSearchPreview() {
    CodeCheckTheme {
        AppBottomBar(
            currentTab = MainTab.SEARCH,
            onTabSelected = {}
        )
    }
}

@Preview(name = "AppBottomBar - Starred Selected", showBackground = true)
@Composable
private fun AppBottomBarStarredPreview() {
    CodeCheckTheme {
        AppBottomBar(
            currentTab = MainTab.STARRED,
            onTabSelected = {}
        )
    }
}

@Preview(name = "AppBottomBar - Settings Selected", showBackground = true)
@Composable
private fun AppBottomBarSettingsPreview() {
    CodeCheckTheme {
        AppBottomBar(
            currentTab = MainTab.SETTINGS,
            onTabSelected = {}
        )
    }
}

