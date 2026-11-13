package plat.lab1.lab7_25213.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import plat.lab1.lab7_25213.data.remote.dto.LocationsResponseDTO

class KtorLocationApi(
    private val client: HttpClient
) : LocationApi {

    override suspend fun getLocations(page: Int): LocationsResponseDTO {
        return client.get("location") {
            parameter("page", page)
        }.body()
    }
}
