package plat.lab1.lab7_25213.screens.locationdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.LocationDb
import plat.lab1.lab7_25213.navigation.LocationDetailDestination

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
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
        }
    }

    private fun loadData() {
        _state.value = LocationDetailState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            _state.value = LocationDetailState(
                isLoading = false,
                data = LocationDb().getLocationById(locationId)
            )
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = LocationDetailState(isLoading = false, hasError = true)
    }
}
