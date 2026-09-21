package plat.lab1.lab7_25213.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab1.lab7_25213.screens.CharacterDetailScreen
import plat.lab1.lab7_25213.screens.CharactersListScreen
import plat.lab1.lab7_25213.screens.LoginScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = modifier.fillMaxSize()
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onEmpezarClick = {
                    navController.navigate(CharactersListDestination) {
                        popUpTo(LoginDestination) { inclusive = true }
                    }
                }
            )
        }

        composable<CharactersListDestination> {
            CharactersListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(
                        CharacterDetailDestination(characterId = characterId)
                    )
                }
            )
        }

        composable<CharacterDetailDestination> { backStackEntry ->
            val destination: CharacterDetailDestination = backStackEntry.toRoute()
            CharacterDetailScreen(
                characterId = destination.characterId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}