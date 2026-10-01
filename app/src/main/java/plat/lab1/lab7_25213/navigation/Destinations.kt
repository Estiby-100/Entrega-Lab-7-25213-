package plat.lab1.lab7_25213.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

@Serializable
data object MainDestination

@Serializable
data object CharactersGraph

@Serializable
data object CharactersListDestination

@Serializable
data class CharacterDetailDestination(
    val characterId: Int
)

@Serializable
data object LocationsGraph

@Serializable
data object LocationsListDestination

@Serializable
data class LocationDetailDestination(
    val locationId: Int
)

@Serializable
data object ProfileDestination
