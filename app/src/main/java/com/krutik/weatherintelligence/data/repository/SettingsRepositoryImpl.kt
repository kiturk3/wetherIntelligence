package com.krutik.weatherintelligence.data.repository

import com.krutik.weatherintelligence.data.local.datastore.UserPreferencesDataStore
import com.krutik.weatherintelligence.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : SettingsRepository {

    override fun getTemperatureUnit(): Flow<String> = dataStore.tempUnit

    override suspend fun setTemperatureUnit(unit: String) = dataStore.setTempUnit(unit)

    override fun getThemeMode(): Flow<String> = dataStore.themeMode

    override suspend fun setThemeMode(mode: String) = dataStore.setThemeMode(mode)

    override fun isNotificationEnabled(): Flow<Boolean> = dataStore.notificationEnabled

    override suspend fun setNotificationEnabled(enabled: Boolean) = dataStore.setNotificationEnabled(enabled)
}
