package com.example.tenant_care.ui.screens.pManagerViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.screens.caretakerViews.sideBarMenuItems
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.RentPaymentsInfoHomeScreenComposable
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnitsManagementComposable
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.PManagerViewSideBarMenuItem
import com.example.tenant_care.util.PManagerViewSideBarMenuScreen
import com.example.tenant_care.util.ReusableFunctions
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object PManagerHomeScreenDestination: AppNavigation {
    override val title: String = "PManager Home Screen"
    override val route: String = "pmanager-home-screen"

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PManagerHomeComposable(
    navigateToUnitsManagementScreen: () -> Unit,
    navigateToRentPaymentsScreen: () -> Unit,
    navigateToUnoccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
) {
    val viewModel: PManagerHomeScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val menuItems = listOf(
        PManagerViewSideBarMenuItem(
            title = "Rent Payments Info",
            icon = R.drawable.paymets,
            screen = PManagerViewSideBarMenuScreen.RENT_PAYMENTS_INFO,
            color = Color.Gray
        ),
        PManagerViewSideBarMenuItem(
            title = "Units Management",
            icon = R.drawable.apartment,
            screen = PManagerViewSideBarMenuScreen.UNITS_MANAGEMENT,
            color = Color.Gray
        ),
        PManagerViewSideBarMenuItem(
            title = "Notifications",
            icon = R.drawable.message,
            screen = PManagerViewSideBarMenuScreen.NOTIFICATIONS,
            color = Color.Gray
        ),
        PManagerViewSideBarMenuItem(
            title = "Amenities",
            icon = R.drawable.amenity,
            screen = PManagerViewSideBarMenuScreen.NOTIFICATIONS,
            color = Color.Gray
        ),
    )

    var currentScreen by rememberSaveable {
        mutableStateOf(PManagerViewSideBarMenuScreen.RENT_PAYMENTS_INFO)
    }

    Box {
        PManagerHomeScreen(
            pManagerName = uiState.userDSDetails.fullName,
            menuItems = menuItems,
            currentScreen = currentScreen,
            uiState = uiState,
            onChangeScreen = {
                currentScreen = it
            },
            navigateToUnitsManagementScreen = navigateToUnitsManagementScreen,
            navigateToRentPaymentsScreen = navigateToRentPaymentsScreen,
            navigateToUnoccupiedUnitDetailsScreen = navigateToUnoccupiedUnitDetailsScreen,
            navigateToOccupiedUnitDetailsScreen = navigateToOccupiedUnitDetailsScreen,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PManagerHomeScreen(
    pManagerName: String,
    menuItems: List<PManagerViewSideBarMenuItem>,
    currentScreen: PManagerViewSideBarMenuScreen,
    uiState: PManagerHomeScreenUiState,
    onChangeScreen: (screen: PManagerViewSideBarMenuScreen) -> Unit,
    navigateToUnitsManagementScreen: () -> Unit,
    navigateToRentPaymentsScreen: () -> Unit,
    navigateToUnoccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                top = 16.dp
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pmanager_house_background),
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(90.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "EstateEase",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                    for(menuItem in menuItems) {
                        NavigationDrawerItem(
                            modifier = Modifier
                                .padding(8.dp),
                            label = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = menuItem.icon),
                                        contentDescription = menuItem.title
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = menuItem.title)
                                }
                            },
                            selected = currentScreen == menuItem.screen,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    onChangeScreen(menuItem.screen)
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    scope.launch {
                        if(drawerState.isClosed) drawerState.open() else drawerState.close()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Navigation menu"
                    )

                }
                Text(
                    text = pManagerName,
                    fontWeight = FontWeight.Bold
                )
            }
            when(currentScreen) {
                PManagerViewSideBarMenuScreen.RENT_PAYMENTS_INFO -> {
                    RentPaymentsInfoHomeScreenComposable(
                        navigateToRentPaymentsScreen = navigateToRentPaymentsScreen
                    )
                }
                PManagerViewSideBarMenuScreen.UNITS_MANAGEMENT -> {
                    UnitsManagementComposable(
                        navigateToPreviousScreen = navigateToPreviousScreen,
                        navigateToUnoccupiedUnitDetailsScreen = navigateToUnoccupiedUnitDetailsScreen,
                        navigateToOccupiedUnitDetailsScreen = navigateToOccupiedUnitDetailsScreen
                    )
                }
                PManagerViewSideBarMenuScreen.NOTIFICATIONS -> {}
                PManagerViewSideBarMenuScreen.AMENITIES -> {}
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PManagerHomeScreenPreview() {
    val menuItems = listOf(
        PManagerViewSideBarMenuItem(
            title = "Rent Payments Info",
            icon = R.drawable.paymets,
            screen = PManagerViewSideBarMenuScreen.RENT_PAYMENTS_INFO,
            color = Color.Gray
        ),
        PManagerViewSideBarMenuItem(
            title = "Units Management",
            icon = R.drawable.apartment,
            screen = PManagerViewSideBarMenuScreen.UNITS_MANAGEMENT,
            color = Color.Gray
        ),
        PManagerViewSideBarMenuItem(
            title = "Notifications",
            icon = R.drawable.message,
            screen = PManagerViewSideBarMenuScreen.NOTIFICATIONS,
            color = Color.Gray
        ),
        PManagerViewSideBarMenuItem(
            title = "Amenities",
            icon = R.drawable.amenity,
            screen = PManagerViewSideBarMenuScreen.NOTIFICATIONS,
            color = Color.Gray
        ),
    )
    Tenant_careTheme {
        PManagerHomeScreen(
            pManagerName = "Alex M",
            menuItems = menuItems,
            currentScreen = PManagerViewSideBarMenuScreen.RENT_PAYMENTS_INFO,
            onChangeScreen = {},
            navigateToUnitsManagementScreen = {},
            uiState = PManagerHomeScreenUiState(),
            navigateToUnoccupiedUnitDetailsScreen = {},
            navigateToRentPaymentsScreen = {},
            navigateToOccupiedUnitDetailsScreen = {},
            navigateToPreviousScreen = {}
        )
    }
}