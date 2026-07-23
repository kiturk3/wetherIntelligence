package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import javax.inject.Inject

class RefreshWeatherDataUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Resource<Unit> {
        return repository.refreshWeather(lat, lon)
    }
}
