package plat.lab1.lab7_25213.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.UserPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

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
            userPreferences.saveName(name)
            _state.value = _state.value.copy(isLoggedIn = true)
        }
    }
}
