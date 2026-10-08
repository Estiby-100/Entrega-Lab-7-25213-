package plat.lab1.lab7_25213.screens.characterslist

import plat.lab1.lab7_25213.data.Character

data class CharactersListState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)
