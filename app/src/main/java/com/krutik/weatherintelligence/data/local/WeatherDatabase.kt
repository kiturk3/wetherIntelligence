package com.krutik.weatherintelligence.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krutik.weatherintelligence.data.local.dao.CacheMetadataDao
import com.krutik.weatherintelligence.data.local.dao.CityDao
import com.krutik.weatherintelligence.data.local.dao.ForecastDao
import com.krutik.weatherintelligence.data.local.dao.WeatherDao
import com.krutik.weatherintelligence.data.local.entity.CacheMetadataEntity
import com.krutik.weatherintelligence.data.local.entity.CityEntity
import com.krutik.weatherintelligence.data.local.entity.ForecastEntity
import com.krutik.weatherintelligence.data.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        ForecastEntity::class,
        CityEntity::class,
        CacheMetadataEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
    abstract fun cityDao(): CityDao
    abstract fun cacheMetadataDao(): CacheMetadataDao
}
