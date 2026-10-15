package plat.lab1.lab7_25213.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import plat.lab1.lab7_25213.screens.MainScreen
import plat.lab1.lab7_25213.screens.login.LoginScreen
import plat.lab1.lab7_25213.screens.splash.SplashScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashDestination,
        modifier = modifier.fillMaxSize()
    ) {
        composable<SplashDestination> {
            SplashScreen(
                onLoggedIn = {
                    navController.navigate(MainDestination) {
                        popUpTo(SplashDestination) { inclusive = true }
                    }
                },
                onNotLoggedIn = {
                    navController.navigate(LoginDestination) {
                        popUpTo(SplashDestination) { inclusive = true }
                    }
                }
            )
        }

        composable<LoginDestination> {
            LoginScreen(
                onLoginSuccess = {
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
