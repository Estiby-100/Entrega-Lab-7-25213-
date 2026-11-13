package plat.lab1.lab7_25213.data.repository

import kotlinx.coroutines.flow.Flow
import plat.lab1.lab7_25213.data.model.Character

interface CharacterRepository {
    fun getCharacters(): Flow<List<Character>>
    fun getCharacter(id: Int): Flow<Character?>
    suspend fun sync()
    suspend fun create(character: Character)
    suspend fun update(character: Character)
    suspend fun delete(id: Int)
    suspend fun setFavorite(id: Int, isFavorite: Boolean)
    suspend fun clear()
}
