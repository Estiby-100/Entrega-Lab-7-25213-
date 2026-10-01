package plat.lab1.lab7_25213.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import plat.lab1.lab7_25213.screens.LoginScreen
import plat.lab1.lab7_25213.screens.MainScreen

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
                    navController.navigate(MainDestination) {
                        popUpTo(LoginDestination) { inclusive = true }
                    }
                }
            )
        }

        composable<MainDestination> {
            MainScreen(
                onLogout = {
                    navController.navigate(LoginDestination) {
                        popUpTo(MainDestination) { inclusive = true }
                    }
                }
            )
        }
    }
}
