package com.krutik.weatherintelligence.core.common

object Constants {
    const val DATABASE_NAME = "weather_intelligence_db"
    const val USER_PREFERENCES_NAME = "user_preferences"
    
    // Cache TTLs in Milliseconds
    const val CURRENT_WEATHER_TTL_MS = 15 * 60 * 1000L // 15 Minutes
    const val FORECAST_TTL_MS = 3 * 60 * 60 * 1000L     // 3 Hours
    
    // WorkManager sync tag
    const val WEATHER_SYNC_WORK_NAME = "weather_sync_work"
}
