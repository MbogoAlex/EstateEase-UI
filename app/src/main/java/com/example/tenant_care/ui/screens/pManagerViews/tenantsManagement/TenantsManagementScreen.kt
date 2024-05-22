package com.example.tenant_care.ui.screens.pManagerViews.tenantsManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

enum class NavigationScreens {
    CURRENT_TENANTS,
    ADD_TENANT,
    FORMER_TENANT
}

data class NavigationItem(
    val label: String,
    val icon: Painter,
    val screen: NavigationScreens
)

@Composable
fun TenantsManagementScreen(
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf<NavigationItem>(
        NavigationItem(
            label = "Active",
            icon = painterResource(id = R.drawable.person),
            screen = NavigationScreens.CURRENT_TENANTS
        ),
        NavigationItem(
            label = "Add",
            icon = painterResource(id = R.drawable.person_add),
            screen = NavigationScreens.ADD_TENANT
        ),
        NavigationItem(
            label = "Former",
            icon = painterResource(id = R.drawable.inactive_tenant),
            screen = NavigationScreens.FORMER_TENANT
        ),
    )
    var currentScreen by rememberSaveable {
        mutableStateOf(NavigationScreens.CURRENT_TENANTS)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentScreen) {
            NavigationScreens.CURRENT_TENANTS -> {
                ActiveTenantsScreen(
                    modifier = Modifier
                        .weight(1f)
                )
            }
            NavigationScreens.ADD_TENANT -> {}
            NavigationScreens.FORMER_TENANT -> {}
        }
        BottomNavigationBar(
            navigationItems = navigationItems,
            currentScreen = currentScreen,
            onChangeScreen = {
                currentScreen = it
            }
        )
    }
}

@Composable
fun BottomNavigationBar(
    navigationItems: List<NavigationItem>,
    currentScreen: NavigationScreens,
    onChangeScreen: (newScreen: NavigationScreens) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for(item in navigationItems) {
            NavigationBarItem(
                label = {
                        Text(text = item.label)
                },
                selected = item.screen == currentScreen,
                onClick = { onChangeScreen(item.screen) },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.label
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantsManagementScreenPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        TenantsManagementScreen()
    }
}