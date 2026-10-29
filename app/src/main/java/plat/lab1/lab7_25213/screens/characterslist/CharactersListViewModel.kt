package plat.lab1.lab7_25213.screens.characterslist

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

class CharactersListViewModel(application: Application) : AndroidViewModel(application) {
    private val characterDao = AppDatabase.getInstance(application).characterDao()

    private val _state = MutableStateFlow(CharactersListState())
    val state: StateFlow<CharactersListState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: CharactersListEvent) {
        when (event) {
            CharactersListEvent.OnLoadingClick -> showError()
            CharactersListEvent.OnRetryClick -> loadData()
            is CharactersListEvent.OnFavoriteClick -> toggleFavorite(event.id, event.isFavorite)
            CharactersListEvent.OnFilterClick -> {
                _state.value = _state.value.copy(
                    showOnlyFavorites = !_state.value.showOnlyFavorites
                )
            }
        }
    }

    private fun loadData() {
        _state.value = _state.value.copy(isLoading = true, hasError = false)
        loadJob = viewModelScope.launch {
            delay(4000)
            characterDao.getAll().collect { entities ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    data = entities.map { it.toModel() }
                )
            }
        }
    }

    private fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            characterDao.updateFavorite(id, !isFavorite)
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = false, hasError = true)
    }
}
