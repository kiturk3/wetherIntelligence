package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<City>> {
        return repository.getFavoriteCities()
    }
}
