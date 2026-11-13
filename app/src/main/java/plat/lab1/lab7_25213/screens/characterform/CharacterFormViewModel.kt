package plat.lab1.lab7_25213.screens.characterform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.model.Character
import plat.lab1.lab7_25213.data.repository.CharacterRepository
import plat.lab1.lab7_25213.di.appContainer
import plat.lab1.lab7_25213.navigation.CharacterFormDestination

class CharacterFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {
    private val characterId = savedStateHandle.toRoute<CharacterFormDestination>().characterId
    private var isFavorite = false

    private val _state = MutableStateFlow(CharacterFormState(isEditing = characterId != 0))
    val state: StateFlow<CharacterFormState> = _state.asStateFlow()

    init {
        if (characterId != 0) {
            viewModelScope.launch {
                val character = repository.getCharacter(characterId).first() ?: return@launch
                isFavorite = character.isFavorite
                _state.value = _state.value.copy(
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    gender = character.gender,
                    image = character.image
                )
            }
        }
    }

    fun onEvent(event: CharacterFormEvent) {
        when (event) {
            is CharacterFormEvent.OnNameChange -> {
                _state.value = _state.value.copy(name = event.name)
            }
            is CharacterFormEvent.OnStatusChange -> {
                _state.value = _state.value.copy(status = event.status)
            }
            is CharacterFormEvent.OnSpeciesChange -> {
                _state.value = _state.value.copy(species = event.species)
            }
            is CharacterFormEvent.OnGenderChange -> {
                _state.value = _state.value.copy(gender = event.gender)
            }
            is CharacterFormEvent.OnImageChange -> {
                _state.value = _state.value.copy(image = event.image)
            }
            CharacterFormEvent.OnSaveClick -> save()
        }
    }

    private fun save() {
        val current = _state.value
        if (current.name.isBlank()) return

        val character = Character(
            id = characterId,
            name = current.name.trim(),
            status = current.status.trim(),
            species = current.species.trim(),
            gender = current.gender.trim(),
            image = current.image.trim(),
            isFavorite = isFavorite
        )

        viewModelScope.launch {
            if (current.isEditing) {
                repository.update(character)
            } else {
                repository.create(character)
            }
            _state.value = _state.value.copy(isSaved = true)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CharacterFormViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = appContainer.characterRepository
                )
            }
        }
    }
}
