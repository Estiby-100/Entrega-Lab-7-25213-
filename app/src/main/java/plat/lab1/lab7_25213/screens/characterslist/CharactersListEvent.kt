package plat.lab1.lab7_25213.screens.characterslist

sealed interface CharactersListEvent {
    data object OnLoadingClick : CharactersListEvent
    data object OnRetryClick : CharactersListEvent
}
