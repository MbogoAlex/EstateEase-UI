package com.example.tenant_care.tenantViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.tenant_care.R
import com.example.tenant_care.tenantViews.amenity.AmenityComposable
import com.example.tenant_care.tenantViews.model.BottomNavigationBar
import com.example.tenant_care.tenantViews.rentPayment.PaymentHomeScreenComposable
import com.example.tenant_care.tenantViews.rentPayment.RentStatusCard
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun TenantHomeScreen(
    modifier: Modifier = Modifier
) {
    var currentBottomBar by rememberSaveable {
        mutableStateOf(BottomNavigationBar.HOME)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentBottomBar) {
            BottomNavigationBar.HOME -> PaymentHomeScreenComposable(modifier = Modifier.weight(1f))
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

@Preview(showBackground = true)
@Composable
fun RentStatusCardPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        RentStatusCard()
    }
}

@Preview(showBackground = true)
@Composable
fun TenantHomeScreenPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        TenantHomeScreen()
    }
}
