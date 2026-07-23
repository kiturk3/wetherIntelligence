package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.model.HourlyForecast
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHourlyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<List<HourlyForecast>>> {
        return repository.getHourlyForecast(lat, lon)
    }
}
