package jp.co.yumemi.android.codecheck.feature.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.domain.model.Owner
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
@OptIn(ExperimentalLayoutApi::class)
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ownerLogin,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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

                // Data row: Language, Stars, Forks (wrapping gracefully for long metrics)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
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
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = formatStarCount(item.stargazersCount),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }

                    // Forks: pure neutral slate text
                    Text(
                        text = stringResource(R.string.search_forks_suffix, formatForkCount(item.forksCount)),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val style = remember(ownerName, primaryContainer, secondaryContainer, surfaceVariant) {
        val hash = abs(ownerName.hashCode())
        when (hash % 3) {
            0 -> MonogramStyle(primaryContainer, onPrimaryContainer)
            1 -> MonogramStyle(secondaryContainer, onSecondaryContainer)
            else -> MonogramStyle(surfaceVariant, onSurfaceVariant)
        }
    }

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
 * Monogram tile showing initials with background tint and colored text.
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
    val placeholderCircle = MaterialTheme.colorScheme.surfaceVariant
    val barPrimary = MaterialTheme.colorScheme.surfaceVariant
    val barSecondary = MaterialTheme.colorScheme.outline

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

@Preview(name = "RepositoryCard - Light", showBackground = true)
@Composable
private fun RepositoryCardLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        RepositoryCard(
            item = RepositoryItem(
                name = "jetbrains/kotlin",
                owner = Owner(login = "jetbrains", avatarUrl = "https://avatars.githubusercontent.com/u/262714"),
                language = "Kotlin",
                stargazersCount = 47200,
                watchersCount = 47200,
                forksCount = 5700,
                openIssuesCount = 180,
                description = "The Kotlin Programming Language. Official repository for Kotlin."
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "RepositoryCard - Dark", showBackground = true)
@Composable
private fun RepositoryCardDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        RepositoryCard(
            item = RepositoryItem(
                name = "jetbrains/kotlin",
                owner = Owner(login = "jetbrains", avatarUrl = "https://avatars.githubusercontent.com/u/262714"),
                language = "Kotlin",
                stargazersCount = 47200,
                watchersCount = 47200,
                forksCount = 5700,
                openIssuesCount = 180,
                description = "The Kotlin Programming Language. Official repository for Kotlin."
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "RepositoryCardSkeleton - Light", showBackground = true)
@Composable
private fun RepositoryCardSkeletonLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        RepositoryCardSkeleton(modifier = Modifier.padding(16.dp))
    }
}

@Preview(name = "RepositoryCardSkeleton - Dark", showBackground = true)
@Composable
private fun RepositoryCardSkeletonDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        RepositoryCardSkeleton(modifier = Modifier.padding(16.dp))
    }
}

