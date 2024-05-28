package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ArchivingStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}

val occupiedPropertyUnitData = PropertyUnit(
    propertyUnitId = 0,
    numberOfRooms = 1,
    propertyNumberOrName = "",
    propertyDescription = "",
    monthlyRent = 0.0,
    propertyAddedAt = "",
    propertyAssignmentStatus = false,
    activeTenant = tenant,
    tenants = emptyList()
)


val tenantData = PropertyTenant(
    tenantId = 0,
    fullName = "",
    phoneNumber = "",
    email = "",
    tenantAddedAt = "",
    tenantActive = false
)
data class OccupiedUnitDetailsScreenUiState (
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val propertyUnit: PropertyUnit = occupiedPropertyUnitData,
    val tenant: PropertyTenant = tenantData,
    val tenantAddedAt: String = "",
    val propertyAddedAt: String = "",
    val archivingStatus: ArchivingStatus = ArchivingStatus.INITIAL
)

@RequiresApi(Build.VERSION_CODES.O)
class OccupiedUnitDetailsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = OccupiedUnitDetailsScreenUiState())
    val uiState: StateFlow<OccupiedUnitDetailsScreenUiState> = _uiState.asStateFlow()

    private val propertyId: String? = savedStateHandle[OccupiedUnitDetailsComposableDestination.propertyId]

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchProperty() {
        viewModelScope.launch {
            try {
               val response = apiRepository.fetchPropertyByPropertyId(propertyId!!.toInt())
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            propertyUnit = response.body()?.data?.property!!,
                            tenant = response.body()?.data?.property?.tenants!![0],
                            tenantAddedAt = ReusableFunctions.formatDateTimeValue(response.body()?.data?.property?.tenants!![0].tenantAddedAt),
                            propertyAddedAt = ReusableFunctions.formatDateTimeValue(response.body()?.data?.property?.propertyAddedAt!!)
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun archiveUnit() {
        _uiState.update {
            it.copy(
                archivingStatus = ArchivingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
               val response = apiRepository.archiveUnit(
                   propertyId = propertyId!!,
                   tenantId = _uiState.value.propertyUnit.tenants[0].tenantId.toString()
               )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            archivingStatus = ArchivingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            archivingStatus = ArchivingStatus.LOADING
                        )
                    }
                }
            }catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        archivingStatus = ArchivingStatus.FAIL
                    )
                }
            }
        }
    }

    fun resetArchivingStatus() {
        _uiState.update {
            it.copy(
                archivingStatus = ArchivingStatus.INITIAL
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

    init {
        loadUserDetails()
        fetchProperty()
    }
}