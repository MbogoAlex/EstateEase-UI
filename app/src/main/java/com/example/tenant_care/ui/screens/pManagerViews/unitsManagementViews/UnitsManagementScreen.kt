package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme



data class NavigationItem (
    val title: String,
    val screen: Screen,
    val icon: Painter,
    val color: Color
)

object UnitsManagementComposableDestination: AppNavigation {
    override val title: String = "Units Management Screen"
    override val route: String = "units-management-screen"
    val childScreen: String = "child-screen"
    val routeWithArgs: String = "$route/{$childScreen}"
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnitsManagementComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToUnoccupiedPropertyDetailsScreen: (propertyId: String) -> Unit,
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: UnitsManagementScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.childScreen == "unoccupied-units") {
        viewModel.changeScreen(
            screen = Screen.UNOCCUPIED_UNITS
        )
        viewModel.resetChildScreen()
    }
    val navigationItems = listOf<NavigationItem>(
        NavigationItem(
            title = "Occupied",
            screen = Screen.OCCUPIED_UNITS,
            icon = painterResource(id = R.drawable.house),
            color = Color.Red

        ),
        NavigationItem(
            title = "Unoccupied",
            screen = Screen.UNOCCUPIED_UNITS,
            icon = painterResource(id = R.drawable.house),
            color = Color.Gray
        ),
        NavigationItem(
            title = "Add New",
            screen = Screen.ADD_UNIT,
            icon = painterResource(id = R.drawable.add_house),
            color = Color.Black
        ),
    )

    UnitsManagementScreen(
        currentScreen = uiState.currentScreen,
        navigationItems = navigationItems,
        navigateToUnoccupiedPropertyDetailsScreen = navigateToUnoccupiedPropertyDetailsScreen,
        navigateToOccupiedUnitDetailsScreen = navigateToOccupiedUnitDetailsScreen,
        onChangeTab = {
            viewModel.changeScreen(it)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnitsManagementScreen(
    currentScreen: Screen,
    navigationItems: List<NavigationItem>,
    onChangeTab: (newScreen: Screen) -> Unit,
    navigateToUnoccupiedPropertyDetailsScreen: (propertyId: String) -> Unit,
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentScreen) {
            Screen.OCCUPIED_UNITS -> {
                OccupiedUnitsComposable(
                    navigateToOccupiedUnitDetailsScreen = navigateToOccupiedUnitDetailsScreen,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Screen.UNOCCUPIED_UNITS -> {
                UnoccupiedUnitsComposable(
                    navigateToUnoccupiedPropertyDetailsScreen = navigateToUnoccupiedPropertyDetailsScreen,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Screen.ADD_UNIT -> {
                PManagerAddUnitComposable(
                    navigateToPreviousScreen = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        BottomNavigationBar(
            navigationItems = navigationItems,
            currentScreen = currentScreen,
            onChangeTab = onChangeTab
        )
    }
}

@Composable
fun BottomNavigationBar(
    navigationItems: List<NavigationItem>,
    currentScreen: Screen,
    onChangeTab: (newScreen: Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        navigationItems.forEach {
            NavigationBarItem(
                label = {
                        Text(text = it.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = it.color,
                    selectedIconColor = it.color
                ),
                selected = currentScreen == it.screen,
                onClick = { onChangeTab(it.screen) },
                icon = {
                    Icon(
                        painter = it.icon,
                        contentDescription = it.title,
                        modifier = Modifier
                    )
                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun UnitsManagementComposablePreview() {
    Tenant_careTheme {
        UnitsManagementComposable(
            navigateToPreviousScreen = {},
            navigateToUnoccupiedPropertyDetailsScreen = {},
            navigateToOccupiedUnitDetailsScreen = {},
        )
    }
}