package com.krutik.weatherintelligence.di

import android.content.Context
import androidx.room.Room
import com.krutik.weatherintelligence.core.common.Constants
import com.krutik.weatherintelligence.data.local.WeatherDatabase
import com.krutik.weatherintelligence.data.local.dao.CacheMetadataDao
import com.krutik.weatherintelligence.data.local.dao.CityDao
import com.krutik.weatherintelligence.data.local.dao.ForecastDao
import com.krutik.weatherintelligence.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao = db.weatherDao()

    @Provides
    fun provideForecastDao(db: WeatherDatabase): ForecastDao = db.forecastDao()

    @Provides
    fun provideCityDao(db: WeatherDatabase): CityDao = db.cityDao()

    @Provides
    fun provideCacheMetadataDao(db: WeatherDatabase): CacheMetadataDao = db.cacheMetadataDao()
}
