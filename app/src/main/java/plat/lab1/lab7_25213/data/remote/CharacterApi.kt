package plat.lab1.lab7_25213.data.remote

import plat.lab1.lab7_25213.data.remote.dto.CharactersResponseDTO

interface CharacterApi {
    suspend fun getCharacters(page: Int): CharactersResponseDTO
}
