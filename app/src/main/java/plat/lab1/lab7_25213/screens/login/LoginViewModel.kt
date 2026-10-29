package plat.lab1.lab7_25213.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.lab7_25213.data.CharacterDb
import plat.lab1.lab7_25213.data.LocationDb
import plat.lab1.lab7_25213.data.UserPreferences
import plat.lab1.lab7_25213.data.local.AppDatabase
import plat.lab1.lab7_25213.data.local.toEntity

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)
    private val database = AppDatabase.getInstance(application)

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
            _state.value = _state.value.copy(isLoading = true)
            userPreferences.saveName(name)
            syncData()
            _state.value = _state.value.copy(isLoading = false, isLoggedIn = true)
        }
    }

    private suspend fun syncData() {
        if (database.characterDao().count() > 0) return

        delay(4000)
        database.characterDao().insertAll(
            CharacterDb().getAllCharacters().map { it.toEntity() }
        )
        database.locationDao().insertAll(
            LocationDb().getAllLocations().map { it.toEntity() }
        )
    }
}
