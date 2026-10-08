package plat.lab1.lab7_25213.screens.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.CharacterDb

class CharactersListViewModel : ViewModel() {

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
        }
    }

    private fun loadData() {
        _state.value = CharactersListState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(4000)
            _state.value = CharactersListState(
                isLoading = false,
                data = CharacterDb().getAllCharacters()
            )
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = CharactersListState(isLoading = false, hasError = true)
    }
}
