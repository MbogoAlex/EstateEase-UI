package com.example.tenant_care.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tenant_care.HomeScreen
import com.example.tenant_care.HomeScreenDestination
import com.example.tenant_care.SplashScreen
import com.example.tenant_care.SplashScreenDestination
import com.example.tenant_care.pManagerViews.PManagerHomeComposable
import com.example.tenant_care.pManagerViews.unitsManagementViews.PManagerAddUnitScreen
import com.example.tenant_care.pManagerViews.unitsManagementViews.PManagerAddUnitScreenDestination
import com.example.tenant_care.pManagerViews.PManagerHomeScreen
import com.example.tenant_care.pManagerViews.PManagerHomeScreenDestination
import com.example.tenant_care.pManagerViews.PManagerLoginScreen
import com.example.tenant_care.pManagerViews.PManagerLoginScreenDestination
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnOccupiedUnitDetailsComposableDestination
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnitsManagementComposable
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnoccupiedUnitDetailsComposable

@RequiresApi(Build.VERSION_CODES.O)
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
            PManagerHomeComposable(
                navigateToUnitsManagementScreen = {
                    navController.navigate(UnitsManagementComposable.route)
                }
            )
        }
        composable(UnitsManagementComposable.route) {
            UnitsManagementComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToUnoccupiedPropertyDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                }
            )
        }
        composable(
            UnOccupiedUnitDetailsComposableDestination.routeWithArgs,
            arguments = listOf(
                navArgument(UnOccupiedUnitDetailsComposableDestination.propertyId) {
                    type = NavType.StringType
                }
            )
        ) {
            UnoccupiedUnitDetailsComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToOccupiedUnitsScreen = {
                    navController.navigate(UnitsManagementComposable.route)
                }
            )
        }
    }
}