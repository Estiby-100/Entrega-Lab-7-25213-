package plat.lab1.lab7_25213.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationsResponseDTO(
    val info: InfoDTO,
    val results: List<LocationDTO>
)
