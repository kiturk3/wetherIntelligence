package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend fun setTempUnit(unit: String) = settingsRepository.setTemperatureUnit(unit)
    suspend fun setThemeMode(mode: String) = settingsRepository.setThemeMode(mode)
    suspend fun setNotificationEnabled(enabled: Boolean) = settingsRepository.setNotificationEnabled(enabled)
}
