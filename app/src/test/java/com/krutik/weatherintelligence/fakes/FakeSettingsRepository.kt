package com.krutik.weatherintelligence.fakes

import com.krutik.weatherintelligence.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val tempUnitFlow = MutableStateFlow("metric")
    private val themeModeFlow = MutableStateFlow("system")
    private val notificationFlow = MutableStateFlow(true)

    override fun getTemperatureUnit(): Flow<String> = tempUnitFlow

    override suspend fun setTemperatureUnit(unit: String) {
        tempUnitFlow.value = unit
    }

    override fun getThemeMode(): Flow<String> = themeModeFlow

    override suspend fun setThemeMode(mode: String) {
        themeModeFlow.value = mode
    }

    override fun isNotificationEnabled(): Flow<Boolean> = notificationFlow

    override suspend fun setNotificationEnabled(enabled: Boolean) {
        notificationFlow.value = enabled
    }
}
