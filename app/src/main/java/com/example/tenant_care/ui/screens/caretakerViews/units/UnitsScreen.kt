package com.example.tenant_care.ui.screens.caretakerViews.units

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.CaretakerUnitsBottomBarMenuItem
import com.example.tenant_care.util.CaretakerViewUnitsBottomBarScreen

val bottomBarMenuItems = listOf<CaretakerUnitsBottomBarMenuItem>(
    CaretakerUnitsBottomBarMenuItem(
        title = "Occupied",
        icon = R.drawable.house,
        color = Color.Gray,
        screen = CaretakerViewUnitsBottomBarScreen.OCCUPIED_UNITS
    ),
    CaretakerUnitsBottomBarMenuItem(
        title = "Unoccupied",
        icon = R.drawable.house,
        color = Color.Red,
        screen = CaretakerViewUnitsBottomBarScreen.UNOCCUPIED_UNITS
    )
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnitsScreenComposable(
    modifier: Modifier = Modifier
) {
    var currentTab by rememberSaveable {
        mutableStateOf(CaretakerViewUnitsBottomBarScreen.OCCUPIED_UNITS)
    }

    Box {
        UnitsScreen(
            currentTab = currentTab,
            onChangeTab = {
                currentTab = it
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnitsScreen(
    currentTab: CaretakerViewUnitsBottomBarScreen,
    onChangeTab: (tab: CaretakerViewUnitsBottomBarScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(currentTab) {
            CaretakerViewUnitsBottomBarScreen.OCCUPIED_UNITS -> {
                OccupiedUnitsScreenComposable(
                    modifier = Modifier
                        .weight(1f)
                )
            }
            CaretakerViewUnitsBottomBarScreen.UNOCCUPIED_UNITS -> {
                UnoccupiedUnitsScreenComposable(
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
        BottomBar(
            tabs = bottomBarMenuItems,
            currentTab = currentTab,
            onChangeTab = onChangeTab
        )
    }
}

@Composable
fun BottomBar(
    tabs: List<CaretakerUnitsBottomBarMenuItem>,
    currentTab: CaretakerViewUnitsBottomBarScreen,
    onChangeTab: (tab: CaretakerViewUnitsBottomBarScreen) -> Unit,
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
fun UnitsScreenPreview() {
    Tenant_careTheme {
        UnitsScreen(
            currentTab = CaretakerViewUnitsBottomBarScreen.OCCUPIED_UNITS,
            onChangeTab = {}
        )
    }
}