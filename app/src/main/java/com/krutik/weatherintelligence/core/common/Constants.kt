package com.krutik.weatherintelligence.core.common

object Constants {
    const val DATABASE_NAME = "weather_intelligence_db"
    const val USER_PREFERENCES_NAME = "user_preferences"
    
    // Cache TTLs in Milliseconds
    const val CURRENT_WEATHER_TTL_MS = 15 * 60 * 1000L // 15 Minutes
    const val HOURLY_FORECAST_TTL_MS = 60 * 60 * 1000L  // 1 Hour
    const val DAILY_FORECAST_TTL_MS = 3 * 60 * 60 * 1000L // 3 Hours

    // Cache Keys
    const val KEY_CURRENT_WEATHER = "CURRENT_WEATHER"
    const val KEY_HOURLY_FORECAST = "HOURLY_FORECAST"
    const val KEY_DAILY_FORECAST = "DAILY_FORECAST"

    // WorkManager sync tag
    const val WEATHER_SYNC_WORK_NAME = "weather_sync_work"
}
