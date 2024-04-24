package com.example.tenant_care.tenantViews.accountManagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}
data class TenantLoginScreenUiState(
    val roomName: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val buttonEnabled: Boolean = false,
    val loginStatus: LoginStatus = LoginStatus.INITIAL
)
class TenantLoginScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = TenantLoginScreenUiState())
    val uiState: StateFlow<TenantLoginScreenUiState> = _uiState.asStateFlow()

    fun updateRoomName(roomName: String) {
        _uiState.update {
            it.copy(
                roomName = roomName,
                buttonEnabled = checkIfAllFieldsFilled()
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                buttonEnabled = checkIfAllFieldsFilled()
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                buttonEnabled = checkIfAllFieldsFilled()
            )
        }
    }

    fun loginTenant() {
        viewModelScope.launch {
            try {
                val response = apiRepository
            } catch (e: Exception) {

            }
        }
    }

    fun checkIfAllFieldsFilled(): Boolean {
        return _uiState.value.roomName.isNotEmpty() &&
                _uiState.value.phoneNumber.isNotEmpty() &&
                _uiState.value.password.isNotEmpty()
    }
}