package plat.lab1.lab7_25213.screens.locationslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.local.AppDatabase
import plat.lab1.lab7_25213.data.local.toModel

class LocationsListViewModel(application: Application) : AndroidViewModel(application) {
    private val locationDao = AppDatabase.getInstance(application).locationDao()

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
            locationDao.getAll().collect { entities ->
                _state.value = LocationsListState(
                    isLoading = false,
                    data = entities.map { it.toModel() }
                )
            }
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = LocationsListState(isLoading = false, hasError = true)
    }
}
