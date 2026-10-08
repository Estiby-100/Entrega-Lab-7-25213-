package plat.lab1.lab7_25213.screens.locationdetail

sealed interface LocationDetailEvent {
    data object OnLoadingClick : LocationDetailEvent
    data object OnRetryClick : LocationDetailEvent
}
