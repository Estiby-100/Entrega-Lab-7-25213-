package plat.lab1.lab7_25213.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

@Serializable
data object CharactersListDestination

@Serializable
data class CharacterDetailDestination(
    val characterId: Int
)