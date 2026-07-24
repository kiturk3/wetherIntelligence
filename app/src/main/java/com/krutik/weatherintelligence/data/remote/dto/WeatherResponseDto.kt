package com.krutik.weatherintelligence.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherConditionDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("main") val main: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("icon") val icon: String = ""
)

@Serializable
data class CitySearchDto(
    @SerialName("name") val name: String,
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double,
    @SerialName("country") val country: String,
    @SerialName("state") val state: String? = null
)
