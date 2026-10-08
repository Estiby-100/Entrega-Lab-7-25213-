package plat.lab1.lab7_25213.screens.locationslist

import plat.lab1.lab7_25213.data.Location

data class LocationsListState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)
