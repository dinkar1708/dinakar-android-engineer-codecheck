package jp.co.yumemi.android.codecheck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.feature.bookmarks.BookmarksViewModel
import jp.co.yumemi.android.codecheck.feature.detail.DetailScreen
import jp.co.yumemi.android.codecheck.feature.search.SearchScreen
import jp.co.yumemi.android.codecheck.feature.search.SearchViewModel
import jp.co.yumemi.android.codecheck.feature.settings.SettingsViewModel
import jp.co.yumemi.android.codecheck.feature.splash.SplashScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Main : Screen("main")
    data object Search : Screen("search")
    data object Detail : Screen("detail/{owner}/{repo}") {
        fun createRoute(owner: String, repo: String): String {
            val encodedOwner = URLEncoder.encode(owner, StandardCharsets.UTF_8.toString())
            val encodedRepo = URLEncoder.encode(repo, StandardCharsets.UTF_8.toString())
            return "detail/$encodedOwner/$encodedRepo"
        }
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    onOpenBrowser: ((String) -> Unit)? = null
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            val bookmarksViewModel = hiltViewModel<BookmarksViewModel>()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            MainScreen(
                searchViewModel = searchViewModel,
                bookmarksViewModel = bookmarksViewModel,
                settingsViewModel = settingsViewModel,
                onRepositoryClick = { item: RepositoryItem ->
                    val (owner, repo) = extractOwnerAndRepo(item)
                    navController.navigate(Screen.Detail.createRoute(owner, repo))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onRepositoryClick = { item: RepositoryItem ->
                    val (owner, repo) = extractOwnerAndRepo(item)
                    navController.navigate(Screen.Detail.createRoute(owner, repo))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() },
                onOpenBrowser = onOpenBrowser
            )
        }
    }
}

internal fun extractOwnerAndRepo(item: RepositoryItem): Pair<String, String> {
    if (!item.htmlUrl.isNullOrBlank() && item.htmlUrl!!.contains("github.com/")) {
        val path = item.htmlUrl!!.substringAfter("github.com/").trim('/')
        val parts = path.split("/")
        if (parts.size >= 2) {
            return Pair(parts[0], parts[1])
        }
    }
    if (item.name.contains("/")) {
        val parts = item.name.split("/")
        return Pair(parts[0], parts[1])
    }
    return Pair("repository", item.name)
}
