package com.krutik.weatherintelligence.data.repository

import com.krutik.weatherintelligence.BuildConfig
import com.krutik.weatherintelligence.core.common.Constants
import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.data.local.WeatherDatabase
import com.krutik.weatherintelligence.data.local.entity.CacheMetadataEntity
import com.krutik.weatherintelligence.data.local.entity.CityEntity
import com.krutik.weatherintelligence.data.local.entity.ForecastEntity
import com.krutik.weatherintelligence.data.local.entity.WeatherEntity
import com.krutik.weatherintelligence.data.remote.datasource.RemoteDataSource
import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.model.CurrentWeather
import com.krutik.weatherintelligence.domain.model.DailyForecast
import com.krutik.weatherintelligence.domain.model.HourlyForecast
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val db: WeatherDatabase
) : WeatherRepository {

    private val weatherDao = db.weatherDao()
    private val forecastDao = db.forecastDao()
    private val cityDao = db.cityDao()
    private val cacheMetadataDao = db.cacheMetadataDao()

    override fun getCurrentWeather(lat: Double, lon: Double, forceRefresh: Boolean): Flow<Resource<CurrentWeather>> = flow {
        emit(Resource.Loading())

        val metadata = cacheMetadataDao.getCacheMetadata("CURRENT_WEATHER")
        val isCacheExpired = metadata == null || System.currentTimeMillis() > metadata.expiresAt

        if (forceRefresh || isCacheExpired) {
            refreshWeather(lat, lon)
        }

        weatherDao.getCurrentWeather().map { entity ->
            if (entity != null) {
                CurrentWeather(
                    cityName = entity.cityName,
                    temperature = entity.temperature,
                    feelsLike = entity.feelsLike,
                    condition = entity.condition,
                    icon = entity.icon,
                    humidity = entity.humidity,
                    windSpeed = entity.windSpeed,
                    pressure = entity.pressure,
                    visibility = entity.visibility,
                    uvIndex = entity.uvIndex,
                    sunrise = entity.sunrise,
                    sunset = entity.sunset,
                    timestamp = entity.updatedAt
                )
            } else null
        }.collect { weather ->
            if (weather != null) {
                emit(Resource.Success(weather))
            } else {
                val refreshResult = refreshWeather(lat, lon)
                if (refreshResult is Resource.Error) {
                    emit(Resource.Error(refreshResult.message ?: "Failed to fetch weather"))
                }
            }
        }
    }

    override fun getHourlyForecast(lat: Double, lon: Double): Flow<Resource<List<HourlyForecast>>> {
        return forecastDao.getForecastByType("HOURLY").map { entities ->
            Resource.Success(entities.map {
                HourlyForecast(
                    timeEpoch = it.timeEpoch,
                    temp = it.temp,
                    condition = it.condition,
                    icon = it.icon,
                    pop = it.pop
                )
            })
        }
    }

    override fun getDailyForecast(lat: Double, lon: Double): Flow<Resource<List<DailyForecast>>> {
        return forecastDao.getForecastByType("DAILY").map { entities ->
            Resource.Success(entities.map {
                DailyForecast(
                    dayEpoch = it.timeEpoch,
                    minTemp = it.minTemp,
                    maxTemp = it.maxTemp,
                    condition = it.condition,
                    icon = it.icon,
                    pop = it.pop
                )
            })
        }
    }

    override suspend fun searchCity(query: String): Resource<List<City>> {
        return try {
            val response = remoteDataSource.searchCity(query = query, limit = 5, apiKey = BuildConfig.OPEN_WEATHER_API_KEY)
            val cities = response.map {
                City(
                    id = "${it.lat},${it.lon}",
                    name = it.name,
                    country = it.country,
                    lat = it.lat,
                    lon = it.lon
                )
            }
            Resource.Success(cities)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "City search failed")
        }
    }

    override fun getFavoriteCities(): Flow<List<City>> {
        return cityDao.getFavoriteCities().map { entities ->
            entities.map {
                City(
                    id = it.id,
                    name = it.name,
                    country = it.country,
                    lat = it.lat,
                    lon = it.lon,
                    isFavorite = it.isFavorite
                )
            }
        }
    }

    override suspend fun toggleFavoriteCity(city: City) {
        val entity = CityEntity(
            id = city.id,
            name = city.name,
            country = city.country,
            lat = city.lat,
            lon = city.lon,
            isFavorite = !city.isFavorite
        )
        cityDao.insertCity(entity)
    }

    override suspend fun refreshWeather(lat: Double, lon: Double): Resource<Unit> {
        return try {
            val dto = remoteDataSource.getWeatherOneCall(lat = lat, lon = lon, units = "metric", apiKey = BuildConfig.OPEN_WEATHER_API_KEY)
            val current = dto.current
            val now = System.currentTimeMillis()

            if (current != null) {
                val weatherEntity = WeatherEntity(
                    cityName = dto.timezone,
                    lat = lat,
                    lon = lon,
                    temperature = current.temp,
                    feelsLike = current.feelsLike,
                    condition = current.weather.firstOrNull()?.main ?: "Unknown",
                    icon = current.weather.firstOrNull()?.icon ?: "",
                    humidity = current.humidity,
                    windSpeed = current.windSpeed,
                    pressure = current.pressure,
                    visibility = current.visibility,
                    uvIndex = current.uvi,
                    sunrise = current.sunrise,
                    sunset = current.sunset,
                    updatedAt = now
                )
                weatherDao.insertWeather(weatherEntity)
            }

            // Save Cache Metadata (15 Min TTL)
            cacheMetadataDao.insertCacheMetadata(
                CacheMetadataEntity(
                    cacheKey = "CURRENT_WEATHER",
                    ttlMs = Constants.CURRENT_WEATHER_TTL_MS,
                    updatedAt = now,
                    expiresAt = now + Constants.CURRENT_WEATHER_TTL_MS
                )
            )

            // Insert Hourly
            val hourlyEntities = dto.hourly.take(24).map {
                ForecastEntity(
                    type = "HOURLY",
                    timeEpoch = it.dt,
                    temp = it.temp,
                    minTemp = 0.0,
                    maxTemp = 0.0,
                    condition = it.weather.firstOrNull()?.main ?: "",
                    icon = it.weather.firstOrNull()?.icon ?: "",
                    pop = it.pop
                )
            }
            forecastDao.clearForecastByType("HOURLY")
            forecastDao.insertForecasts(hourlyEntities)

            // Insert Daily
            val dailyEntities = dto.daily.take(7).map {
                ForecastEntity(
                    type = "DAILY",
                    timeEpoch = it.dt,
                    temp = (it.temp.min + it.temp.max) / 2,
                    minTemp = it.temp.min,
                    maxTemp = it.temp.max,
                    condition = it.weather.firstOrNull()?.main ?: "",
                    icon = it.weather.firstOrNull()?.icon ?: "",
                    pop = it.pop
                )
            }
            forecastDao.clearForecastByType("DAILY")
            forecastDao.insertForecasts(dailyEntities)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to refresh weather")
        }
    }
}
