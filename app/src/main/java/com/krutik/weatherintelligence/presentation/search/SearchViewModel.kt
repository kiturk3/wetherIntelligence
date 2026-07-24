package com.krutik.weatherintelligence.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.model.City
import com.krutik.weatherintelligence.domain.usecase.DeleteFavoriteCityUseCase
import com.krutik.weatherintelligence.domain.usecase.GetFavoriteCitiesUseCase
import com.krutik.weatherintelligence.domain.usecase.SaveFavoriteCityUseCase
import com.krutik.weatherintelligence.domain.usecase.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val cities: List<City> = emptyList(),
    val favoriteCities: List<City> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val saveFavoriteCityUseCase: SaveFavoriteCityUseCase,
    private val deleteFavoriteCityUseCase: DeleteFavoriteCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeFavoriteCities()
    }

    private fun observeFavoriteCities() {
        viewModelScope.launch {
            getFavoriteCitiesUseCase().collect { favorites ->
                val favIds = favorites.map { it.id }.toSet()
                val updatedCities = _uiState.value.cities.map { city ->
                    city.copy(isFavorite = favIds.contains(city.id))
                }
                _uiState.value = _uiState.value.copy(
                    favoriteCities = favorites,
                    cities = updatedCities
                )
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        if (newQuery.length >= 3) {
            searchCity(newQuery)
        } else if (newQuery.isEmpty()) {
            _uiState.value = _uiState.value.copy(cities = emptyList())
        }
    }

    fun toggleFavorite(city: City) {
        viewModelScope.launch {
            if (city.isFavorite) {
                deleteFavoriteCityUseCase(city)
            } else {
                saveFavoriteCityUseCase(city)
            }
        }
    }

    private fun searchCity(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = searchCityUseCase(query)) {
                is Resource.Success -> {
                    val favIds = _uiState.value.favoriteCities.map { it.id }.toSet()
                    val citiesWithFav = (result.data ?: emptyList()).map { city ->
                        city.copy(isFavorite = favIds.contains(city.id))
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cities = citiesWithFav,
                        error = null
                    )
                }
                is Resource.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
                is Resource.Loading -> {}
            }
        }
    }
}
