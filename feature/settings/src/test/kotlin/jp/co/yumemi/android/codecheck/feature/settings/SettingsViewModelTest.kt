package jp.co.yumemi.android.codecheck.feature.settings

import app.cash.turbine.test
import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode
import jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private lateinit var fakePreferencesRepository: FakePreferencesRepository
    private lateinit var viewModel: SettingsViewModel

    private class FakePreferencesRepository(
        initialThemeMode: ThemeMode = ThemeMode.SYSTEM,
        initialLanguage: AppLanguagePreference = AppLanguagePreference.SYSTEM
    ) : PreferencesRepository {
        private val _themeMode = MutableStateFlow(initialThemeMode)
        private val _language = MutableStateFlow(initialLanguage)

        override fun getThemeMode(): Flow<ThemeMode> = _themeMode.asStateFlow()
        override suspend fun setThemeMode(themeMode: ThemeMode) {
            _themeMode.value = themeMode
        }

        override fun getLanguagePreference(): Flow<AppLanguagePreference> = _language.asStateFlow()
        override suspend fun setLanguagePreference(language: AppLanguagePreference) {
            _language.value = language
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakePreferencesRepository = FakePreferencesRepository()
        viewModel = SettingsViewModel(preferencesRepository = fakePreferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasDefaultValues() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(ThemeMode.SYSTEM, state.themeMode)
            assertEquals(AppLanguagePreference.SYSTEM, state.language)
        }
    }

    @Test
    fun setThemeMode_updatesThemeModeInState() = runTest {
        viewModel.uiState.test {
            assertEquals(ThemeMode.SYSTEM, awaitItem().themeMode)

            viewModel.setThemeMode(ThemeMode.DARK)
            assertEquals(ThemeMode.DARK, awaitItem().themeMode)

            viewModel.setThemeMode(ThemeMode.LIGHT)
            assertEquals(ThemeMode.LIGHT, awaitItem().themeMode)
        }
    }

    @Test
    fun setLanguage_updatesLanguageInState() = runTest {
        viewModel.uiState.test {
            assertEquals(AppLanguagePreference.SYSTEM, awaitItem().language)

            viewModel.setLanguage(AppLanguagePreference.JA)
            assertEquals(AppLanguagePreference.JA, awaitItem().language)

            viewModel.setLanguage(AppLanguagePreference.EN)
            assertEquals(AppLanguagePreference.EN, awaitItem().language)
        }
    }
}
