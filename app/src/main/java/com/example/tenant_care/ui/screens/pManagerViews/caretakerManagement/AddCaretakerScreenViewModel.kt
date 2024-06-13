package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.CaretakerRegistrationRequestBody
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddCaretakerScreenUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val natId: String = "",
    val salary: String = "",
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val addButtonEnabled: Boolean = false,
    val executionStatus: ExecutionStatus = ExecutionStatus.INITIAL,
)
class AddCaretakerScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = AddCaretakerScreenUiState())
    val uiState: StateFlow<AddCaretakerScreenUiState> = _uiState.asStateFlow()
    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect(){dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                name = name
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone
            )
        }
    }

    fun updateNatId(natId: String) {
        _uiState.update {
            it.copy(
                natId = natId
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

    fun updateSalary(salary: String) {
        _uiState.update {
            it.copy(
                salary = salary
            )
        }
    }

    fun addCaretaker() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }
        val caretakerRegistrationRequestBody = CaretakerRegistrationRequestBody(
            fullName = uiState.value.name,
            email = uiState.value.email,
            phoneNumber = uiState.value.phoneNumber,
            nationalIdOrPassportNumber = uiState.value.natId,
            salary = uiState.value.salary.toDouble(),
            password = uiState.value.password,
            roleId = 2,
            pManagerId = uiState.value.userDetails.userId!!
        )

        viewModelScope.launch {
            try {
                val response = apiRepository.registerCaretaker(caretakerRegistrationRequestBody)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.FAILURE
                        )
                    }
                    Log.e("ADD_CARETAKER_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("ADD_CARETAKER_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun resetExecutionStatus() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.INITIAL
            )
        }
    }

    fun checkIfRequiredFieldsAreFilled() {
        _uiState.update {
            it.copy(
                addButtonEnabled = uiState.value.name.isNotEmpty() &&
                        uiState.value.phoneNumber.isNotEmpty() &&
                        uiState.value.salary.isNotEmpty() &&
                        uiState.value.password.isNotEmpty()
            )
        }
    }
}