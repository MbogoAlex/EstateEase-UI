package com.example.tenant_care.ui.screens.tenantViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.screens.tenantViews.amenity.AmenityComposable
import com.example.tenant_care.ui.screens.tenantViews.model.BottomNavigationBar
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.PaymentHomeScreenComposable
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.RentStatusCard
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.TenantSideBarMenuItem
import com.example.tenant_care.util.TenantViewSidebarMenuScreen
import kotlinx.coroutines.launch

object TenantHomeScreenDestination: AppNavigation {
    override val title: String = "Tenant home screen"
    override val route: String = "tenant-home-screen"

}

val sideBarMenuItems = listOf<TenantSideBarMenuItem>(
    TenantSideBarMenuItem(
        title = "Home screen",
        icon = R.drawable.home,
        screen = TenantViewSidebarMenuScreen.HOME_SCREEN,
        color = Color.Gray
    ),
    TenantSideBarMenuItem(
        title = "Logout",
        icon = R.drawable.logout,
        screen = TenantViewSidebarMenuScreen.LOGIN_SCREEN,
        color = Color.Gray
    )
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantHomeScreenComposable(
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    navigateToTenantReportScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, password: String) -> Unit,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: TenantHomeScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var logoutAlert by remember {
        mutableStateOf(false)
    }

    if(logoutAlert) {
        LogoutAlert(
            onDismiss = { logoutAlert = !logoutAlert },
            onLogout = {

                navigateToHomeScreen()
//                navigateToTenantLoginScreenWithArgs(
//                    uiState.userDetails.room,
//                    uiState.userDetails.phoneNumber,
//                    uiState.userDetails.password
//                )
                logoutAlert = !logoutAlert
                viewModel.logout()
            }
        )
    }

    Box {
        TenantHomeScreen(
            sideBarMenuItems = sideBarMenuItems,
            navigateToRentInvoiceScreen = navigateToRentInvoiceScreen,
            navigateToTenantReportScreen = navigateToTenantReportScreen,
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
            onLogout = {
                logoutAlert = true
            }
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantHomeScreen(
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    navigateToTenantReportScreen: () -> Unit,
    sideBarMenuItems: List<TenantSideBarMenuItem>,
    navigateToLoginScreenWithArgs: (phoneNumber: String, password: String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentBottomBar by rememberSaveable {
        mutableStateOf(BottomNavigationBar.HOME)
    }

    var currentSideBarMenu by rememberSaveable {
        mutableStateOf(TenantViewSidebarMenuScreen.HOME_SCREEN)
    }

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
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        for(menuItem in sideBarMenuItems) {
                            NavigationDrawerItem(
                                modifier = Modifier
                                    .padding(8.dp),
                                label = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            tint = menuItem.color,
                                            painter = painterResource(id = menuItem.icon),
                                            contentDescription = menuItem.title
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = menuItem.title)
                                    }
                                },
                                selected = menuItem.screen == currentSideBarMenu,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        currentSideBarMenu = menuItem.screen
                                    }

                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            if(drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Sidebar menu"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "EstateEase",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            // sidebar menu
            when(currentSideBarMenu) {
                TenantViewSidebarMenuScreen.HOME_SCREEN -> {
                    currentBottomBar = currentBottomBar
                }
                TenantViewSidebarMenuScreen.LOGIN_SCREEN -> {
                    onLogout()
                    currentSideBarMenu = TenantViewSidebarMenuScreen.HOME_SCREEN
                }
            }

            // bottombar menu
            when(currentBottomBar) {
                BottomNavigationBar.HOME -> PaymentHomeScreenComposable(
                    navigateToRentInvoiceScreen = navigateToRentInvoiceScreen,
                    navigateToTenantReportScreen = navigateToTenantReportScreen,
                    modifier = Modifier.weight(1f)
                )
                BottomNavigationBar.CHAT -> {
//                TenantReportScreen(
//                    modifier = Modifier.weight(1f)
//                )
                }
                BottomNavigationBar.SERVICES -> {
                    AmenityComposable(
                        modifier = Modifier
                            .weight(1f)
                    )
//                TenantsNotificationsScreen(
//                    modifier = Modifier
//                        .weight(1f)
//                )
                }

            }
            TenantViewBottomNavigationBar(
                onSelected = {currentBottomBar = it},
                currentBottomBar = currentBottomBar,
                modifier = Modifier
//                .weight(1f)
            )
        }
    }

}



@Composable
fun TenantViewBottomNavigationBar(
    onSelected: (bottomBarNavigation: BottomNavigationBar) -> Unit,
    currentBottomBar: BottomNavigationBar,
    modifier: Modifier = Modifier
) {
    val navItems = listOf<NavigationContentItem>(
        NavigationContentItem(
            name = "Home",
            icon = painterResource(id = R.drawable.home),
            bottomNavigationBar = BottomNavigationBar.HOME,
            description = "Navigate to home"
        ),
        NavigationContentItem(
            name = "Chat",
            icon = painterResource(id = R.drawable.chat),
            bottomNavigationBar = BottomNavigationBar.CHAT,
            description = "Navigate to chat screen"
        ),
        NavigationContentItem(
            name = "Amenities",
            icon = painterResource(id = R.drawable.business),
            bottomNavigationBar = BottomNavigationBar.SERVICES,
            description = "Navigate to amenities"
        ),
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (navItem in navItems) {
            NavigationBarItem(
                selected = navItem.bottomNavigationBar == currentBottomBar,
                onClick = {
                    onSelected(navItem.bottomNavigationBar)
                },
                label = {
                        Text(text = navItem.name)
                },
                icon = {
                    Icon(
                        painter = navItem.icon,
                        contentDescription = navItem.description
                    )
                }
            )
        }
    }


}

@Composable
fun LogoutAlert(
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        title = {
            Text(text = "Logout confirmation")
        },
        confirmButton = {
            Button(onClick = onLogout) {
                Text(text = "Log out")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        onDismissRequest = onDismiss
    )
}

private data class NavigationContentItem (
    val name: String,
    val icon: Painter,
    val bottomNavigationBar: BottomNavigationBar,
    val description: String,
)

@Preview(showBackground = true)
@Composable
fun TenantBottomNavigationBarPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        TenantViewBottomNavigationBar(
            onSelected = {},
            currentBottomBar = BottomNavigationBar.HOME
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RentStatusCardPreview(
//    modifier: Modifier = Modifier
//) {
//    Tenant_careTheme {
//        RentStatusCard()
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TenantHomeScreenPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        TenantHomeScreen(
            navigateToRentInvoiceScreen = {tenantId, month, year ->  },
            navigateToTenantReportScreen = {},
            sideBarMenuItems = sideBarMenuItems,
            navigateToLoginScreenWithArgs = {phoneNumber, password ->  },
            onLogout = {}
        )
    }
}
