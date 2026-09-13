package jp.co.yumemi.android.codecheck.core.data.repository

import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultPreferencesRepositoryTest {

    @Test
    fun defaultPreferences_initialValuesAreSystem() = runTest {
        val repository = DefaultPreferencesRepository()
        assertEquals(ThemeMode.SYSTEM, repository.getThemeMode().first())
        assertEquals(AppLanguagePreference.SYSTEM, repository.getLanguagePreference().first())
    }

    @Test
    fun setThemeMode_updatesThemeFlow() = runTest {
        val repository = DefaultPreferencesRepository()
        repository.setThemeMode(ThemeMode.DARK)
        assertEquals(ThemeMode.DARK, repository.getThemeMode().first())

        repository.setThemeMode(ThemeMode.LIGHT)
        assertEquals(ThemeMode.LIGHT, repository.getThemeMode().first())
    }

    @Test
    fun setLanguagePreference_updatesLanguageFlow() = runTest {
        val repository = DefaultPreferencesRepository()
        repository.setLanguagePreference(AppLanguagePreference.JA)
        assertEquals(AppLanguagePreference.JA, repository.getLanguagePreference().first())

        repository.setLanguagePreference(AppLanguagePreference.EN)
        assertEquals(AppLanguagePreference.EN, repository.getLanguagePreference().first())
    }
}
