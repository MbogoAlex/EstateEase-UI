package com.example.tenant_care.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.tenant_care.pManagerViews.PManagerHomeScreenDestination
import com.example.tenant_care.pManagerViews.PManagerLoginScreen
import com.example.tenant_care.pManagerViews.PManagerLoginScreenDestination
import com.example.tenant_care.pManagerViews.rentPayment.RentPaymentsComposable
import com.example.tenant_care.pManagerViews.rentPayment.RentPaymentsComposableDestination
import com.example.tenant_care.pManagerViews.rentPayment.SingleTenantPaymentDetailsComposable
import com.example.tenant_care.pManagerViews.rentPayment.SingleTenantPaymentDetailsComposableDestination
import com.example.tenant_care.pManagerViews.unitsManagementViews.OccupiedUnitDetailsComposable
import com.example.tenant_care.pManagerViews.unitsManagementViews.OccupiedUnitDetailsComposableDestination
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnOccupiedUnitDetailsComposableDestination
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnitsManagementComposable
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnitsManagementComposableDestination
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnoccupiedUnitDetailsComposable
import com.example.tenant_care.tenantViews.TenantHomeScreenComposable
import com.example.tenant_care.tenantViews.TenantHomeScreenDestination
import com.example.tenant_care.tenantViews.accountManagement.TenantLoginScreenComposable
import com.example.tenant_care.tenantViews.accountManagement.TenantLoginScreenDestination
import com.example.tenant_care.tenantViews.rentPayment.PaymentsReportScreenComposable
import com.example.tenant_care.tenantViews.rentPayment.PaymentsReportScreenDestination
import com.example.tenant_care.tenantViews.rentPayment.RentInvoiceScreenComposable
import com.example.tenant_care.tenantViews.rentPayment.RentInvoiceScreenDestination

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
                },
                navigateToTenantHomeScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(TenantHomeScreenDestination.route)
                }
            )
        }
        composable(HomeScreenDestination.route) {
            HomeScreen(
                navigateToPManagerLoginScreen = {
                    navController.popBackStack(HomeScreenDestination.route, true)
                    navController.navigate(PManagerLoginScreenDestination.route)
                },
                navigateToTenantLoginScreen = {
                    navController.popBackStack(HomeScreenDestination.route, true)
                    navController.navigate(TenantLoginScreenDestination.route)
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
                    navController.navigate(UnitsManagementComposableDestination.route)
                },
                navigateToRentPaymentsScreen = {
                    navController.navigate(RentPaymentsComposableDestination.route)
                }
            )
        }
        composable(UnitsManagementComposableDestination.route) {
            UnitsManagementComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToUnoccupiedPropertyDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToOccupiedUnitDetailsScreen = {
                    navController.navigate("${OccupiedUnitDetailsComposableDestination.route}/${it}")
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
                    navController.navigate(UnitsManagementComposableDestination.route)
                }
            )
        }
        composable(
            OccupiedUnitDetailsComposableDestination.routeWithArgs,
            arguments = listOf(
                navArgument(OccupiedUnitDetailsComposableDestination.propertyId) {
                    type = NavType.StringType
                }
            )
        ) {
            OccupiedUnitDetailsComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToUnoccupiedUnits = {
                    navController.popBackStack(OccupiedUnitDetailsComposableDestination.routeWithArgs, true)
                    navController.navigate("${UnitsManagementComposableDestination.route}/${it}")
                }
            )
        }
        composable(
            UnitsManagementComposableDestination.routeWithArgs,
            arguments = listOf(
                navArgument(UnitsManagementComposableDestination.childScreen) {
                    type = NavType.StringType
                }
            )
        ) {
            UnitsManagementComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToUnoccupiedPropertyDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToOccupiedUnitDetailsScreen = {
                    navController.navigate("${OccupiedUnitDetailsComposableDestination.route}/${it}")
                }
            )
        }
        composable(RentPaymentsComposableDestination.route) {
            RentPaymentsComposable(
                navigateToSingleTenantPaymentDetails = {
                    navController.navigate("${SingleTenantPaymentDetailsComposableDestination.route}/${it}")
                }
            )
        }
        composable(
            SingleTenantPaymentDetailsComposableDestination.routeWithArgs,
            arguments = listOf(
                navArgument(SingleTenantPaymentDetailsComposableDestination.tenantId) {
                    type = NavType.StringType
                }
            )
        ) {
            SingleTenantPaymentDetailsComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(TenantLoginScreenDestination.route) {
            TenantLoginScreenComposable(
                navigateToTenantHomeScreen = {
                    navController.popBackStack(TenantLoginScreenDestination.route, true)
                    navController.navigate(TenantHomeScreenDestination.route)
                }
            )
        }
        composable(TenantHomeScreenDestination.route) {
            TenantHomeScreenComposable(
                navigateToRentInvoiceScreen = {tenantId, month, year ->
                    navController.navigate("${RentInvoiceScreenDestination.route}/${tenantId}/${month}/${year}")
                },
                navigateToTenantReportScreen = {
                    navController.navigate(PaymentsReportScreenDestination.route)
                }
            )
        }
        composable(
            RentInvoiceScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(RentInvoiceScreenDestination.tenantId) {
                    type = NavType.StringType
                },
                navArgument(RentInvoiceScreenDestination.month) {
                    type = NavType.StringType
                },
                navArgument(RentInvoiceScreenDestination.year) {
                    type = NavType.StringType
                }
            )
        ) {
            RentInvoiceScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToTenantHomeScreen = {
                    navController.popBackStack(RentInvoiceScreenDestination.routeWithArgs, true)
                    navController.navigate(TenantHomeScreenDestination.route)
                }
            )
        }
        composable(PaymentsReportScreenDestination.route) {
            PaymentsReportScreenComposable(
                navigateToRentInvoiceScreen = {tenantId, month, year ->
                    navController.navigate("${RentInvoiceScreenDestination.route}/${tenantId}/${month}/${year}")
                }
            )
        }
    }
}