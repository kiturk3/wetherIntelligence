package com.krutik.weatherintelligence.presentation.home

sealed interface HomeEvent {
    data class RefreshWeather(val lat: Double, val lon: Double) : HomeEvent
    data class SelectCity(val lat: Double, val lon: Double, val cityName: String) : HomeEvent
}
