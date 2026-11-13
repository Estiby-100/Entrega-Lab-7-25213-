package plat.lab1.lab7_25213.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab1.lab7_25213.data.local.CharacterDao
import plat.lab1.lab7_25213.data.model.Character
import plat.lab1.lab7_25213.data.remote.CharacterApi
import plat.lab1.lab7_25213.data.toEntity
import plat.lab1.lab7_25213.data.toModel

class CharacterRepositoryImpl(
    private val api: CharacterApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override fun getCharacters(): Flow<List<Character>> {
        return dao.getAll().map { entities -> entities.map { it.toModel() } }
    }

    override fun getCharacter(id: Int): Flow<Character?> {
        return dao.getById(id).map { it?.toModel() }
    }

    override suspend fun sync() {
        if (dao.count() > 0) return

        val firstPage = api.getCharacters(page = 1)
        val characters = firstPage.results.toMutableList()
        for (page in 2..firstPage.info.pages) {
            characters += api.getCharacters(page).results
        }
        dao.insertAll(characters.map { it.toEntity() })
    }

    override suspend fun create(character: Character) {
        dao.insert(character.copy(id = 0).toEntity())
    }

    override suspend fun update(character: Character) {
        dao.update(character.toEntity())
    }

    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }

    override suspend fun clear() {
        dao.deleteAll()
    }
}
