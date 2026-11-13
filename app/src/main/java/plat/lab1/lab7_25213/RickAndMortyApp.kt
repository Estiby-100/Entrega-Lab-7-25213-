package plat.lab1.lab7_25213

import android.app.Application
import plat.lab1.lab7_25213.di.AppContainer

class RickAndMortyApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
