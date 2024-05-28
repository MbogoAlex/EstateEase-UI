package com.example.tenant_care.ui.screens.caretakerViews

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tenant_care.R
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.MeterReadingHomeScreenComposable
import com.example.tenant_care.ui.screens.caretakerViews.units.UnitsScreenComposable
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.CaretakerSideBarMenuItem
import com.example.tenant_care.util.CaretakerViewSidebarMenuScreen
import kotlinx.coroutines.launch

val sideBarMenuItems = listOf<CaretakerSideBarMenuItem>(
    CaretakerSideBarMenuItem(
        title = "All houses",
        screen = CaretakerViewSidebarMenuScreen.UNITS_SCREEN,
        color = Color.Gray,
        icon = R.drawable.house
    ),
    CaretakerSideBarMenuItem(
        title = "Meter reading",
        screen = CaretakerViewSidebarMenuScreen.METER_READING_SCREEN,
        color = Color.Gray,
        icon = R.drawable.meter_reading
    ),
    CaretakerSideBarMenuItem(
        title = "Log out",
        screen = CaretakerViewSidebarMenuScreen.LOGOUT,
        color = Color.Gray,
        icon = R.drawable.logout
    ),
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CaretakerHomeScreenComposable(
    modifier: Modifier = Modifier
) {
    var currentScreen by rememberSaveable {
        mutableStateOf(CaretakerViewSidebarMenuScreen.UNITS_SCREEN)
    }

    Box {
        CaretakerHomeScreen(
            currentScreen = currentScreen,
            onChangeScreen = {
                currentScreen = it
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CaretakerHomeScreen(
    currentScreen: CaretakerViewSidebarMenuScreen,
    onChangeScreen: (screen: CaretakerViewSidebarMenuScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
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
                    for(menuItem in sideBarMenuItems) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
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
            when(currentScreen) {
                CaretakerViewSidebarMenuScreen.UNITS_SCREEN -> {
                    UnitsScreenComposable()
                }
                CaretakerViewSidebarMenuScreen.METER_READING_SCREEN -> {
                    MeterReadingHomeScreenComposable(
                        navigateToUploadMeterReadingScreen = {},
                        navigateToUpdateMeterReadingScreen = {},
                    )
                }
                CaretakerViewSidebarMenuScreen.LOGOUT -> {}
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CaretakerHomeScreenPreview() {
    Tenant_careTheme {
        CaretakerHomeScreen(
            currentScreen = CaretakerViewSidebarMenuScreen.UNITS_SCREEN,
            onChangeScreen = {}
        )
    }
}