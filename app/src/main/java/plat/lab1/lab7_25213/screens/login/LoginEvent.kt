package plat.lab1.lab7_25213.screens.login

sealed interface LoginEvent {
    data class OnNameChange(val name: String) : LoginEvent
    data object OnLoginClick : LoginEvent
}
