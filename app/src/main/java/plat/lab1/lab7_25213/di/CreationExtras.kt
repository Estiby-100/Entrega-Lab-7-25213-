package plat.lab1.lab7_25213.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import plat.lab1.lab7_25213.RickAndMortyApp

val CreationExtras.appContainer: AppContainer
    get() = (this[APPLICATION_KEY] as RickAndMortyApp).container
