package com.krutik.weatherintelligence.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseDto(
    @SerialName("list") val list: List<ForecastItemDto> = emptyList(),
    @SerialName("city") val city: CityInfoDto? = null
)

@Serializable
data class ForecastItemDto(
    @SerialName("dt") val dt: Long,
    @SerialName("main") val main: MainDataDto,
    @SerialName("weather") val weather: List<WeatherConditionDto> = emptyList(),
    @SerialName("wind") val wind: WindDto? = null,
    @SerialName("pop") val pop: Double = 0.0,
    @SerialName("dt_txt") val dtTxt: String? = null
)

@Serializable
data class CityInfoDto(
    @SerialName("id") val id: Long = 0,
    @SerialName("name") val name: String,
    @SerialName("country") val country: String? = null,
    @SerialName("sunrise") val sunrise: Long = 0,
    @SerialName("sunset") val sunset: Long = 0,
    @SerialName("coord") val coord: CoordDto? = null
)
