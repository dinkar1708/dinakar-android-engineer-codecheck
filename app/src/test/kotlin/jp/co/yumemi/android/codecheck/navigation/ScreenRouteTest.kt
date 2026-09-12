package jp.co.yumemi.android.codecheck.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [Screen] navigation route definitions and parameter encoding.
 */
class ScreenRouteTest {

    @Test
    fun screenSplash_routeIsConsistent() {
        assertEquals("splash", Screen.Splash.route)
    }

    @Test
    fun screenSearch_routeIsConsistent() {
        assertEquals("search", Screen.Search.route)
    }

    @Test
    fun screenDetail_routePatternMatchesExpectedTemplate() {
        assertEquals("detail/{owner}/{repo}", Screen.Detail.route)
    }

    @Test
    fun screenDetail_createRoute_formatsStandardArguments() {
        val route = Screen.Detail.createRoute(owner = "google", repo = "accompanist")
        assertEquals("detail/google/accompanist", route)
    }

    @Test
    fun screenDetail_createRoute_urlEncodesSpecialCharacters() {
        val route = Screen.Detail.createRoute(owner = "my org", repo = "repo#1")
        assertEquals("detail/my+org/repo%231", route)
    }
}
