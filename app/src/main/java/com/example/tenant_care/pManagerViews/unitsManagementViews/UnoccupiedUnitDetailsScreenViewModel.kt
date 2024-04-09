package com.example.tenant_care.pManagerViews.unitsManagementViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.model.tenant.UnitAssignmentRequestBody
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val propertyUnitData = PropertyUnit(
    propertyUnitId = 0,
    numberOfRooms = 1,
    propertyNumberOrName = "",
    propertyDescription = "",
    monthlyRent = 0.0,
    propertyAddedAt = "",
    propertyAssignmentStatus = false,
    tenants = emptyList()
)

enum class AssignmentStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}
data class UnoccupiedUnitDetailsScreenUiState (
    val propertyUnit: PropertyUnit = propertyUnitData,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val propertyAddedAt: String = "",
    val tenantName: String = "",
    val nationalIdOrPassportNumber: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val roleId: Int = 3,
    val assignmentButtonEnabled: Boolean = false,
    val showUnitAssignmentScreen: Boolean = false,
    val assignmentStatus: AssignmentStatus = AssignmentStatus.INITIAL
)

@RequiresApi(Build.VERSION_CODES.O)
class UnoccupiedUnitDetailsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(UnoccupiedUnitDetailsScreenUiState())
    val uiState: StateFlow<UnoccupiedUnitDetailsScreenUiState> = _uiState.asStateFlow()
    private val propertyId: String? = savedStateHandle[UnOccupiedUnitDetailsComposableDestination.propertyId]

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadProperty() {
        viewModelScope.launch {
            try {
               val response = apiRepository.fetchPropertyByPropertyId(propertyId!!.toInt())
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            propertyUnit = response.body()?.data?.property!!,
                            propertyAddedAt = ReusableFunctions.formatDateTimeValue(response.body()?.data?.property?.propertyAddedAt!!)
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateTenantName(name: String) {
        _uiState.update {
            it.copy(
                tenantName = name,
                assignmentButtonEnabled = allFieldsFilled()
            )
        }
    }

    fun updateTenantIdOrPassport(idOrPass: String) {
        _uiState.update {
            it.copy(
                nationalIdOrPassportNumber = idOrPass,
                assignmentButtonEnabled = allFieldsFilled()
            )
        }
    }

    fun updateTenantPhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                assignmentButtonEnabled = allFieldsFilled()
            )
        }
    }

    fun updateTenantEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                assignmentButtonEnabled = allFieldsFilled()
            )
        }
    }

    fun updateTenantPassword(pass: String) {
        _uiState.update {
            it.copy(
                password = pass,
                assignmentButtonEnabled = allFieldsFilled()
            )
        }
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
    }

    fun assignUnitToTenant() {
        _uiState.update {
            it.copy(
                assignmentStatus = AssignmentStatus.LOADING,
                assignmentButtonEnabled = false
            )
        }
        val unitAssignmentRequestBody = UnitAssignmentRequestBody(
            fullName = _uiState.value.tenantName,
            nationalIdOrPassportNumber = _uiState.value.nationalIdOrPassportNumber,
            phoneNumber = _uiState.value.phoneNumber,
            email = _uiState.value.email,
            password = _uiState.value.password,
            roleId = _uiState.value.roleId,
            propertyUnitId = propertyId!!.toInt(),
            tenantAddedByPManagerId = _uiState.value.userDetails.userId
        )

        viewModelScope.launch {
            try {
               val response = apiRepository.assignPropertyUnit(unitAssignmentRequestBody)
               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           assignmentStatus = AssignmentStatus.SUCCESS
                       )
                   }
               } else {
                   _uiState.update {
                       it.copy(
                           assignmentStatus = AssignmentStatus.FAIL
                       )
                   }
               }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        assignmentStatus = AssignmentStatus.FAIL
                    )
                }
            }
        }
    }

    fun toggleScreen(){
        _uiState.update {
            it.copy(
                showUnitAssignmentScreen = !(_uiState.value.showUnitAssignmentScreen)
            )
        }
    }

    fun resetAssignmentStatus() {
        _uiState.update {
            it.copy(
                assignmentStatus = AssignmentStatus.INITIAL
            )
        }
    }

    fun allFieldsFilled(): Boolean {
        return _uiState.value.tenantName.isNotEmpty() &&
                _uiState.value.nationalIdOrPassportNumber.isNotEmpty() &&
                _uiState.value.phoneNumber.isNotEmpty() &&
                _uiState.value.email.isNotEmpty() &&
                _uiState.value.password.isNotEmpty()
    }

    init {
        loadUserDetails()
        loadProperty()
    }
}