package plat.lab1.lab7_25213.di

import android.content.Context
import plat.lab1.lab7_25213.data.UserPreferences
import plat.lab1.lab7_25213.data.local.AppDatabase
import plat.lab1.lab7_25213.data.remote.HttpClientFactory
import plat.lab1.lab7_25213.data.remote.KtorCharacterApi
import plat.lab1.lab7_25213.data.remote.KtorLocationApi
import plat.lab1.lab7_25213.data.repository.CharacterRepository
import plat.lab1.lab7_25213.data.repository.CharacterRepositoryImpl
import plat.lab1.lab7_25213.data.repository.LocationRepository
import plat.lab1.lab7_25213.data.repository.LocationRepositoryImpl

class AppContainer(context: Context) {
    private val httpClient = HttpClientFactory.create()
    private val database = AppDatabase.getInstance(context)

    val userPreferences = UserPreferences(context)

    val characterRepository: CharacterRepository = CharacterRepositoryImpl(
        api = KtorCharacterApi(httpClient),
        dao = database.characterDao()
    )

    val locationRepository: LocationRepository = LocationRepositoryImpl(
        api = KtorLocationApi(httpClient),
        dao = database.locationDao()
    )
}
