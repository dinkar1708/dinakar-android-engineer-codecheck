package jp.co.yumemi.android.codecheck.feature.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppAmber
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppBlue
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppNavy
import jp.co.yumemi.android.codecheck.core.designsystem.theme.AppWhite
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate200
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate300
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate400
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate500
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate600
import jp.co.yumemi.android.codecheck.core.designsystem.theme.Slate900
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.ui.component.ErrorView
import jp.co.yumemi.android.codecheck.core.ui.component.LoadingView
import jp.co.yumemi.android.codecheck.core.ui.util.formatDecimalNumber
import jp.co.yumemi.android.codecheck.core.ui.util.getMonogramInitials
import jp.co.yumemi.android.codecheck.feature.detail.R
import jp.co.yumemi.android.codecheck.feature.detail.component.MetaRow

/**
 * Repository detail screen composable strictly adhering to the design specification:
 * - Dark Navy header (#2D3545) with back navigation, avatar, owner handle, repo title, description, and language chip
 * - Light background with 2x2 grid of StatCards (Stars, Forks, Watchers, Open Issues)
 * - Full-width "View on GitHub" brand blue action button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenBrowser: ((String) -> Unit)? = null,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val effectiveOpenBrowser = onOpenBrowser ?: remember(context) {
        { url: String -> launchChromeCustomTab(context, url) }
    }
    val uiState by viewModel.uiState.collectAsState()
    DetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetry = viewModel::retry,
        modifier = modifier,
        onOpenBrowser = effectiveOpenBrowser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreen(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenBrowser: ((String) -> Unit)? = null
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.detail_top_bar_title),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AppWhite
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_navigate_back),
                            tint = AppWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppNavy,
                    titleContentColor = AppWhite,
                    navigationIconContentColor = AppWhite
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    LoadingView(message = stringResource(R.string.detail_loading_message))
                }
                is DetailUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = onRetry
                    )
                }
                is DetailUiState.Success -> {
                    DetailContent(
                        repository = state.repository,
                        onOpenBrowser = onOpenBrowser
                    )
                }
            }
        }
    }
}

@Composable
internal fun DetailContent(
    repository: RepositoryItem,
    onOpenBrowser: ((String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hero Header Section (Navy Background matching 01-05 Core Screens.dc.html)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppNavy)
                .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Owner + Repo Name row with Avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val ownerLogin = repository.owner.login.takeIf { it.isNotBlank() }
                        ?: if (repository.name.contains("/")) repository.name.substringBefore("/") else null
                    val displayName = if (repository.name.contains("/")) repository.name.substringAfter("/") else repository.name

                    val initials = remember(displayName, ownerLogin) {
                        val source = ownerLogin ?: displayName
                        getMonogramInitials(source)
                    }

                    SubcomposeAsyncImage(
                        model = repository.ownerIconUrl,
                        contentDescription = stringResource(R.string.detail_avatar_content_description, displayName),
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Slate600),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppWhite
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Slate600),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppWhite
                                )
                            }
                        }
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        if (ownerLogin != null) {
                            Text(
                                text = ownerLogin,
                                fontSize = 13.sp,
                                color = Slate400,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = displayName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppWhite,
                            lineHeight = 28.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Description (if present)
                val description = repository.description
                if (!description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Slate300
                    )
                }

                // Language Chip (if present)
                val language = repository.language
                if (!language.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Slate600,
                        contentColor = Slate300
                    ) {
                        Text(
                            text = language,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Body Content: 2x2 Stat Cards and Action Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 2x2 StatCard Grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.detail_stat_stars),
                        value = formatDecimalNumber(repository.stargazersCount),
                        valueColor = Slate900,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.detail_stat_forks),
                        value = formatDecimalNumber(repository.forksCount),
                        valueColor = Slate900,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.detail_stat_watchers),
                        value = formatDecimalNumber(repository.watchersCount),
                        valueColor = Slate900,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.detail_stat_open_issues),
                        value = formatDecimalNumber(repository.openIssuesCount),
                        valueColor = AppAmber,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 4 Extra Meta Rows (Default branch, Last push, License, Size)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MetaRow(
                    label = stringResource(R.string.detail_meta_default_branch),
                    value = repository.defaultBranch?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.detail_meta_not_available),
                    fontFamily = FontFamily.Monospace
                )
                MetaRow(
                    label = stringResource(R.string.detail_meta_last_push),
                    value = DetailFormatters.formatPushDate(repository.pushedAt)
                )
                MetaRow(
                    label = stringResource(R.string.detail_meta_license),
                    value = repository.license?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.detail_meta_none)
                )
                MetaRow(
                    label = stringResource(R.string.detail_meta_size),
                    value = DetailFormatters.formatSize(repository.size)
                )
            }

            // Action Buttons: View on GitHub + Companion Download
            if (!repository.htmlUrl.isNullOrBlank() && onOpenBrowser != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onOpenBrowser(repository.htmlUrl.orEmpty()) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppBlue,
                            contentColor = AppWhite
                        ),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.detail_view_on_github),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    val downloadUrl = remember(repository.htmlUrl, repository.defaultBranch) {
                        val branch = repository.defaultBranch?.takeIf { it.isNotBlank() } ?: "main"
                        "${repository.htmlUrl}/archive/refs/heads/$branch.zip"
                    }

                    Surface(
                        onClick = { onOpenBrowser(downloadUrl) },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = AppWhite,
                        border = BorderStroke(1.dp, Slate300)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = stringResource(R.string.detail_download),
                                tint = Slate900,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Metric StatCard component adhering to StatCard.dc.html specification:
 * - 8dp rounded card container with 1dp Slate200 border
 * - 11sp bold uppercase label in Slate500
 * - 24sp bold metric value with dynamic value color
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Slate900
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppWhite
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Slate200
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.05.em,
                color = Slate500
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

/**
 * Opens the given web [url] inside a Chrome Custom Tab styled with Brand Navy header (#2D3545),
 * falling back to the standard system browser if Custom Tabs cannot be opened.
 */
internal fun launchChromeCustomTab(context: Context, url: String) {
    if (url.isBlank()) return
    try {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(AppNavy.toArgb())
                    .build()
            )
            .build()
        if (context !is Activity) {
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        customTabsIntent.launchUrl(context, Uri.parse(url))
    } catch (_: Exception) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (context !is Activity) {
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        } catch (_: Exception) {
            // Silently ignore if no browser application can handle the intent
        }
    }
}
