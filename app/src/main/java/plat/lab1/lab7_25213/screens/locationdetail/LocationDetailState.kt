package plat.lab1.lab7_25213.screens.locationdetail

import plat.lab1.lab7_25213.data.Location

data class LocationDetailState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)
