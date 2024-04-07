package com.example.tenant_care.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tenant_care.HomeScreen
import com.example.tenant_care.HomeScreenDestination
import com.example.tenant_care.SplashScreen
import com.example.tenant_care.SplashScreenDestination
import com.example.tenant_care.pManagerViews.PManagerAddUnitScreen
import com.example.tenant_care.pManagerViews.PManagerAddUnitScreenDestination
import com.example.tenant_care.pManagerViews.PManagerHomeScreen
import com.example.tenant_care.pManagerViews.PManagerHomeScreenDestination
import com.example.tenant_care.pManagerViews.PManagerLoginScreen
import com.example.tenant_care.pManagerViews.PManagerLoginScreenDestination

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreenDestination.route,
        modifier = modifier
    ) {
        composable(SplashScreenDestination.route) {
            SplashScreen(
                navigateToHomeScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(HomeScreenDestination.route)
                },
                navigateToPManagerHomeScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(PManagerHomeScreenDestination.route)
                }
            )
        }
        composable(HomeScreenDestination.route) {
            HomeScreen(
                navigateToPManagerLoginScreen = {
                    navController.popBackStack(HomeScreenDestination.route, true)
                    navController.navigate(PManagerLoginScreenDestination.route)
                }
            )
        }
        composable(PManagerLoginScreenDestination.route) {
            PManagerLoginScreen(
                navigateBackToHomeScreen = {
                    navController.popBackStack(PManagerLoginScreenDestination.route, true)
                    navController.navigate(HomeScreenDestination.route)
                },
                navigateToPManagerHomeScreen = {
                    navController.popBackStack(PManagerLoginScreenDestination.route, true)
                    navController.navigate(PManagerHomeScreenDestination.route)
                }
            )
        }
        composable(PManagerHomeScreenDestination.route) {
            PManagerHomeScreen(
                navigateToAddUnitScreen = {
                    navController.navigate(PManagerAddUnitScreenDestination.route)
                }
            )
        }
        composable(PManagerAddUnitScreenDestination.route) {
            PManagerAddUnitScreen(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
    }
}