package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyRequestBody
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.network.ApiRepository
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
    val numOfRooms: String = "",
    val unitNameOrNumber: String = "",
    val unitDescription: String = "",
    val monthlyRent: String = "",
    val propertyUnit: PropertyUnit = occupiedPropertyUnitData,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val uploadingStatus: UploadingStatus = UploadingStatus.INITIAL,
    val uploadingResponseMessage: String = "",
    val propertyId: String? = null,
    val showSaveButton: Boolean = false
)


@RequiresApi(Build.VERSION_CODES.O)
class PManagerAddUnitScreenViewModel(
    private val dsRepository: DSRepository,
    private val apiRepository: ApiRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerAddUnitScreenUiState())
    val uiState: StateFlow<PManagerAddUnitScreenUiState> = _uiState.asStateFlow()

    private val propertyId: String? = savedStateHandle[PManagerAddUnitScreenDestination.unitId]
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails(),
                        propertyId = propertyId
                    )
                }
            }
        }
        if(uiState.value.propertyId != null) {
            fetchProperty()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchProperty() {
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchPropertyByPropertyId(propertyId!!.toInt())
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            propertyUnit = response.body()?.data?.property!!,
                            numOfRooms = response.body()?.data?.property?.rooms!!,
                            unitNameOrNumber = response.body()?.data?.property?.propertyNumberOrName!!,
                            unitDescription = response.body()?.data?.property?.propertyDescription!!,
                            monthlyRent = response.body()?.data?.property?.monthlyRent!!.toString()
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateNumOfRooms(numOfRooms: String) {
        _uiState.update {
            it.copy(
                numOfRooms = numOfRooms,
            )
        }
    }

    fun updateUnitNameOrNumber(unitNameOrNumber: String) {
        _uiState.update {
            it.copy(
                unitNameOrNumber = unitNameOrNumber,
            )
        }
    }

    fun updateUnitDescription(unitDescription: String) {
        _uiState.update {
            it.copy(
                unitDescription = unitDescription,
            )
        }
    }

    fun updateUnitRent(monthlyRent: String) {
        _uiState.update {
            it.copy(
                monthlyRent = monthlyRent,
            )
        }
    }

    fun uploadNewUnit() {
        _uiState.update {
            it.copy(
                uploadingStatus = UploadingStatus.UPLOADING
            )
        }
        val property = PropertyRequestBody(
            rooms = _uiState.value.numOfRooms,
            propertyNumberOrName = _uiState.value.unitNameOrNumber,
            propertyDescription = _uiState.value.unitDescription,
            monthlyRent = _uiState.value.monthlyRent.toDouble(),
            propertyManagerId = _uiState.value.userDetails.userId!!
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

    fun updateUnit() {
        _uiState.update {
            it.copy(
                uploadingStatus = UploadingStatus.UPLOADING
            )
        }
        val property = PropertyRequestBody(
            rooms = _uiState.value.numOfRooms,
            propertyNumberOrName = _uiState.value.unitNameOrNumber,
            propertyDescription = _uiState.value.unitDescription,
            monthlyRent = _uiState.value.monthlyRent.toDouble(),
            propertyManagerId = _uiState.value.userDetails.userId!!
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.updatePropertyUnit(property, uiState.value.propertyId!!.toInt())
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

    fun checkIfAllFieldsAreFilled() {
        _uiState.update {
            it.copy(
                showSaveButton = uiState.value.numOfRooms.isNotEmpty() && uiState.value.unitNameOrNumber.isNotEmpty() && uiState.value.unitDescription.isNotEmpty()
                        && uiState.value.monthlyRent.isNotEmpty()
            )
        }
    }
}