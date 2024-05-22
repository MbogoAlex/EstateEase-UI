package com.example.tenant_care.ui.screens.pManagerTenantManagementViews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddTenantUiState(
    val tenantDetails: TenantDetails = TenantDetails(),
    val roomDetails: RoomDetails = RoomDetails(),
    val showSaveButton: Boolean = false
)

data class TenantDetails(
    val fullName: String = "",
    val natIdOrPassportNum: String = "",
    val phoneNumber: String = "",
    val email: String = "",
)

data class RoomDetails(
    val numOfRooms: Int = 0,
    val roomNumberOrName: String = ""
)
class AddTenantScreenViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(value = AddTenantUiState())
    val uiState: StateFlow<AddTenantUiState> = _uiState.asStateFlow()

    var tenantDetails by mutableStateOf(TenantDetails())
    var roomDetails by mutableStateOf(RoomDetails())

    fun updateFullName(fullName: String) {
        tenantDetails = tenantDetails.copy(
            fullName = fullName
        )
        _uiState.update {
            it.copy(
                tenantDetails = tenantDetails
            )
        }
    }

    fun updateNatIdOrPassportNum(natIdOrPassportNum: String) {
        tenantDetails = tenantDetails.copy(
            natIdOrPassportNum = natIdOrPassportNum
        )
        _uiState.update {
            it.copy(
                tenantDetails = tenantDetails,
                showSaveButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        tenantDetails = tenantDetails.copy(
            phoneNumber = phoneNumber
        )
        _uiState.update {
            it.copy(
                tenantDetails = tenantDetails,
                showSaveButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun updateEmail(email: String) {
        tenantDetails = tenantDetails.copy(
            email = email
        )
        _uiState.update {
            it.copy(
                tenantDetails = tenantDetails,
                showSaveButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun updateNumOfRooms(numOfRooms: Int) {
        roomDetails = roomDetails.copy(
            numOfRooms = numOfRooms
        )
        _uiState.update {
            it.copy(
                roomDetails = roomDetails,
                showSaveButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun updateRoomNumberOrName(roomNumberOrName: String) {
        roomDetails = roomDetails.copy(
            roomNumberOrName = roomNumberOrName
        )
        _uiState.update {
            it.copy(
                roomDetails = roomDetails,
                showSaveButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun checkIfFieldsAreEmpty(): Boolean {
        return tenantDetails.fullName.isNotEmpty() && tenantDetails.natIdOrPassportNum.isNotEmpty() && tenantDetails.email.isNotEmpty()
                && tenantDetails.phoneNumber.isNotEmpty()
                && roomDetails.numOfRooms != 0 && roomDetails.roomNumberOrName.isNotEmpty()
    }
}