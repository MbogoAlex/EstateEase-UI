package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.CaretakerMeterReadingBottomBarMenuItem
import com.example.tenant_care.util.CaretakerViewMeterReadingBottomBarScreen


@Composable
fun MeterReadingHomeScreenComposable(
    navigateToUploadMeterReadingScreen: () -> Unit,
    navigateToUpdateMeterReadingScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems: List<CaretakerMeterReadingBottomBarMenuItem> = listOf(
        CaretakerMeterReadingBottomBarMenuItem(
            title = "Uploaded",
            icon = R.drawable.done,
            screen = CaretakerViewMeterReadingBottomBarScreen.UPLOADED,
            color = Color.Green
        ),
        CaretakerMeterReadingBottomBarMenuItem(
            title = "Not uploaded",
            icon = R.drawable.not_done,
            screen = CaretakerViewMeterReadingBottomBarScreen.NOT_UPLOADED,
            color = Color.Red
        ),
    )

    var currentTab by rememberSaveable {
        mutableStateOf(CaretakerViewMeterReadingBottomBarScreen.UPLOADED)
    }

    Box {
        MeterReadingHomeScreen(
            currentTab = currentTab,
            menuItems = menuItems,
            onChangeTab = {
                currentTab = it
            },
            navigateToUpdateMeterReadingScreen = navigateToUpdateMeterReadingScreen,
            navigateToUploadMeterReadingScreen = navigateToUploadMeterReadingScreen
        )
    }
}

@Composable
fun MeterReadingHomeScreen(
    currentTab: CaretakerViewMeterReadingBottomBarScreen,
    menuItems: List<CaretakerMeterReadingBottomBarMenuItem>,
    onChangeTab: (tab: CaretakerViewMeterReadingBottomBarScreen) -> Unit,
    navigateToUploadMeterReadingScreen: () -> Unit,
    navigateToUpdateMeterReadingScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentTab) {
            CaretakerViewMeterReadingBottomBarScreen.UPLOADED -> {
                UploadedScreenComposable(
                    navigateToUpdateMeterReadingScreen = navigateToUpdateMeterReadingScreen,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            CaretakerViewMeterReadingBottomBarScreen.NOT_UPLOADED -> {
                NotUploadedScreenComposable(
                    navigateToUploadMeterReadingScreen = navigateToUploadMeterReadingScreen,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        BottomBar(
            menuItems = menuItems,
            currentTab = currentTab,
            onChangeTab = onChangeTab
        )
    }

}

@Composable
fun BottomBar(
    menuItems: List<CaretakerMeterReadingBottomBarMenuItem>,
    currentTab: CaretakerViewMeterReadingBottomBarScreen,
    onChangeTab: (tab: CaretakerViewMeterReadingBottomBarScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for(menuItem in menuItems) {
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = menuItem.color
                ),
                label = {
                    Text(text = menuItem.title)
                },
                selected = menuItem.screen == currentTab,
                onClick = { onChangeTab(menuItem.screen) },
                icon = {
                    Icon(
                        painter = painterResource(id = menuItem.icon),
                        contentDescription = menuItem.title
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeterReadingHomeScreenPreview() {
    val menuItems: List<CaretakerMeterReadingBottomBarMenuItem> = listOf(
        CaretakerMeterReadingBottomBarMenuItem(
            title = "Uploaded",
            icon = R.drawable.done,
            screen = CaretakerViewMeterReadingBottomBarScreen.UPLOADED,
            color = Color.Green
        ),
        CaretakerMeterReadingBottomBarMenuItem(
            title = "Not uploaded",
            icon = R.drawable.not_done,
            screen = CaretakerViewMeterReadingBottomBarScreen.NOT_UPLOADED,
            color = Color.Red
        ),
    )
    var currentTab by rememberSaveable {
        mutableStateOf(CaretakerViewMeterReadingBottomBarScreen.UPLOADED)
    }
    Tenant_careTheme {
        MeterReadingHomeScreen(
            currentTab = CaretakerViewMeterReadingBottomBarScreen.UPLOADED,
            menuItems = menuItems,
            onChangeTab = {
                currentTab = it
            },
            navigateToUploadMeterReadingScreen = {},
            navigateToUpdateMeterReadingScreen = {}
        )
    }
}