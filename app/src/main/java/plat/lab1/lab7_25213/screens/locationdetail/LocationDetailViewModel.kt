package plat.lab1.lab7_25213.screens.locationdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.repository.LocationRepository
import plat.lab1.lab7_25213.di.appContainer
import plat.lab1.lab7_25213.navigation.LocationDetailDestination

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LocationRepository
) : ViewModel() {
    private val locationId = savedStateHandle.toRoute<LocationDetailDestination>().locationId

    private val _state = MutableStateFlow(LocationDetailState())
    val state: StateFlow<LocationDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: LocationDetailEvent) {
        when (event) {
            LocationDetailEvent.OnLoadingClick -> showError()
            LocationDetailEvent.OnRetryClick -> loadData()
            LocationDetailEvent.OnFavoriteClick -> toggleFavorite()
        }
    }

    private fun loadData() {
        _state.value = LocationDetailState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            repository.getLocation(locationId).collect { location ->
                _state.value = LocationDetailState(
                    isLoading = false,
                    data = location
                )
            }
        }
    }

    private fun toggleFavorite() {
        val location = _state.value.data ?: return
        viewModelScope.launch {
            repository.setFavorite(location.id, !location.isFavorite)
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = LocationDetailState(isLoading = false, hasError = true)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                LocationDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = appContainer.locationRepository
                )
            }
        }
    }
}
