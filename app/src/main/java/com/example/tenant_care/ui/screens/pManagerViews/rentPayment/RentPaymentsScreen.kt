package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme

object RentPaymentsComposableDestination: AppNavigation {
    override val title: String = "Rent payments screen"
    override val route: String = "rent-payments-screen"
    val month: String = "month"
    val year: String = "year"
    val roomName: String = "year"
    val routeWithArgs: String = "$route/{$month}/{$roomName}"

}

data class RentPaymentsNavigationItem(
    val title: String,
    val screen: RentPaymentsScreen,
    val icon: Painter,

)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentsComposable(
    navigateToSingleTenantPaymentDetails: (month: String, year: String, tenantId: String, roomName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: RentPaymentsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiStatus.collectAsState()

    val currentScreen = uiState.rentPaymentsScreen


    val navigationItems = listOf<RentPaymentsNavigationItem>(
        RentPaymentsNavigationItem(
            title = "All",
            screen = RentPaymentsScreen.ALL_TENANTS,
            icon = painterResource(id = R.drawable.people),
        ),
        RentPaymentsNavigationItem(
            title = "Paid",
            screen = RentPaymentsScreen.PAID,
            icon = painterResource(id = R.drawable.paid),
        ),
        RentPaymentsNavigationItem(
            title = "Unpaid",
            screen = RentPaymentsScreen.NOT_PAID,
            icon = painterResource(id = R.drawable.unpaid),
        ),
    )

    Box() {
        RentPaymentsScreen(
            month = uiState.month!!,
            year = uiState.year!!,
            currentScreen = currentScreen,
            navItems = navigationItems,
            onChangeTab = {
                viewModel.changeScreen(it)
            },
            navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentsScreen(
    month: String,
    year: String,
    navItems: List<RentPaymentsNavigationItem>,
    currentScreen: RentPaymentsScreen,
    onChangeTab: (newScreen: RentPaymentsScreen) -> Unit,
    navigateToSingleTenantPaymentDetails: (roomName: String, tenantId: String, month: String, year: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentScreen) {
            RentPaymentsScreen.ALL_TENANTS -> {
                AllTenantsPaymentsScreenComposable(
                    month = month,
                    year = year,
                    navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            RentPaymentsScreen.PAID -> {
                TenantsPaidScreenComposable(
                    month = month,
                    year = year,
                    navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            RentPaymentsScreen.NOT_PAID -> {
                TenantsNotPaidScreenComposable(
                    month = month,
                    year = year,
                    navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        RentPaymentsBottomNavBar(
            navItems = navItems,
            currentScreen = currentScreen,
            onChangeTab = onChangeTab
        )
    }
}
@Composable
fun RentPaymentsBottomNavBar(
    navItems: List<RentPaymentsNavigationItem>,
    currentScreen: RentPaymentsScreen,
    onChangeTab: (newScreen: RentPaymentsScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for(navItem in navItems) {
            NavigationBarItem(
                label = {
                        Text(text = navItem.title)
                },
                selected = navItem.screen == currentScreen,
                onClick = { onChangeTab(navItem.screen) },
                icon = {
                    Icon(
                        painter = navItem.icon,
                        contentDescription = navItem.title
                    )
                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RentPaymentScreenPreview() {
    val navigationItems = listOf<RentPaymentsNavigationItem>(
        RentPaymentsNavigationItem(
            title = "All",
            screen = RentPaymentsScreen.ALL_TENANTS,
            icon = painterResource(id = R.drawable.people),
        ),
        RentPaymentsNavigationItem(
            title = "Paid",
            screen = RentPaymentsScreen.PAID,
            icon = painterResource(id = R.drawable.paid),
        ),
        RentPaymentsNavigationItem(
            title = "Unpaid",
            screen = RentPaymentsScreen.NOT_PAID,
            icon = painterResource(id = R.drawable.unpaid),
        ),
    )
    val currentScreen = RentPaymentsScreen.ALL_TENANTS
    Tenant_careTheme {
        RentPaymentsScreen(
            month = "JANUARY",
            year = "2024",
            currentScreen = currentScreen,
            navItems = navigationItems,
            onChangeTab = {},
            navigateToSingleTenantPaymentDetails = {roomName, tenantId, month, year ->   }
        )
    }
}