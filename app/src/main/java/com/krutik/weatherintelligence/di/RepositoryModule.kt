package com.krutik.weatherintelligence.di

import com.krutik.weatherintelligence.data.repository.SettingsRepositoryImpl
import com.krutik.weatherintelligence.data.repository.WeatherRepositoryImpl
import com.krutik.weatherintelligence.domain.repository.SettingsRepository
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
