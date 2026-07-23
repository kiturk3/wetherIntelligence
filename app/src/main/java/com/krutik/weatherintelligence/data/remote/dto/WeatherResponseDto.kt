package com.krutik.weatherintelligence.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDto(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double,
    @SerialName("timezone") val timezone: String,
    @SerialName("current") val current: CurrentDataDto? = null,
    @SerialName("hourly") val hourly: List<HourlyDataDto> = emptyList(),
    @SerialName("daily") val daily: List<DailyDataDto> = emptyList()
)

@Serializable
data class CurrentDataDto(
    @SerialName("dt") val dt: Long,
    @SerialName("sunrise") val sunrise: Long = 0,
    @SerialName("sunset") val sunset: Long = 0,
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("uvi") val uvi: Double = 0.0,
    @SerialName("visibility") val visibility: Int = 10000,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("weather") val weather: List<WeatherConditionDto> = emptyList()
)

@Serializable
data class HourlyDataDto(
    @SerialName("dt") val dt: Long,
    @SerialName("temp") val temp: Double,
    @SerialName("pop") val pop: Double = 0.0,
    @SerialName("weather") val weather: List<WeatherConditionDto> = emptyList()
)

@Serializable
data class DailyDataDto(
    @SerialName("dt") val dt: Long,
    @SerialName("temp") val temp: TempRangeDto,
    @SerialName("pop") val pop: Double = 0.0,
    @SerialName("weather") val weather: List<WeatherConditionDto> = emptyList()
)

@Serializable
data class TempRangeDto(
    @SerialName("min") val min: Double,
    @SerialName("max") val max: Double
)

@Serializable
data class WeatherConditionDto(
    @SerialName("id") val id: Int,
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)
