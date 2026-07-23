package com.krutik.weatherintelligence.data.remote.datasource

import com.krutik.weatherintelligence.data.remote.WeatherApiService
import com.krutik.weatherintelligence.data.remote.dto.CitySearchDto
import com.krutik.weatherintelligence.data.remote.dto.WeatherResponseDto
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: WeatherApiService
) : RemoteDataSource {

    override suspend fun getWeatherOneCall(
        lat: Double,
        lon: Double,
        units: String,
        apiKey: String
    ): WeatherResponseDto {
        return apiService.getWeatherOneCall(lat = lat, lon = lon, units = units, apiKey = apiKey)
    }

    override suspend fun searchCity(
        query: String,
        limit: Int,
        apiKey: String
    ): List<CitySearchDto> {
        return apiService.searchCity(query = query, limit = limit, apiKey = apiKey)
    }
}
