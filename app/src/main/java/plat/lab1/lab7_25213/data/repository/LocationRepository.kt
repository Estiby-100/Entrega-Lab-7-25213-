package plat.lab1.lab7_25213.data.repository

import kotlinx.coroutines.flow.Flow
import plat.lab1.lab7_25213.data.model.Location

interface LocationRepository {
    fun getLocations(): Flow<List<Location>>
    fun getLocation(id: Int): Flow<Location?>
    suspend fun sync()
    suspend fun setFavorite(id: Int, isFavorite: Boolean)
    suspend fun clear()
}
