package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun getTempUnit(): Flow<String> = settingsRepository.getTemperatureUnit()
    fun getThemeMode(): Flow<String> = settingsRepository.getThemeMode()
    fun isNotificationEnabled(): Flow<Boolean> = settingsRepository.isNotificationEnabled()
}
