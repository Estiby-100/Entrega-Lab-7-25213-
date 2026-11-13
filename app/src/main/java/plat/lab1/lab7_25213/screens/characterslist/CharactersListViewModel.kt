package plat.lab1.lab7_25213.screens.characterslist

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
import plat.lab1.lab7_25213.data.repository.CharacterRepository
import plat.lab1.lab7_25213.di.appContainer

class CharactersListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

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
            repository.getCharacters().collect { characters ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    data = characters
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
                CharactersListViewModel(appContainer.characterRepository)
            }
        }
    }
}
