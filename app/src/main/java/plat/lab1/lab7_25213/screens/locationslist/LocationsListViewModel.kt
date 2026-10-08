package plat.lab1.lab7_25213.screens.locationslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.LocationDb

class LocationsListViewModel : ViewModel() {

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
        }
    }

    private fun loadData() {
        _state.value = LocationsListState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(4000)
            _state.value = LocationsListState(
                isLoading = false,
                data = LocationDb().getAllLocations()
            )
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = LocationsListState(isLoading = false, hasError = true)
    }
}
