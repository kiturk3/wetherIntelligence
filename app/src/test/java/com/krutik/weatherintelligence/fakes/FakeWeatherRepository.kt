package com.krutik.weatherintelligence.fakes

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.model.CurrentWeather
import com.krutik.weatherintelligence.domain.model.DailyForecast
import com.krutik.weatherintelligence.domain.model.HourlyForecast
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRepository : WeatherRepository {

    private val sampleWeather = CurrentWeather(
        cityName = "New Delhi",
        temperature = 28.0,
        feelsLike = 30.0,
        condition = "Sunny",
        icon = "01d",
        humidity = 55,
        windSpeed = 12.0,
        pressure = 1012,
        visibility = 10000,
        uvIndex = 6.0,
        sunrise = 1600000000L,
        sunset = 1600040000L,
        timestamp = System.currentTimeMillis()
    )

    private val favoriteCities = mutableListOf<City>()
    var shouldReturnError = false

    override fun getCurrentWeather(lat: Double, lon: Double, forceRefresh: Boolean): Flow<Resource<CurrentWeather>> {
        return if (shouldReturnError) {
            flowOf(Resource.Error("Failed to fetch weather"))
        } else {
            flowOf(Resource.Success(sampleWeather))
        }
    }

    override fun getHourlyForecast(lat: Double, lon: Double): Flow<Resource<List<HourlyForecast>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override fun getDailyForecast(lat: Double, lon: Double): Flow<Resource<List<DailyForecast>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun searchCity(query: String): Resource<List<City>> {
        return if (shouldReturnError) {
            Resource.Error("Search error")
        } else {
            Resource.Success(listOf(City("1", "London", "UK", 51.5, -0.1)))
        }
    }

    override fun getFavoriteCities(): Flow<List<City>> {
        return MutableStateFlow(favoriteCities)
    }

    override suspend fun toggleFavoriteCity(city: City) {
        if (favoriteCities.any { it.id == city.id }) {
            favoriteCities.removeIf { it.id == city.id }
        } else {
            favoriteCities.add(city.copy(isFavorite = true))
        }
    }

    override suspend fun refreshWeather(lat: Double, lon: Double): Resource<Unit> {
        return if (shouldReturnError) Resource.Error("Refresh error") else Resource.Success(Unit)
    }
}
