package plat.lab1.lab7_25213.screens.characterdetail

sealed interface CharacterDetailEvent {
    data object OnLoadingClick : CharacterDetailEvent
    data object OnRetryClick : CharacterDetailEvent
    data object OnFavoriteClick : CharacterDetailEvent
}
