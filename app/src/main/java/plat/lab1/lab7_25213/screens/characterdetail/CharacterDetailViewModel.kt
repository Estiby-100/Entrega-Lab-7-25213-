package plat.lab1.lab7_25213.screens.characterdetail

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
import plat.lab1.lab7_25213.data.CharacterDb
import plat.lab1.lab7_25213.navigation.CharacterDetailDestination

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
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
        }
    }

    private fun loadData() {
        _state.value = CharacterDetailState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            _state.value = CharacterDetailState(
                isLoading = false,
                data = CharacterDb().getCharacterById(characterId)
            )
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = CharacterDetailState(isLoading = false, hasError = true)
    }
}
