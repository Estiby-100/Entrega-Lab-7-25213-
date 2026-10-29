package plat.lab1.lab7_25213.screens.login

data class LoginState(
    val name: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)
