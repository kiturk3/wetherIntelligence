package com.krutik.weatherintelligence.data.remote.datasource

import com.krutik.weatherintelligence.data.remote.dto.CitySearchDto
import com.krutik.weatherintelligence.data.remote.dto.WeatherResponseDto

interface RemoteDataSource {
    suspend fun getWeatherOneCall(lat: Double, lon: Double, units: String, apiKey: String): WeatherResponseDto
    suspend fun searchCity(query: String, limit: Int, apiKey: String): List<CitySearchDto>
}
