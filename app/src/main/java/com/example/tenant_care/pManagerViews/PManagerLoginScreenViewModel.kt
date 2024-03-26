package com.example.tenant_care.pManagerViews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PManagerLoginScreenUiState(
    val pManagerLoginDetails: PManagerLoginDetails = PManagerLoginDetails(),
    val showLoginButton: Boolean = false,
)

data class PManagerLoginDetails(
    val phoneNumber: String = "",
    val password: String = "",
)

class PManagerLoginScreenViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerLoginScreenUiState())
    val uiState: StateFlow<PManagerLoginScreenUiState> = _uiState.asStateFlow()

    var pManagerLoginDetails by mutableStateOf(PManagerLoginDetails())

    fun updatePhoneNumber(phoneNumber: String) {
        pManagerLoginDetails = pManagerLoginDetails.copy(
            phoneNumber = phoneNumber
        )
        _uiState.update {
            it.copy(
                pManagerLoginDetails = pManagerLoginDetails,
                showLoginButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun updatePassword(password: String) {
        pManagerLoginDetails = pManagerLoginDetails.copy(
            password = password
        )
        _uiState.update {
            it.copy(
                pManagerLoginDetails = pManagerLoginDetails,
                showLoginButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun checkIfFieldsAreEmpty(): Boolean {
        return pManagerLoginDetails.phoneNumber.isNotEmpty() && pManagerLoginDetails.password.isNotEmpty()
    }
}