package jp.co.yumemi.android.codecheck.feature.settings

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasDefaultValues() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(AppThemeMode.SYSTEM, state.themeMode)
            assertEquals(AppLanguage.SYSTEM, state.language)
            assertEquals("1.0.0 (Build 1)", state.appVersion)
            assertEquals("PRODUCTION", state.environment)
            assertEquals("GitHub REST API v3", state.apiSource)
        }
    }

    @Test
    fun setThemeMode_updatesThemeModeInState() = runTest {
        viewModel.uiState.test {
            assertEquals(AppThemeMode.SYSTEM, awaitItem().themeMode)

            viewModel.setThemeMode(AppThemeMode.DARK)
            assertEquals(AppThemeMode.DARK, awaitItem().themeMode)

            viewModel.setThemeMode(AppThemeMode.LIGHT)
            assertEquals(AppThemeMode.LIGHT, awaitItem().themeMode)
        }
    }

    @Test
    fun setLanguage_updatesLanguageInState() = runTest {
        viewModel.uiState.test {
            assertEquals(AppLanguage.SYSTEM, awaitItem().language)

            viewModel.setLanguage(AppLanguage.JA)
            assertEquals(AppLanguage.JA, awaitItem().language)

            viewModel.setLanguage(AppLanguage.EN)
            assertEquals(AppLanguage.EN, awaitItem().language)
        }
    }
}
