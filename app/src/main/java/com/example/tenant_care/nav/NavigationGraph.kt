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
import com.example.tenant_care.ui.screens.account.LoginScreenComposable
import com.example.tenant_care.ui.screens.account.LoginScreenDestination
import com.example.tenant_care.ui.screens.caretakerViews.CaretakerHomeScreenComposable
import com.example.tenant_care.ui.screens.caretakerViews.CaretakerHomeScreenDestination
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.EditMeterReadingScreenComposable
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.EditMeterReadingScreenDestination
import com.example.tenant_care.ui.screens.pManagerViews.PManagerHomeComposable
import com.example.tenant_care.ui.screens.pManagerViews.PManagerHomeScreenDestination
import com.example.tenant_care.ui.screens.pManagerViews.PManagerLoginScreen
import com.example.tenant_care.ui.screens.pManagerViews.PManagerLoginScreenDestination
import com.example.tenant_care.ui.screens.generalViews.amenity.AmenityDetailsScreenComposable
import com.example.tenant_care.ui.screens.generalViews.amenity.AmenityDetailsScreenDestination
import com.example.tenant_care.ui.screens.generalViews.amenity.EditAmenityComposable
import com.example.tenant_care.ui.screens.generalViews.amenity.EditAmenityScreenDestination
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.RentPaymentsComposable
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.RentPaymentsComposableDestination
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.SingleTenantPaymentDetailsComposable
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.SingleTenantPaymentDetailsComposableDestination
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.OccupiedUnitDetailsComposable
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.OccupiedUnitDetailsComposableDestination
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.PManagerAddUnitComposable
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.PManagerAddUnitScreenDestination
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnOccupiedUnitDetailsComposableDestination
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnitsManagementComposable
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnitsManagementComposableDestination
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnoccupiedUnitDetailsComposable
import com.example.tenant_care.ui.screens.tenantViews.TenantHomeScreenComposable
import com.example.tenant_care.ui.screens.tenantViews.TenantHomeScreenDestination
import com.example.tenant_care.ui.screens.tenantViews.accountManagement.TenantLoginScreenComposable
import com.example.tenant_care.ui.screens.tenantViews.accountManagement.TenantLoginScreenDestination
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.PaymentsReportScreenComposable
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.PaymentsReportScreenDestination
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.RentInvoiceScreenComposable
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.RentInvoiceScreenDestination

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
                navigateToLoginScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(LoginScreenDestination.route)
                },
                navigateToPManagerHomeScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(PManagerHomeScreenDestination.route)
                },
                navigateToTenantHomeScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(TenantHomeScreenDestination.route)
                },
                navigateToCaretakerHomeScreen = {
                    navController.popBackStack(SplashScreenDestination.route, true)
                    navController.navigate(CaretakerHomeScreenDestination.route)
                }
            )
        }
        composable(LoginScreenDestination.route) {
            LoginScreenComposable(
                navigateToPManagerHomeScreen = {
                    navController.popBackStack(LoginScreenDestination.route, true)
                    navController.navigate(PManagerHomeScreenDestination.route)
                },
                navigateToCaretakerHomeScreen = {
                    navController.popBackStack(LoginScreenDestination.route, true)
                    navController.navigate(CaretakerHomeScreenDestination.route)
                },
                navigateToTenantHomeScreen = {
                    navController.popBackStack(LoginScreenDestination.route, true)
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
                navigateToRentPaymentsScreen = { month, year ->
                    navController.navigate("${RentPaymentsComposableDestination.route}/${month}/${year}")
                },
                navigateToUnoccupiedUnitDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToOccupiedUnitDetailsScreen = {
                    navController.navigate("${OccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToAmenityDetailsScreen = {
                    navController.navigate("${AmenityDetailsScreenDestination.route}/${it}")
                },
                navigateToEditAmenityScreen = {
                    navController.navigate(EditAmenityScreenDestination.route)
                },
                navigateToPmanagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                },
                navigateToLoginScreenWithArgs = {roleId, phoneNumber, password ->
                    navController.navigate("${LoginScreenDestination.route}/${roleId}/${phoneNumber}/${password}")
                },
            )
        }
        composable(UnitsManagementComposableDestination.route) {
            UnitsManagementComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToUnoccupiedUnitDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToOccupiedUnitDetailsScreen = {
                    navController.navigate("${OccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToPmanagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
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
                },
                navigateToEditUnitScreen = {
                    navController.navigate("${PManagerAddUnitScreenDestination.route}/${it}")
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
                },
                navigateToEditUnitScreen = {
                    navController.navigate("${PManagerAddUnitScreenDestination.route}/${it}")
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
                navigateToUnoccupiedUnitDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToOccupiedUnitDetailsScreen = {
                    navController.navigate("${OccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToPmanagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                }
            )
        }
        composable(
            RentPaymentsComposableDestination.routeWithArgs,
            arguments = listOf(
                navArgument(RentPaymentsComposableDestination.month) {
                    type = NavType.StringType
                },
                navArgument(RentPaymentsComposableDestination.year) {
                    type = NavType.StringType
                },
                navArgument(RentPaymentsComposableDestination.roomName) {
                    type = NavType.StringType
                },
            )
        ) {
            RentPaymentsComposable(
                navigateToSingleTenantPaymentDetails = {month, year, tenantId, roomName ->
                    navController.navigate("${SingleTenantPaymentDetailsComposableDestination.route}/${month}/${year}/${tenantId}/${roomName}")
                }
            )
        }
        composable(
            SingleTenantPaymentDetailsComposableDestination.routeWithArgs,
            arguments = listOf(
                navArgument(SingleTenantPaymentDetailsComposableDestination.month) {
                    type = NavType.StringType
                },
                navArgument(SingleTenantPaymentDetailsComposableDestination.year) {
                    type = NavType.StringType
                },
                navArgument(SingleTenantPaymentDetailsComposableDestination.tenantId) {
                    type = NavType.StringType
                },
                navArgument(SingleTenantPaymentDetailsComposableDestination.roomName) {
                    type = NavType.StringType
                },
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
        composable(
            TenantLoginScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(TenantLoginScreenDestination.phoneNumber) {
                    type = NavType.StringType
                },
                navArgument(TenantLoginScreenDestination.password) {
                    type = NavType.StringType
                },
            )
        ) {
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
                },
                navigateToLoginScreenWithArgs = {roleId, phoneNumber, password ->
                    navController.navigate("${LoginScreenDestination.route}/${roleId}/${phoneNumber}/${password}")
                },
                navigateToAmenityDetailsScreen = {
                    navController.navigate("${AmenityDetailsScreenDestination.route}/${it}")
                },
                navigateToEditAmenityScreen = {
                    navController.navigate(EditAmenityScreenDestination.route)
                },
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
        composable(CaretakerHomeScreenDestination.route) {
            CaretakerHomeScreenComposable(
                navigateToEditMeterReadingScreen = {month, year, propertyName, meterTableId, childScreen ->
                    navController.navigate("${EditMeterReadingScreenDestination.route}/${month}/${year}/${propertyName}/${meterTableId}/${childScreen}")
                },
                navigateToLoginScreenWithArgs = {roleId, phoneNumber, password ->
                    navController.popBackStack(CaretakerHomeScreenDestination.route, true)
                    navController.navigate("${LoginScreenDestination.route}/${roleId}/${phoneNumber}/${password}")
                }
            )
        }
        composable(
            EditMeterReadingScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(EditMeterReadingScreenDestination.propertyName) {
                    type = NavType.StringType
                },
                navArgument(EditMeterReadingScreenDestination.meterTableId) {
                    type = NavType.StringType
                },
                navArgument(EditMeterReadingScreenDestination.childScreen) {
                    type = NavType.StringType
                },
            )
        ) {
            EditMeterReadingScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToCaretakerHomeScreenWithArgs = {
                    navController.navigate("${CaretakerHomeScreenDestination.route}/${it}")
                }
            )
        }
        composable(
            CaretakerHomeScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CaretakerHomeScreenDestination.childScreen) {
                    type = NavType.StringType
                }
            )
        ) {
            CaretakerHomeScreenComposable(
                navigateToEditMeterReadingScreen = {month, year, propertyName, meterTableId, childScreen ->
                    navController.navigate("${EditMeterReadingScreenDestination.route}/${month}/${year}/${propertyName}/${meterTableId}/${childScreen}")
                },
                navigateToLoginScreenWithArgs = {roleId, phoneNumber, password ->
                    navController.popBackStack(CaretakerHomeScreenDestination.routeWithArgs, true)
                    navController.navigate("${LoginScreenDestination.route}/${roleId}/${phoneNumber}/${password}")
                }
            )
        }
        composable(
            LoginScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(LoginScreenDestination.roleId) {
                    type = NavType.StringType
                },
                navArgument(LoginScreenDestination.phoneNumber) {
                    type = NavType.StringType
                },
                navArgument(LoginScreenDestination.password) {
                    type = NavType.StringType
                },
            )
        ) {
            LoginScreenComposable(
                navigateToPManagerHomeScreen = {
                    navController.popBackStack(LoginScreenDestination.route, true)
                    navController.navigate(PManagerHomeScreenDestination.route)
                },
                navigateToCaretakerHomeScreen = {
                    navController.popBackStack(LoginScreenDestination.route, true)
                    navController.navigate(CaretakerHomeScreenDestination.route)
                },
                navigateToTenantHomeScreen = {
                    navController.popBackStack(LoginScreenDestination.route, true)
                    navController.navigate(TenantHomeScreenDestination.route)
                }
            )
        }
        composable(
            AmenityDetailsScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(AmenityDetailsScreenDestination.amenityId) {
                    type = NavType.StringType
                }
            )
        ) {
            AmenityDetailsScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToEditAmenityScreenWithArgs = {
                    navController.navigate("${EditAmenityScreenDestination.route}/${it}")
                },
                navigateToPManagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                }
            )
        }
        composable(EditAmenityScreenDestination.route) {
            EditAmenityComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToPManagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                }
            )
        }
        composable(
            EditAmenityScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(EditAmenityScreenDestination.amenityId) {
                    type = NavType.StringType
                }
            )
        ) {
            EditAmenityComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToPManagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                }
            )
        }
        composable(
            PManagerHomeScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(PManagerHomeScreenDestination.childScreen) {
                    type = NavType.StringType
                }
            )
        ) {
            PManagerHomeComposable(
                navigateToUnitsManagementScreen = {
                    navController.navigate(UnitsManagementComposableDestination.route)
                },
                navigateToRentPaymentsScreen = { month, year ->
                    navController.navigate("${RentPaymentsComposableDestination.route}/${month}/${year}")
                },
                navigateToUnoccupiedUnitDetailsScreen = {
                    navController.navigate("${UnOccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToOccupiedUnitDetailsScreen = {
                    navController.navigate("${OccupiedUnitDetailsComposableDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToAmenityDetailsScreen = {
                    navController.navigate("${AmenityDetailsScreenDestination.route}/${it}")
                },
                navigateToEditAmenityScreen = {
                    navController.navigate(EditAmenityScreenDestination.route)
                },
                navigateToPmanagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                },
                navigateToLoginScreenWithArgs = {roleId, phoneNumber, password ->
                    navController.navigate("${LoginScreenDestination.route}/${roleId}/${phoneNumber}/${password}")
                },
            )
        }
        composable(
            PManagerAddUnitScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(PManagerAddUnitScreenDestination.unitId) {
                    type = NavType.StringType
                }
            )
        ) {
            PManagerAddUnitComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToPmanagerHomeScreenWithArgs = {
                    navController.navigate("${PManagerHomeScreenDestination.route}/${it}")
                }
            )
        }
    }
}