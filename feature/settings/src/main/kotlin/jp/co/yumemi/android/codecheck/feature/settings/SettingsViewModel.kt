package jp.co.yumemi.android.codecheck.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.core.domain.model.AppBuildInfo
import jp.co.yumemi.android.codecheck.core.domain.model.AppLanguagePreference
import jp.co.yumemi.android.codecheck.core.domain.model.ThemeMode
import jp.co.yumemi.android.codecheck.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val appBuildInfo: AppBuildInfo = AppBuildInfo()
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesRepository.getThemeMode(),
        preferencesRepository.getLanguagePreference()
    ) { themeMode, language ->
        SettingsUiState(
            themeMode = themeMode,
            language = language,
            appVersion = appBuildInfo.formattedVersion,
            environment = appBuildInfo.environmentLabel,
            apiSource = appBuildInfo.apiSourceLabel
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SettingsUiState(
            appVersion = appBuildInfo.formattedVersion,
            environment = appBuildInfo.environmentLabel,
            apiSource = appBuildInfo.apiSourceLabel
        )
    )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    fun setLanguage(language: AppLanguagePreference) {
        viewModelScope.launch {
            preferencesRepository.setLanguagePreference(language)
        }
    }
}
