package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.CaretakerManagementBottomBarMenuItem
import com.example.tenant_care.util.CaretakerManagementBottomBarScreen
import com.example.tenant_care.util.CaretakerUnitsBottomBarMenuItem
import com.example.tenant_care.util.CaretakerViewUnitsBottomBarScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CaretakerManagementHomeScreenComposable(
    navigateToCaretakerDetailsScreenWithArgs: (caretakerId: String) -> Unit,
    navigateToHomeScreenWithArgs: (childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf<CaretakerManagementBottomBarMenuItem>(
        CaretakerManagementBottomBarMenuItem(
            title = "Caretakers",
            icon = R .drawable.person,
            screen = CaretakerManagementBottomBarScreen.CARETAKERS,
            color = Color.Gray
        ),
        CaretakerManagementBottomBarMenuItem(
            title = "Add new",
            icon = R .drawable.person_add,
            screen = CaretakerManagementBottomBarScreen.ADD_CARETAKER,
            color = Color.Gray
        ),
    )

    var currentScreen by remember {
        mutableStateOf(CaretakerManagementBottomBarScreen.CARETAKERS)
    }

    Box(
        modifier = modifier
    ) {
        CaretakerManagementHomeScreen(
            menuItems = menuItems,
            currentTab = currentScreen,
            onChangeScreen = {
                currentScreen = it
            },
            navigateToCaretakerDetailsScreenWithArgs = navigateToCaretakerDetailsScreenWithArgs,
            navigateToHomeScreenWithArgs = navigateToHomeScreenWithArgs
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CaretakerManagementHomeScreen(
    menuItems: List<CaretakerManagementBottomBarMenuItem>,
    currentTab: CaretakerManagementBottomBarScreen,
    onChangeScreen: (screen: CaretakerManagementBottomBarScreen) -> Unit,
    navigateToCaretakerDetailsScreenWithArgs: (caretakerId: String) -> Unit,
    navigateToHomeScreenWithArgs: (childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentTab) {
            CaretakerManagementBottomBarScreen.CARETAKERS -> {
                CaretakersScreenComposable(
                    navigateToCaretakerDetailsScreen = navigateToCaretakerDetailsScreenWithArgs,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            CaretakerManagementBottomBarScreen.ADD_CARETAKER -> {
                AddCaretakerScreenComposable(
                    navigateToHomeScreenWithArgs = navigateToHomeScreenWithArgs,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        BottomBar(
            tabs = menuItems,
            currentTab = currentTab,
            onChangeTab = onChangeScreen
        )
    }
}

@Composable
fun BottomBar(
    tabs: List<CaretakerManagementBottomBarMenuItem>,
    currentTab: CaretakerManagementBottomBarScreen,
    onChangeTab: (tab: CaretakerManagementBottomBarScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for (tab in tabs) {
            NavigationBarItem(
                label = {
                    Text(text = tab.title)
                },
                selected = tab.screen == currentTab,
                onClick = { onChangeTab(tab.screen) },
                icon = {
                    Icon(
                        tint = tab.color,
                        painter = painterResource(id = tab.icon),
                        contentDescription = tab.title
                    )
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CaretakerManagementHomeScreenPreview() {
    val menuItems = listOf<CaretakerManagementBottomBarMenuItem>(
        CaretakerManagementBottomBarMenuItem(
            title = "Caretakers",
            icon = R .drawable.person,
            screen = CaretakerManagementBottomBarScreen.CARETAKERS,
            color = Color.Gray
        ),
        CaretakerManagementBottomBarMenuItem(
            title = "Add new",
            icon = R .drawable.person_add,
            screen = CaretakerManagementBottomBarScreen.ADD_CARETAKER,
            color = Color.Gray
        ),
    )
    Tenant_careTheme {
        CaretakerManagementHomeScreen(
            menuItems = menuItems,
            currentTab = CaretakerManagementBottomBarScreen.CARETAKERS,
            onChangeScreen = {},
            navigateToCaretakerDetailsScreenWithArgs = {},
            navigateToHomeScreenWithArgs = {}
        )
    }
}
