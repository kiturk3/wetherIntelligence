package com.krutik.weatherintelligence.data.remote.datasource

import com.krutik.weatherintelligence.data.remote.dto.CitySearchDto
import com.krutik.weatherintelligence.data.remote.dto.CurrentWeatherResponseDto
import com.krutik.weatherintelligence.data.remote.dto.ForecastResponseDto

interface RemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double, units: String, apiKey: String): CurrentWeatherResponseDto
    suspend fun getForecast(lat: Double, lon: Double, units: String, apiKey: String): ForecastResponseDto
    suspend fun searchCity(query: String, limit: Int, apiKey: String): List<CitySearchDto>
}
