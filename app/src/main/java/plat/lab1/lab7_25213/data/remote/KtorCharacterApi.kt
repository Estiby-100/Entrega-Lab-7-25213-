package plat.lab1.lab7_25213.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import plat.lab1.lab7_25213.data.remote.dto.CharactersResponseDTO

class KtorCharacterApi(
    private val client: HttpClient
) : CharacterApi {

    override suspend fun getCharacters(page: Int): CharactersResponseDTO {
        return client.get("character") {
            parameter("page", page)
        }.body()
    }
}
