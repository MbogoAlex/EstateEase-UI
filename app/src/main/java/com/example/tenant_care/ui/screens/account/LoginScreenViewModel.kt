package com.example.tenant_care.ui.screens.account

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.model.caretaker.CaretakerLoginRequestBody
import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.tenant.LoginTenantRequestBody
import com.example.tenant_care.util.LoadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginScreenUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val role: String = "",
    val loginButtonEnabled: Boolean = false,
    val loginResponseMessage: String = "",
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
class LoginScreenViewModel(
    val apiRepository: ApiRepository,
    val dsRepository: DSRepository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(value = LoginScreenUiState())
    val uiState: StateFlow<LoginScreenUiState> = _uiState.asStateFlow()

    val phoneNumber: String? = savedStateHandle[LoginScreenDestination.phoneNumber]
    val password: String? = savedStateHandle[LoginScreenDestination.password]

    fun updateRole(role: String) {
        _uiState.update {
            it.copy(
                role = role
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun loginAsPropertyManager() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING,
            )
        }
        val pManagerRequestBody = PManagerRequestBody(
            phoneNumber = uiState.value.phoneNumber,
            password = uiState.value.password
        )

        Log.i("LOGIN_DETAILS", pManagerRequestBody.toString())

        // login attempt

        viewModelScope.launch {
            try {
                val response = apiRepository.loginPManager(pManagerRequestBody)
                if(response.isSuccessful) {

                    // save to datastore

                    val userDSDetails = UserDSDetails(
                        roleId = 1,
                        userId = response.body()?.data?.pmanager?.pmanagerId!!,
                        fullName = response.body()?.data?.pmanager?.fullName!!,
                        email = response.body()?.data?.pmanager?.email!!,
                        phoneNumber = response.body()?.data?.pmanager?.phoneNumber!!,
                        userAddedAt = response.body()?.data?.pmanager?.propertyManagerAddedAt!!,
                        room = "",
                        password = uiState.value.password,

                        )
                    dsRepository.saveUserDetails(userDSDetails)

                    // update UI

                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS,
                            loginResponseMessage = "Login success"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE,
                            loginResponseMessage = "Invalid credentials"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("LOGIN_FAILED", e.toString())
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE,
                        loginResponseMessage = e.message.toString()
                    )
                }
            }
        }
    }

    fun loginAsTenant() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        val tenant: LoginTenantRequestBody = LoginTenantRequestBody(
            tenantPhoneNumber = uiState.value.phoneNumber,
            tenantPassword = uiState.value.password
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
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loginResponseMessage = "Invalid credentials",
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("TENANT_LOGIN_FAIL_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loginResponseMessage = "Failed. Check you connection and try again",
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("TENANT_LOGIN_FAIL_EXCEPTION", e.toString())
            }
        }
    }

    fun loginAsCaretaker() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        val caretaker: CaretakerLoginRequestBody = CaretakerLoginRequestBody(
            phoneNumber = uiState.value.phoneNumber,
            password = uiState.value.password
        )
        viewModelScope.launch {
            try {
               val response = apiRepository.loginAsCaretaker(caretaker)
                if(response.isSuccessful) {
                    val userDSDetails: UserDSDetails = UserDSDetails(
                        roleId = 2,
                        userId = response.body()?.data?.caretaker?.caretakerId,
                        fullName = response.body()?.data?.caretaker?.fullName!!,
                        email = response.body()?.data?.caretaker?.email!!,
                        userAddedAt = response.body()?.data?.caretaker?.caretakerAddedAt!!,
                        phoneNumber = response.body()?.data?.caretaker?.phoneNumber!!,
                        room = "",
                        password = uiState.value.password
                    )
                    dsRepository.saveUserDetails(userDSDetails)
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                    Log.i("CARETAKER_LOGIN", "SUCCESS")
                } else {
                    _uiState.update {
                        it.copy(
                            loginResponseMessage = "Invalid credentials",
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("CARETAKER_LOGIN_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loginResponseMessage = "Failed. Check you connection and try again",
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("CARETAKER_LOGIN_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun checkIfAllFieldsAreFilled() {
        _uiState.update {
            it.copy(
                loginButtonEnabled = uiState.value.role.isNotEmpty() &&
                        uiState.value.phoneNumber.isNotEmpty() &&
                        uiState.value.password.isNotEmpty()
            )
        }

    }

    fun resetLoadingStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }
    init {
        if(phoneNumber != null && password != null) {
            _uiState.update {
                it.copy(
                    phoneNumber = phoneNumber,
                    password = password
                )
            }
        }
    }
}