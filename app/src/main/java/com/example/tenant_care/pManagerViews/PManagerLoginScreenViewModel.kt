package com.example.tenant_care.pManagerViews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.util.ReusableFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LOGIN_STATUS {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}
data class PManagerLoginScreenUiState(
    val pManagerLoginDetails: PManagerLoginDetails = PManagerLoginDetails(),
    val showLoginButton: Boolean = false,
    val loginStatus: LOGIN_STATUS = LOGIN_STATUS.INITIAL,
    val loginResponseMessage: String = ""
)

data class PManagerLoginDetails(
    val email: String = "",
    val password: String = "",
)

class PManagerLoginScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
):ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerLoginScreenUiState())
    val uiState: StateFlow<PManagerLoginScreenUiState> = _uiState.asStateFlow()

    var pManagerLoginDetails by mutableStateOf(PManagerLoginDetails())

    fun updateEmail(phoneNumber: String) {
        pManagerLoginDetails = pManagerLoginDetails.copy(
            email = phoneNumber
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
        return pManagerLoginDetails.email.isNotEmpty() && pManagerLoginDetails.password.isNotEmpty()
    }

    fun loginPManager() {
        if(ReusableFunctions.isValidEmail(_uiState.value.pManagerLoginDetails.email)) {
            _uiState.update {
                it.copy(
                    loginStatus = LOGIN_STATUS.LOADING,
                )
            }
            val pManagerRequestBody = PManagerRequestBody(
                email = _uiState.value.pManagerLoginDetails.email,
                password = _uiState.value.pManagerLoginDetails.password
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
                            userAddedAt = response.body()?.data?.pmanager?.propertyManagerAddedAt!!

                        )
                        dsRepository.saveUserDetails(userDSDetails)

                        // update UI

                        _uiState.update {
                            it.copy(
                                loginStatus = LOGIN_STATUS.SUCCESS,
                                loginResponseMessage = "Login success"
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                loginStatus = LOGIN_STATUS.FAIL,
                                loginResponseMessage = "Invalid credentials"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LOGIN_FAILED", e.toString())
                    _uiState.update {
                        it.copy(
                            loginStatus = LOGIN_STATUS.FAIL,
                            loginResponseMessage = e.message.toString()
                        )
                    }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    loginStatus = LOGIN_STATUS.FAIL,
                    loginResponseMessage = "Please enter a valid email"
                )
            }
        }

    }

    fun resetLoginStatus() {
        _uiState.update {
            it.copy(
                loginStatus = LOGIN_STATUS.INITIAL
            )
        }
    }
}