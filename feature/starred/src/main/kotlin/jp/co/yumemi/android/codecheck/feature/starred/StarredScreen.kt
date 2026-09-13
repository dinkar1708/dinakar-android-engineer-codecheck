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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppNavy
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramBlueBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramBlueText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramGreenBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramGreenText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramSlateBg
import jp.co.yumemi.android.codecheck.core.designsystem.theme.MonogramSlateText
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate300
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate400
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate50
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate600
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
 * Stateful entry point for the offline Starred repositories screen.
 */
@Composable
fun StarredScreen(
    onRepositoryClick: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToSearch: () -> Unit = {},
    viewModel: StarredViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    StarredContent(
        uiState = uiState,
        onRepositoryClick = onRepositoryClick,
        onUnstar = viewModel::unstar,
        onClearAll = viewModel::clearAllStars,
        onNavigateToSearch = onNavigateToSearch,
        modifier = modifier
    )
}

/**
 * Stateless content of the Starred repositories screen matching the design specification.
 */
@Composable
fun StarredContent(
    uiState: StarredUiState,
    onRepositoryClick: (RepositoryItem) -> Unit,
    onUnstar: (RepositoryItem) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToSearch: () -> Unit = {}
) {
    var showClearConfirmation by remember { mutableStateOf(false) }

    val isDarkTheme = MaterialTheme.colorScheme.surface != AppWhite

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF451A1A) else Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = if (isDarkTheme) Color(0xFFF87171) else Color(0xFFDC2626),
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.starred_clear_confirm_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.starred_clear_confirm_message),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirmation = false
                        onClearAll()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = AppWhite
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.starred_clear),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClearConfirmation = false },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(R.string.starred_cancel),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
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
                                color = Color(0xFFCBD5E1),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Body Area
            when (uiState) {
                is StarredUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Clean neutral loading state
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
                            // Circular amber badge matching mockup
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp),
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = stringResource(R.string.starred_empty_title),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stringResource(R.string.starred_empty_subtitle),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onNavigateToSearch,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppBlue,
                                    contentColor = AppWhite
                                ),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 11.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.starred_search_repositories),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                is StarredUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            val countText = if (uiState.repositories.size == 1) {
                                stringResource(R.string.starred_count_singular)
                            } else {
                                stringResource(R.string.starred_count_plural, uiState.repositories.size)
                            }
                            Text(
                                text = countText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.05.em,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
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
    val isDarkTheme = MaterialTheme.colorScheme.surface != AppWhite
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        color = if (isDarkTheme) Color(0xFFA9B4C4) else Color(0xFF475569),
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
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = formatStarCount(item.stargazersCount),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFE2E8F0) else Slate800
                        )
                    }

                    Text(
                        text = stringResource(R.string.starred_forks_suffix, formatForkCount(item.forksCount)),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Filled gold star action button (tapping unstars)
            IconButton(
                onClick = onUnstarClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.starred_remove_content_description, displayName),
                    tint = MaterialTheme.colorScheme.tertiary,
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
    val isDarkTheme = MaterialTheme.colorScheme.surface != AppWhite
    val initials = remember(ownerName) { getMonogramInitials(ownerName) }
    val hash = remember(ownerName) { abs(ownerName.hashCode()) }
    val (bgColor, textColor) = if (isDarkTheme) {
        when (hash % 3) {
            0 -> Pair(Color(0xFF2A3350), Color(0xFF8F9DF5))
            1 -> Pair(Color(0xFF064E3B), Color(0xFFA7F3D0))
            else -> Pair(Color(0xFF2B3344), Color(0xFFA9B4C4))
        }
    } else {
        when (hash % 3) {
            0 -> Pair(MonogramBlueBg, MonogramBlueText)
            1 -> Pair(MonogramGreenBg, MonogramGreenText)
            else -> Pair(MonogramSlateBg, MonogramSlateText)
        }
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

@Preview(name = "Starred - Light", showBackground = true)
@Composable
private fun StarredScreenLightPreview() {
    CodeCheckTheme(darkTheme = false) {
        StarredContent(
            uiState = StarredUiState.Success(
                listOf(
                    RepositoryItem(
                        name = "hussien89aa/KotlinUdemy",
                        description = "Learn how to make online games, and apps for Android O, like Pokémon, twitter…",
                        language = "Kotlin",
                        stargazersCount = 2000,
                        forksCount = 5000,
                        owner = jp.co.yumemi.android.codecheck.core.domain.model.Owner(login = "hussien89aa")
                    ),
                    RepositoryItem(
                        name = "JetBrains/kotlin",
                        description = "The Kotlin Programming Language.",
                        language = "Kotlin",
                        stargazersCount = 53400,
                        forksCount = 6400,
                        owner = jp.co.yumemi.android.codecheck.core.domain.model.Owner(login = "JetBrains")
                    )
                )
            ),
            onRepositoryClick = {},
            onUnstar = {},
            onClearAll = {}
        )
    }
}

@Preview(name = "Starred - Dark", showBackground = true)
@Composable
private fun StarredScreenDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        StarredContent(
            uiState = StarredUiState.Success(
                listOf(
                    RepositoryItem(
                        name = "hussien89aa/KotlinUdemy",
                        description = "Learn how to make online games, and apps for Android O, like Pokémon, twitter…",
                        language = "Kotlin",
                        stargazersCount = 2000,
                        forksCount = 5000,
                        owner = jp.co.yumemi.android.codecheck.core.domain.model.Owner(login = "hussien89aa")
                    ),
                    RepositoryItem(
                        name = "JetBrains/kotlin",
                        description = "The Kotlin Programming Language.",
                        language = "Kotlin",
                        stargazersCount = 53400,
                        forksCount = 6400,
                        owner = jp.co.yumemi.android.codecheck.core.domain.model.Owner(login = "JetBrains")
                    )
                )
            ),
            onRepositoryClick = {},
            onUnstar = {},
            onClearAll = {}
        )
    }
}

@Preview(name = "Starred Empty - Dark", showBackground = true)
@Composable
private fun StarredScreenEmptyDarkPreview() {
    CodeCheckTheme(darkTheme = true) {
        StarredContent(
            uiState = StarredUiState.Empty,
            onRepositoryClick = {},
            onUnstar = {},
            onClearAll = {}
        )
    }
}
