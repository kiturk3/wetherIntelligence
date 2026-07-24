package com.krutik.weatherintelligence.data.remote

import com.krutik.weatherintelligence.data.remote.dto.CitySearchDto
import com.krutik.weatherintelligence.data.remote.dto.CurrentWeatherResponseDto
import com.krutik.weatherintelligence.data.remote.dto.ForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): CurrentWeatherResponseDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): ForecastResponseDto

    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<CitySearchDto>
}
