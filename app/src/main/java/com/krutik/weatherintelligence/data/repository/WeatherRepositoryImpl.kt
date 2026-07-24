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
            val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            val currentWeatherDto = remoteDataSource.getCurrentWeather(lat = lat, lon = lon, units = "metric", apiKey = apiKey)
            val forecastDto = remoteDataSource.getForecast(lat = lat, lon = lon, units = "metric", apiKey = apiKey)
            val now = System.currentTimeMillis()

            val mainWeather = currentWeatherDto.weather.firstOrNull()
            val sys = currentWeatherDto.sys

            val weatherEntity = WeatherEntity(
                cityName = currentWeatherDto.name ?: "Unknown Location",
                lat = lat,
                lon = lon,
                temperature = currentWeatherDto.main.temp,
                feelsLike = currentWeatherDto.main.feelsLike,
                condition = mainWeather?.main ?: "Clear",
                icon = mainWeather?.icon ?: "01d",
                humidity = currentWeatherDto.main.humidity,
                windSpeed = currentWeatherDto.wind?.speed ?: 0.0,
                pressure = currentWeatherDto.main.pressure,
                visibility = currentWeatherDto.visibility,
                uvIndex = 5.0, // Default for free tier
                sunrise = sys?.sunrise ?: 0L,
                sunset = sys?.sunset ?: 0L,
                updatedAt = now
            )
            weatherDao.insertWeather(weatherEntity)

            // Save Cache Metadata (15 Min TTL)
            cacheMetadataDao.insertCacheMetadata(
                CacheMetadataEntity(
                    cacheKey = "CURRENT_WEATHER",
                    ttlMs = Constants.CURRENT_WEATHER_TTL_MS,
                    updatedAt = now,
                    expiresAt = now + Constants.CURRENT_WEATHER_TTL_MS
                )
            )

            // Process Forecast Items
            val forecastItems = forecastDto.list

            // Hourly (First 8 intervals = 24 Hours)
            val hourlyEntities = forecastItems.take(8).map { item ->
                val weather = item.weather.firstOrNull()
                ForecastEntity(
                    type = "HOURLY",
                    timeEpoch = item.dt,
                    temp = item.main.temp,
                    minTemp = item.main.tempMin,
                    maxTemp = item.main.tempMax,
                    condition = weather?.main ?: "Clear",
                    icon = weather?.icon ?: "01d",
                    pop = item.pop
                )
            }
            forecastDao.clearForecastByType("HOURLY")
            forecastDao.insertForecasts(hourlyEntities)

            // Daily (Group by 24h day intervals)
            val dailyEntities = forecastItems.chunked(8).take(5).map { dayChunk ->
                val first = dayChunk.first()
                val minT = dayChunk.minOf { it.main.tempMin }
                val maxT = dayChunk.maxOf { it.main.tempMax }
                val weather = first.weather.firstOrNull()

                ForecastEntity(
                    type = "DAILY",
                    timeEpoch = first.dt,
                    temp = (minT + maxT) / 2,
                    minTemp = minT,
                    maxTemp = maxT,
                    condition = weather?.main ?: "Clear",
                    icon = weather?.icon ?: "01d",
                    pop = dayChunk.maxOf { it.pop }
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
