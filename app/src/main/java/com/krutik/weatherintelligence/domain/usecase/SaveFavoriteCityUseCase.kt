package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveFavoriteCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: City) {
        if (!city.isFavorite) {
            repository.toggleFavoriteCity(city.copy(isFavorite = false))
        }
    }
}
