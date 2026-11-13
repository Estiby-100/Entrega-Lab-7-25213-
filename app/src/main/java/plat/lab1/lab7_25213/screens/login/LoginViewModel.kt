package plat.lab1.lab7_25213.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.UserPreferences
import plat.lab1.lab7_25213.data.repository.CharacterRepository
import plat.lab1.lab7_25213.data.repository.LocationRepository
import plat.lab1.lab7_25213.di.appContainer

class LoginViewModel(
    private val userPreferences: UserPreferences,
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnNameChange -> {
                _state.value = _state.value.copy(name = event.name)
            }
            LoginEvent.OnLoginClick -> login()
        }
    }

    private fun login() {
        val name = _state.value.name.trim()
        if (name.isEmpty()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, hasError = false)
            try {
                characterRepository.sync()
                locationRepository.sync()
                userPreferences.saveName(name)
                _state.value = _state.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, hasError = true)
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                LoginViewModel(
                    userPreferences = appContainer.userPreferences,
                    characterRepository = appContainer.characterRepository,
                    locationRepository = appContainer.locationRepository
                )
            }
        }
    }
}
