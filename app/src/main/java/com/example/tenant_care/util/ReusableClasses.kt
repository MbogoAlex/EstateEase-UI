package com.example.tenant_care.util

import androidx.compose.ui.graphics.Color

enum class DownloadingStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class LoadingStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class TenantViewSidebarMenuScreen {
    HOME_SCREEN,
    LOGIN_SCREEN
}

enum class CaretakerViewSidebarMenuScreen {
    UNITS_SCREEN,
    METER_READING_SCREEN,
    LOGOUT
}

data class TenantSideBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: TenantViewSidebarMenuScreen,
    val color: Color
)

data class CaretakerSideBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: CaretakerViewSidebarMenuScreen,
    val color: Color
)