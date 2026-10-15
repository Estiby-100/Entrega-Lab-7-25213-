package plat.lab1.lab7_25213.screens.profile

sealed interface ProfileEvent {
    data object OnLogoutClick : ProfileEvent
}
