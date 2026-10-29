package plat.lab1.lab7_25213.screens.characterdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.local.AppDatabase
import plat.lab1.lab7_25213.data.local.toModel
import plat.lab1.lab7_25213.navigation.CharacterDetailDestination

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val characterId = savedStateHandle.toRoute<CharacterDetailDestination>().characterId
    private val characterDao = AppDatabase.getInstance(application).characterDao()

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
        }
    }

    private fun loadData() {
        _state.value = CharacterDetailState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            characterDao.getById(characterId).collect { entity ->
                _state.value = CharacterDetailState(
                    isLoading = false,
                    data = entity?.toModel()
                )
            }
        }
    }

    private fun toggleFavorite() {
        val character = _state.value.data ?: return
        viewModelScope.launch {
            characterDao.updateFavorite(character.id, !character.isFavorite)
        }
    }

    private fun showError() {
        loadJob?.cancel()
        _state.value = CharacterDetailState(isLoading = false, hasError = true)
    }
}
