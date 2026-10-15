package plat.lab1.lab7_25213.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.UserPreferences

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

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
            _state.value = _state.value.copy(isLoggedOut = true)
        }
    }
}
