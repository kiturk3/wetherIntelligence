package com.krutik.weatherintelligence.presentation.search

import app.cash.turbine.test
import com.krutik.weatherintelligence.domain.usecase.SearchCityUseCase
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import com.krutik.weatherintelligence.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeWeatherRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        val searchCityUseCase = SearchCityUseCase(repository)
        viewModel = SearchViewModel(searchCityUseCase)
    }

    @Test
    fun `onQueryChanged with length greater than 3 should trigger search`() = runTest {
        viewModel.onQueryChanged("London")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("London", state.query)
            assertFalse(state.isLoading)
            assertEquals(1, state.cities.size)
            assertEquals("London", state.cities.first().name)
        }
    }
}
