package com.krutik.weatherintelligence.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponseDto(
    @SerialName("dt") val dt: Long,
    @SerialName("name") val name: String? = null,
    @SerialName("main") val main: MainDataDto,
    @SerialName("weather") val weather: List<WeatherConditionDto> = emptyList(),
    @SerialName("wind") val wind: WindDto? = null,
    @SerialName("visibility") val visibility: Int = 10000,
    @SerialName("sys") val sys: SysDataDto? = null,
    @SerialName("coord") val coord: CoordDto? = null
)

@Serializable
data class MainDataDto(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("temp_min") val tempMin: Double = 0.0,
    @SerialName("temp_max") val tempMax: Double = 0.0,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int
)

@Serializable
data class WindDto(
    @SerialName("speed") val speed: Double
)

@Serializable
data class SysDataDto(
    @SerialName("country") val country: String? = null,
    @SerialName("sunrise") val sunrise: Long = 0,
    @SerialName("sunset") val sunset: Long = 0
)

@Serializable
data class CoordDto(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double
)
