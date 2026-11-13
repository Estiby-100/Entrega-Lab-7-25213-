package plat.lab1.lab7_25213.data.remote

import plat.lab1.lab7_25213.data.remote.dto.LocationsResponseDTO

interface LocationApi {
    suspend fun getLocations(page: Int): LocationsResponseDTO
}
