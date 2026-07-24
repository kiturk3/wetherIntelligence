package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshWeatherDataUseCaseTest {

    private lateinit var repository: FakeWeatherRepository
    private lateinit var useCase: RefreshWeatherDataUseCase

    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        useCase = RefreshWeatherDataUseCase(repository)
    }

    @Test
    fun `invoke should trigger repository refresh and return success`() = runTest {
        val result = useCase(28.6, 77.2)
        assertTrue(result is Resource.Success)
    }
}
