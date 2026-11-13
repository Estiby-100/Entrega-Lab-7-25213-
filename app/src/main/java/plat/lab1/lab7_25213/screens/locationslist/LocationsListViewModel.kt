package plat.lab1.lab7_25213.screens.locationslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.repository.LocationRepository
import plat.lab1.lab7_25213.di.appContainer

class LocationsListViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LocationsListState())
    val state: StateFlow<LocationsListState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: LocationsListEvent) {
        when (event) {
            LocationsListEvent.OnLoadingClick -> showError()
            LocationsListEvent.OnRetryClick -> loadData()
            is LocationsListEvent.OnFavoriteClick -> toggleFavorite(event.id, event.isFavorite)
            is LocationsListEvent.OnFilterChange -> {
                _state.value = _state.value.copy(filter = event.filter)
            }
        }
    }

    private fun loadData() {
        _state.value = _state.value.copy(isLoading = true, hasError = false)
        loadJob = viewModelScope.launch {
            delay(4000)
            repository.getLocations().collect { locations ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    data = locations
                )
            }
        }
    }

    private fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(id, !isFavorite)
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = false, hasError = true)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                LocationsListViewModel(appContainer.locationRepository)
            }
        }
    }
}
