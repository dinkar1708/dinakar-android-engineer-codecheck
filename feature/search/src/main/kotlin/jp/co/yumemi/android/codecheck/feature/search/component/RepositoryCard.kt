package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.codecheck.feature.search.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppGreen
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramBlueText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramGreenBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramGreenText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramSlateBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramSlateText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate100
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate300
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate400
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate700
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate800
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate900
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.ui.util.formatForkCount
import jp.co.yumemi.android.codecheck.core.ui.util.formatStarCount
import jp.co.yumemi.android.codecheck.core.ui.util.getLanguageColor
import jp.co.yumemi.android.codecheck.core.ui.util.getMonogramInitials
import kotlin.math.abs

/**
 * Repository card component strictly adhering to the design specifications.
 * - Flat card surface with slate neutral border
 * - Monogram tile fallback / real avatar image loading
 * - Slate neutrals for structure
 * - Language dot and star count are the only colors carrying data
 */
@Composable
fun RepositoryCard(
    item: RepositoryItem,
    onClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val ownerLogin = item.owner.login.ifBlank {
        if (item.name.contains("/")) item.name.substringBefore("/") else "unknown"
    }
    val displayName = if (item.name.contains("/")) item.name.substringAfter("/") else item.name

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(item) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Monogram Avatar / Real Image loading
            MonogramAvatar(
                imageUrl = item.ownerIconUrl,
                ownerName = ownerLogin,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Owner line: outline user icon + login
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = Slate400
                    )
                    Text(
                        text = ownerLogin,
                        fontSize = 13.sp,
                        color = Slate500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                // Repository Name
                Text(
                    text = displayName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Description
                if (!item.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.description.orEmpty(),
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Data row: Language, Stars, Forks
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Language with data color dot
                    if (!item.language.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(getLanguageColor(item.language))
                            )
                            Text(
                                text = item.language.orEmpty(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Stars: filled Amber star + count in bold
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = AppAmber
                        )
                        Text(
                            text = formatStarCount(item.stargazersCount),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                    }

                    // Forks: pure neutral slate text
                    Text(
                        text = stringResource(R.string.search_forks_suffix, formatForkCount(item.forksCount)),
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }
        }
    }
}

/**
 * Avatar rendering real image if available, falling back to monogram tile with 7% tint on white.
 */
@Composable
fun MonogramAvatar(
    imageUrl: String,
    ownerName: String,
    modifier: Modifier = Modifier
) {
    val initials = remember(ownerName) { getMonogramInitials(ownerName) }
    val style = remember(ownerName) { getMonogramStyle(ownerName) }

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.search_avatar_content_description, ownerName),
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop,
        loading = {
            MonogramTile(initials = initials, style = style)
        },
        error = {
            MonogramTile(initials = initials, style = style)
        }
    )
}

/**
 * Monogram tile showing initials with 7% background tint and colored text.
 */
@Composable
fun MonogramTile(
    initials: String,
    style: MonogramStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(style.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )
    }
}

/**
 * Skeleton placeholder matching the card layout during loading states.
 */
@Composable
fun RepositoryCardSkeleton(
    modifier: Modifier = Modifier
) {
    val placeholderCircle: Color = Slate100
    val barPrimary: Color = Slate100
    val barSecondary: Color = Slate200

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Circular avatar placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(placeholderCircle)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Short bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(barPrimary)
                )
                // Middle bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.70f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(barSecondary)
                )
                // Long bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(barPrimary)
                )
            }
        }
    }
}

data class MonogramStyle(
    val backgroundColor: Color,
    val textColor: Color
)

/**
 * Deterministically assigns one of the 3 derived monogram tints (Blue, Green, or Slate).
 */
internal fun getMonogramStyle(key: String): MonogramStyle {
    val hash = abs(key.hashCode())
    return when (hash % 3) {
        0 -> MonogramStyle(
            backgroundColor = MonogramBlueBg,
            textColor = MonogramBlueText
        )
        1 -> MonogramStyle(
            backgroundColor = MonogramGreenBg,
            textColor = MonogramGreenText
        )
        else -> MonogramStyle(
            backgroundColor = MonogramSlateBg,
            textColor = MonogramSlateText
        )
    }
}
