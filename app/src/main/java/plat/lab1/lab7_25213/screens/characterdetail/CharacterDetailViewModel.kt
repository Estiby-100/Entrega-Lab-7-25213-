package plat.lab1.lab7_25213.screens.characterdetail

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
import plat.lab1.lab7_25213.data.repository.CharacterRepository
import plat.lab1.lab7_25213.di.appContainer
import plat.lab1.lab7_25213.navigation.CharacterDetailDestination

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {
    private val characterId = savedStateHandle.toRoute<CharacterDetailDestination>().characterId

    private val _state = MutableStateFlow(CharacterDetailState())
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: CharacterDetailEvent) {
        when (event) {
            CharacterDetailEvent.OnLoadingClick -> showError()
            CharacterDetailEvent.OnRetryClick -> loadData()
            CharacterDetailEvent.OnFavoriteClick -> toggleFavorite()
            CharacterDetailEvent.OnDeleteClick -> {
                _state.value = _state.value.copy(showDeleteDialog = true)
            }
            CharacterDetailEvent.OnDeleteDismiss -> {
                _state.value = _state.value.copy(showDeleteDialog = false)
            }
            CharacterDetailEvent.OnDeleteConfirm -> deleteCharacter()
        }
    }

    private fun loadData() {
        _state.value = CharacterDetailState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            repository.getCharacter(characterId).collect { character ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    data = character
                )
            }
        }
    }

    private fun toggleFavorite() {
        val character = _state.value.data ?: return
        viewModelScope.launch {
            repository.setFavorite(character.id, !character.isFavorite)
        }
    }

    private fun deleteCharacter() {
        loadJob?.cancel()
        viewModelScope.launch {
            repository.delete(characterId)
            _state.value = _state.value.copy(showDeleteDialog = false, isDeleted = true)
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = CharacterDetailState(isLoading = false, hasError = true)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CharacterDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = appContainer.characterRepository
                )
            }
        }
    }
}
