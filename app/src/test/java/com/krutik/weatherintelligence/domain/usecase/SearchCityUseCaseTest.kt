package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchCityUseCaseTest {

    private lateinit var repository: FakeWeatherRepository
    private lateinit var useCase: SearchCityUseCase

    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        useCase = SearchCityUseCase(repository)
    }

    @Test
    fun `invoke with blank query should return empty success list without calling repository`() = runTest {
        val result = useCase("   ")

        assertTrue(result is Resource.Success)
        assertTrue(result.data?.isEmpty() == true)
    }

    @Test
    fun `invoke with valid query should return cities list`() = runTest {
        val result = useCase("London")

        assertTrue(result is Resource.Success)
        assertEquals(1, result.data?.size)
        assertEquals("London", result.data?.first()?.name)
    }
}
