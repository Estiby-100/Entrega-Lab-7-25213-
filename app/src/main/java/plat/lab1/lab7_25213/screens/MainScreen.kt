package plat.lab1.lab7_25213.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab1.lab7_25213.navigation.CharacterDetailDestination
import plat.lab1.lab7_25213.navigation.CharactersGraph
import plat.lab1.lab7_25213.navigation.CharactersListDestination
import plat.lab1.lab7_25213.navigation.LocationDetailDestination
import plat.lab1.lab7_25213.navigation.LocationsGraph
import plat.lab1.lab7_25213.navigation.LocationsListDestination
import plat.lab1.lab7_25213.navigation.ProfileDestination

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AppBottomBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<CharactersGraph>(startDestination = CharactersListDestination) {
                composable<CharactersListDestination> {
                    CharactersListScreen(
                        onCharacterClick = { characterId ->
                            bottomNavController.navigate(
                                CharacterDetailDestination(characterId = characterId)
                            )
                        }
                    )
                }
                composable<CharacterDetailDestination> { backStackEntry ->
                    val destination: CharacterDetailDestination = backStackEntry.toRoute()
                    CharacterDetailScreen(
                        characterId = destination.characterId,
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }
            }

            navigation<LocationsGraph>(startDestination = LocationsListDestination) {
                composable<LocationsListDestination> {
                    LocationsListScreen(
                        onLocationClick = { locationId ->
                            bottomNavController.navigate(
                                LocationDetailDestination(locationId = locationId)
                            )
                        }
                    )
                }
                composable<LocationDetailDestination> { backStackEntry ->
                    val destination: LocationDetailDestination = backStackEntry.toRoute()
                    LocationDetailScreen(
                        locationId = destination.locationId,
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }
            }

            composable<ProfileDestination> {
                ProfileScreen(onLogoutClick = onLogout)
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(CharactersGraph::class)
            } == true,
            onClick = { navigateToTab(navController, CharactersGraph) },
            icon = { Icon(Icons.Filled.Face, contentDescription = "Characters") },
            label = { Text("Characters") }
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(LocationsGraph::class)
            } == true,
            onClick = { navigateToTab(navController, LocationsGraph) },
            icon = { Icon(Icons.Filled.Place, contentDescription = "Locations") },
            label = { Text("Locations") }
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(ProfileDestination::class)
            } == true,
            onClick = { navigateToTab(navController, ProfileDestination) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

private fun navigateToTab(navController: NavHostController, destination: Any) {
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
