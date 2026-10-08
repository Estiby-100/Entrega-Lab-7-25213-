package plat.lab1.lab7_25213.screens.characterdetail

import plat.lab1.lab7_25213.data.Character

data class CharacterDetailState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)
