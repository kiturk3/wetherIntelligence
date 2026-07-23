package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: String): Resource<List<City>> {
        if (query.isBlank()) return Resource.Success(emptyList())
        return repository.searchCity(query)
    }
}
