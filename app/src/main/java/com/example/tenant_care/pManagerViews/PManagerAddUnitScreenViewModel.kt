package com.example.tenant_care.pManagerViews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.NewPropertyRequestBody
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class UploadingStatus {
    INITIAL,
    UPLOADING,
    SUCCESS,
    FAIL
}

data class PManagerAddUnitScreenUiState(
    val unitDetails: UnitDetails = UnitDetails(),
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val uploadingStatus: UploadingStatus = UploadingStatus.INITIAL,
    val uploadingResponseMessage: String = "",
    val showSaveButton: Boolean = false
)

data class UnitDetails(
    val numOfRooms: Int = 0,
    val unitNameOrNumber: String = "",
    val unitDescription: String = "",
    val monthlyRent: String = ""
)

class PManagerAddUnitScreenViewModel(
    private val dsRepository: DSRepository,
    private val apiRepository: ApiRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerAddUnitScreenUiState())
    val uiState: StateFlow<PManagerAddUnitScreenUiState> = _uiState.asStateFlow()

    var unitDetails by mutableStateOf(UnitDetails())

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

    fun updateNumOfRooms(numOfRooms: Int) {
        unitDetails = unitDetails.copy(
            numOfRooms = numOfRooms
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun updateUnitNameOrNumber(unitNameOrNumber: String) {
        unitDetails = unitDetails.copy(
            unitNameOrNumber = unitNameOrNumber
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun updateUnitDescription(unitDescription: String) {
        unitDetails = unitDetails.copy(
            unitDescription = unitDescription
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun updateUnitRent(monthlyRent: String) {
        unitDetails = unitDetails.copy(
            monthlyRent = monthlyRent
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun uploadNewUnit() {
        _uiState.update {
            it.copy(
                uploadingStatus = UploadingStatus.UPLOADING
            )
        }
        val property = NewPropertyRequestBody(
            numberOfRooms = _uiState.value.unitDetails.numOfRooms,
            propertyNumberOrName = _uiState.value.unitDetails.unitNameOrNumber,
            propertyDescription = _uiState.value.unitDetails.unitDescription,
            monthlyRent = _uiState.value.unitDetails.monthlyRent.toDouble(),
            propertyManagerId = _uiState.value.userDetails.userId
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.addNewUnit(property)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            uploadingStatus = UploadingStatus.SUCCESS
                        )
                    }
                    Log.i("PROPERTY_UPLOADED", response.body().toString())
                } else {
                    _uiState.update {
                        it.copy(
                            uploadingStatus = UploadingStatus.FAIL,
                            uploadingResponseMessage = "Unit with a similar name/number already exists"
                        )
                    }
                    Log.e("PROPERTY_UPLOAD_UNSUCCESSFUL", response.toString())
                }
            } catch (e: Exception) {
                Log.e("PROPERTY_UPLOAD_UNSUCCESSFUL_EXCEPTION", e.message.toString())
                _uiState.update {
                    it.copy(
                        uploadingStatus = UploadingStatus.FAIL,
                        uploadingResponseMessage = "Failed to upload unit. Try again later."
                    )
                }
            }
        }
    }

    fun resetUploadingStatus() {
        _uiState.update {
            it.copy(
                uploadingStatus = UploadingStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
    }

    fun checkIfAddUnitFieldsAreEmpty(): Boolean {
        return unitDetails.numOfRooms != 0 && unitDetails.unitNameOrNumber.isNotEmpty() && unitDetails.unitDescription.isNotEmpty()
                && unitDetails.monthlyRent.isNotEmpty()
    }
}