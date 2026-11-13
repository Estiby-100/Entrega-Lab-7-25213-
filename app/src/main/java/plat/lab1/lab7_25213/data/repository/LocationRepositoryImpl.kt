package plat.lab1.lab7_25213.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab1.lab7_25213.data.local.LocationDao
import plat.lab1.lab7_25213.data.model.Location
import plat.lab1.lab7_25213.data.remote.LocationApi
import plat.lab1.lab7_25213.data.toEntity
import plat.lab1.lab7_25213.data.toModel

class LocationRepositoryImpl(
    private val api: LocationApi,
    private val dao: LocationDao
) : LocationRepository {

    override fun getLocations(): Flow<List<Location>> {
        return dao.getAll().map { entities -> entities.map { it.toModel() } }
    }

    override fun getLocation(id: Int): Flow<Location?> {
        return dao.getById(id).map { it?.toModel() }
    }

    override suspend fun sync() {
        if (dao.count() > 0) return

        val firstPage = api.getLocations(page = 1)
        val locations = firstPage.results.toMutableList()
        for (page in 2..firstPage.info.pages) {
            locations += api.getLocations(page).results
        }
        dao.insertAll(locations.map { it.toEntity() })
    }

    override suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }

    override suspend fun clear() {
        dao.deleteAll()
    }
}
