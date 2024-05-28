package com.example.tenant_care.ui.screens.tenantViews.accountManagement

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.model.tenant.LoginTenantRequestBody
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
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = TenantLoginScreenUiState())
    val uiState: StateFlow<TenantLoginScreenUiState> = _uiState.asStateFlow()

    private val phoneNumber: String? = savedStateHandle[TenantLoginScreenDestination.phoneNumber]
    private val password: String? = savedStateHandle[TenantLoginScreenDestination.password]

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
        _uiState.update {
            it.copy(
                loginStatus = LoginStatus.LOADING
            )
        }
        val tenant: LoginTenantRequestBody = LoginTenantRequestBody(
            tenantPhoneNumber = _uiState.value.phoneNumber,
            tenantPassword = _uiState.value.password
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.loginTenant(tenant)
                if(response.isSuccessful) {
                    val userDSDetails: UserDSDetails = UserDSDetails(
                        roleId = 3,
                        userId = response.body()?.data?.tenant?.tenantId!!,
                        fullName = response.body()?.data?.tenant?.fullName!!,
                        email = response.body()?.data?.tenant?.email!!,
                        userAddedAt = response.body()?.data?.tenant?.tenantAddedAt!!,
                        phoneNumber = response.body()?.data?.tenant?.phoneNumber!!,
                        room = response.body()?.data?.tenant?.propertyUnit?.propertyNumberOrName!!,
                        password = uiState.value.password
                    )
                    dsRepository.saveUserDetails(userDSDetails)
                    Log.i("SAVE_TO_DS", userDSDetails.toString())
                    _uiState.update {
                        it.copy(
                            loginStatus = LoginStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loginStatus = LoginStatus.FAILURE
                        )
                    }
                    Log.e("TENANT_LOGIN_FAIL_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loginStatus = LoginStatus.FAILURE
                    )
                }
                Log.e("TENANT_LOGIN_FAIL_EXCEPTION", e.toString())
            }
        }
    }

    private fun initializeFields() {
        if(phoneNumber != null && password != null) {
            _uiState.update {
                it.copy(
                    password = password,
                    phoneNumber = phoneNumber,
                )
            }
        }
    }

    fun resetLoginStatus() {
        _uiState.update {
            it.copy(
                loginStatus = LoginStatus.INITIAL
            )
        }
    }

    fun checkIfAllFieldsFilled(): Boolean {
        return _uiState.value.roomName.isNotEmpty() &&
                _uiState.value.phoneNumber.isNotEmpty() &&
                _uiState.value.password.isNotEmpty()
    }

    init {
        initializeFields()
    }


}