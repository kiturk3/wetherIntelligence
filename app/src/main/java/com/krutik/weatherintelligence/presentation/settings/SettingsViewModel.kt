package com.krutik.weatherintelligence.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krutik.weatherintelligence.domain.usecase.GetSettingsUseCase
import com.krutik.weatherintelligence.domain.usecase.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {

    val tempUnit: StateFlow<String> = getSettingsUseCase.getTempUnit()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "metric")

    val themeMode: StateFlow<String> = getSettingsUseCase.getThemeMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    val notificationsEnabled: StateFlow<Boolean> = getSettingsUseCase.isNotificationEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun toggleTempUnit(unit: String) {
        viewModelScope.launch {
            updateSettingsUseCase.setTempUnit(unit)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            updateSettingsUseCase.setThemeMode(mode)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.setNotificationEnabled(enabled)
        }
    }
}
