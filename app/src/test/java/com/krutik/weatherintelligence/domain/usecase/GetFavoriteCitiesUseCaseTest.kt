package com.krutik.weatherintelligence.domain.usecase

import app.cash.turbine.test
import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFavoriteCitiesUseCaseTest {

    private lateinit var repository: FakeWeatherRepository
    private lateinit var useCase: GetFavoriteCitiesUseCase

    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        useCase = GetFavoriteCitiesUseCase(repository)
    }

    @Test
    fun `invoke returns favorite cities stream from repository`() = runTest {
        val testCity = City("1", "London", "UK", 51.5, -0.1, isFavorite = true)
        repository.toggleFavoriteCity(testCity)

        useCase().test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("London", favorites.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
