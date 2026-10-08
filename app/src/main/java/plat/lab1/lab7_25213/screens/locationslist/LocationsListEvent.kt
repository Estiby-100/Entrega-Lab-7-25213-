package plat.lab1.lab7_25213.screens.locationslist

sealed interface LocationsListEvent {
    data object OnLoadingClick : LocationsListEvent
    data object OnRetryClick : LocationsListEvent
}
