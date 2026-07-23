package com.krutik.weatherintelligence.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getTemperatureUnit(): Flow<String> // "metric" or "imperial"
    suspend fun setTemperatureUnit(unit: String)
    fun getThemeMode(): Flow<String> // "system", "light", "dark"
    suspend fun setThemeMode(mode: String)
    fun isNotificationEnabled(): Flow<Boolean>
    suspend fun setNotificationEnabled(enabled: Boolean)
}
