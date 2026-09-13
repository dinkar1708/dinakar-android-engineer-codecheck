package jp.co.yumemi.android.codecheck.feature.starred

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppNavy
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramBlueText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramGreenBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramGreenText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramSlateBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramSlateText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate400
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate50
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate800
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.ui.util.formatForkCount
import jp.co.yumemi.android.codecheck.core.ui.util.formatStarCount
import jp.co.yumemi.android.codecheck.core.ui.util.getLanguageColor
import jp.co.yumemi.android.codecheck.core.ui.util.getMonogramInitials
import kotlin.math.abs

/**
 * Stateful entry point for the offline Starred repositories screen.
 */
@Composable
fun StarredScreen(
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StarredViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    StarredContent(
        uiState = uiState,
        onRepositoryClick = onRepositoryClick,
        onUnstar = viewModel::unstar,
        onClearAll = viewModel::clearAllStars,
        modifier = modifier
    )
}

/**
 * Stateless content of the Starred repositories screen.
 */
@Composable
fun StarredContent(
    uiState: StarredUiState,
    onRepositoryClick: (RepositoryItem) -> Unit,
    onUnstar: (RepositoryItem) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearConfirmation by remember { mutableStateOf(false) }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = {
                Text(
                    text = stringResource(R.string.starred_clear_confirm_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = stringResource(R.string.starred_clear_confirm_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearConfirmation = false
                        onClearAll()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.starred_clear),
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text(text = stringResource(R.string.starred_cancel))
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Slate50
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Dark Navy Anchor Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppNavy)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.starred_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppWhite
                    )

                    if (uiState is StarredUiState.Success && uiState.repositories.isNotEmpty()) {
                        TextButton(
                            onClick = { showClearConfirmation = true },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.starred_clear_all),
                                color = AppWhite.copy(alpha = 0.85f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Body
            when (uiState) {
                is StarredUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Neutral loading state
                    }
                }

                is StarredUiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.StarBorder,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Slate400
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.starred_empty_title),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate800,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.starred_empty_subtitle),
                                fontSize = 14.sp,
                                color = Slate500,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                is StarredUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.starred_count, uiState.repositories.size),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate500,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        items(
                            items = uiState.repositories,
                            key = { it.id.takeIf { id -> id != 0L } ?: it.name.hashCode().toLong() }
                        ) { repository ->
                            StarredCard(
                                item = repository,
                                onClick = { onRepositoryClick(repository) },
                                onUnstarClick = { onUnstar(repository) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StarredCard(
    item: RepositoryItem,
    onClick: () -> Unit,
    onUnstarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ownerLogin = item.owner.login.ifBlank {
        if (item.name.contains("/")) item.name.substringBefore("/") else "unknown"
    }
    val displayName = if (item.name.contains("/")) item.name.substringAfter("/") else item.name

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Monogram Avatar
            StarredAvatar(
                imageUrl = item.ownerIconUrl,
                ownerName = ownerLogin,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Owner login
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

                Spacer(modifier = Modifier.height(2.dp))

                // Repo name
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
                    Spacer(modifier = Modifier.height(4.dp))
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

                // Data row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
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

                    Text(
                        text = stringResource(R.string.starred_forks_suffix, formatForkCount(item.forksCount)),
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }

            // Unstar Button
            IconButton(
                onClick = onUnstarClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.starred_remove_content_description, displayName),
                    tint = AppAmber,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun StarredAvatar(
    imageUrl: String,
    ownerName: String,
    modifier: Modifier = Modifier
) {
    val initials = remember(ownerName) { getMonogramInitials(ownerName) }
    val hash = remember(ownerName) { abs(ownerName.hashCode()) }
    val (bgColor, textColor) = when (hash % 3) {
        0 -> Pair(MonogramBlueBg, MonogramBlueText)
        1 -> Pair(MonogramGreenBg, MonogramGreenText)
        else -> Pair(MonogramSlateBg, MonogramSlateText)
    }

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.starred_avatar_content_description, ownerName),
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop,
        loading = {
            MonogramBox(initials = initials, bgColor = bgColor, textColor = textColor)
        },
        error = {
            MonogramBox(initials = initials, bgColor = bgColor, textColor = textColor)
        }
    )
}

@Composable
private fun MonogramBox(
    initials: String,
    bgColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
