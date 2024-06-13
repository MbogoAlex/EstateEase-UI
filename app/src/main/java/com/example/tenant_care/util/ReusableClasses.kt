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

enum class ExecutionStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class PenaltyUpdateStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class ExpenseUpdateStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class PaymentStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class TenantViewSidebarMenuScreen {
    HOME_SCREEN,
    LOGIN_SCREEN
}

enum class CaretakerViewUnitsBottomBarScreen {
    OCCUPIED_UNITS,
    UNOCCUPIED_UNITS
}

enum class CaretakerViewMeterReadingBottomBarScreen {
    UPLOADED,
    NOT_UPLOADED
}
enum class CaretakerViewSidebarMenuScreen {
    UNITS_SCREEN,
    METER_READING_SCREEN,
    AMENITIES,
    LOGOUT
}

data class TenantSideBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: TenantViewSidebarMenuScreen,
    val color: Color
)

data class CaretakerUnitsBottomBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: CaretakerViewUnitsBottomBarScreen,
    val color: Color
)

data class CaretakerSideBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: CaretakerViewSidebarMenuScreen,
    val color: Color
)

data class CaretakerMeterReadingBottomBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: CaretakerViewMeterReadingBottomBarScreen,
    val color: Color
)

enum class PManagerViewSideBarMenuScreen {
    RENT_PAYMENTS_INFO,
    UNITS_MANAGEMENT,
    NOTIFICATIONS,
    CARETAKER_MANAGEMENT,
    AMENITIES,
    LOGOUT
}

data class PManagerViewSideBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: PManagerViewSideBarMenuScreen,
    val color: Color
)

enum class CaretakerManagementBottomBarScreen {
    CARETAKERS,
    ADD_CARETAKER
}

data class CaretakerManagementBottomBarMenuItem(
    val title: String,
    val icon: Int,
    val screen: CaretakerManagementBottomBarScreen,
    val color: Color

)