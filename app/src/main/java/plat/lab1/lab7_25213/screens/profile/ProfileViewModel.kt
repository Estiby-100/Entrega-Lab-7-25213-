package plat.lab1.lab7_25213.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.UserPreferences
import plat.lab1.lab7_25213.data.repository.CharacterRepository
import plat.lab1.lab7_25213.data.repository.LocationRepository
import plat.lab1.lab7_25213.di.appContainer

class ProfileViewModel(
    private val userPreferences: UserPreferences,
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val name = userPreferences.name.first() ?: ""
            _state.value = _state.value.copy(name = name)
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnLogoutClick -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            userPreferences.clearName()
            characterRepository.clear()
            locationRepository.clear()
            _state.value = _state.value.copy(isLoggedOut = true)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProfileViewModel(
                    userPreferences = appContainer.userPreferences,
                    characterRepository = appContainer.characterRepository,
                    locationRepository = appContainer.locationRepository
                )
            }
        }
    }
}
