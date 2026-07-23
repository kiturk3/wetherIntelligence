package com.krutik.weatherintelligence.domain.repository

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.model.CurrentWeather
import com.krutik.weatherintelligence.domain.model.DailyForecast
import com.krutik.weatherintelligence.domain.model.HourlyForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(lat: Double, lon: Double, forceRefresh: Boolean = false): Flow<Resource<CurrentWeather>>
    fun getHourlyForecast(lat: Double, lon: Double): Flow<Resource<List<HourlyForecast>>>
    fun getDailyForecast(lat: Double, lon: Double): Flow<Resource<List<DailyForecast>>>
    suspend fun searchCity(query: String): Resource<List<City>>
    fun getFavoriteCities(): Flow<List<City>>
    suspend fun toggleFavoriteCity(city: City)
    suspend fun refreshWeather(lat: Double, lon: Double): Resource<Unit>
}
