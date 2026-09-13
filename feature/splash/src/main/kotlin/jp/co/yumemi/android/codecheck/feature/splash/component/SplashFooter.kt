package jp.co.yumemi.android.codecheck.feature.splash.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.splash.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.DarkActionBorder

/**
 * Bottom brand footer featuring the indeterminate loading track and API label.
 *
 * @param progressOffset Animated offset for the sliding 40dp bar within the 120dp track.
 * @param modifier Optional layout modifier.
 */
@Composable
fun SplashFooter(
    progressOffset: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = if (MaterialTheme.colorScheme.surface != AppWhite) Color(0x14FFFFFF) else Color(0x1FFFFFFF),
    accentColor: Color = if (MaterialTheme.colorScheme.surface != AppWhite) DarkActionBorder else AppBlue
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Indeterminate Progress Bar Track (120dp width, 3dp height)
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = progressOffset.dp)
                    .width(40.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentColor)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.splash_api_label),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            color = Color(0xFF8C99AB)
        )
    }
}

@Preview(name = "Splash Footer", showBackground = true, backgroundColor = 0xFF2D3545)
@Composable
private fun SplashFooterPreview() {
    CodeCheckTheme {
        SplashFooter(progressOffset = 30f)
    }
}
