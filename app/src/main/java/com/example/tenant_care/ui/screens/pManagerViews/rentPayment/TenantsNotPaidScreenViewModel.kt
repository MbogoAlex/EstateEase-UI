package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.pManager.RentPaymentDetailsResponseBodyData
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

enum class FetchingTenantsNotPaidStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}

val unPaidRentPaymentsDataInit = RentPaymentDetailsResponseBodyData(
    rentpayment = emptyList()
)

data class TenantsNotPaidScreenUiState(
    val month: String = "",
    val year: String = "",
    val rentPaymentsData: RentPaymentDetailsResponseBodyData = unPaidRentPaymentsDataInit,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val fetchingStatus: FetchingTenantsNotPaidStatus = FetchingTenantsNotPaidStatus.INITIAL,
    val tenantName: String? = null,
    val selectedNumOfRooms: String? = null,
    val rooms: List<String> = emptyList(),
    val selectedUnitName: String? = null,
    val tenantActive: Boolean? = null,
    val activeTenantsSelected: Boolean = false,
    val inactiveTenantsSelected: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
class TenantsNotPaidScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = TenantsNotPaidScreenUiState())
    val uiStatus: StateFlow<TenantsNotPaidScreenUiState> = _uiState.asStateFlow()

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchRentPaymentsData(
        month: String,
        year: String,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        tenantId: Int?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ) {
        Log.i("FETCHING_WITH_TENANT_ID", tenantId.toString())
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingTenantsNotPaidStatus.FETCHING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchRentPaymentStatusForAllTenants(
                    month = month,
                    year = year,
                    roomName = roomName,
                    rooms = rooms,
                    tenantName = tenantName,
                    tenantId = tenantId,
                    rentPaymentStatus = rentPaymentStatus,
                    paidLate = paidLate,
                    tenantActive = tenantActive
                )
                if(response.isSuccessful) {
                    val rooms = mutableListOf<String>()
                    for(rentPayment in response.body()?.data!!.rentpayment) {
                        rooms.add(rentPayment.propertyNumberOrName)
                    }
                    _uiState.update {
                        it.copy(
                            rentPaymentsData = response.body()?.data!!,
                            rooms = rooms,
                            fetchingStatus = FetchingTenantsNotPaidStatus.SUCCESS
                        )
                    }

                    Log.i("PAYMENTS_FETCHED", "PAYMENTS FETCHED SUCCESSFULLY")

                } else {
                    _uiState.update {
                        it.copy(
                            fetchingStatus = FetchingTenantsNotPaidStatus.FAIL
                        )
                    }
                    Log.e("PAYMENTS_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingStatus = FetchingTenantsNotPaidStatus.FAIL
                    )
                }
                Log.e("PAYMENTS_FETCH_EXCEPTION", e.toString())
            }
        }
    }

    fun filterByTenantName(tenantName: String?) {
        Log.i("TENANT_NAME", tenantName!!)
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }
        _uiState.update {
            it.copy(
                tenantName = tenantName
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = false,
            paidLate = null,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun filterByNumberOfRooms(selectedNumOfRooms: String?) {
        Log.i("NoRooms", selectedNumOfRooms!!.toString())
        _uiState.update {
            it.copy(
                selectedNumOfRooms = selectedNumOfRooms
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = selectedNumOfRooms!!.toInt(),
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = false,
            paidLate = null,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun filterByUnitName(roomName: String?) {
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }
        _uiState.update {
            it.copy(
                selectedUnitName = roomName
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = roomName,
            rentPaymentStatus = false,
            paidLate = null,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun unfilterUnits() {
        _uiState.update {
            it.copy(
                tenantName = null,
                selectedNumOfRooms = null,
                selectedUnitName = null,
                tenantActive = null,
                activeTenantsSelected = false,
                inactiveTenantsSelected = false
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = null,
            roomName = null,
            rentPaymentStatus = false,
            paidLate = null,
            tenantActive = null
        )
    }

    fun filterByActiveTenants(tenantActive: Boolean) {
        _uiState.update {
            it.copy(
                tenantActive = tenantActive,
                activeTenantsSelected = tenantActive,
                inactiveTenantsSelected = !tenantActive
            )
        }
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }
        _uiState.update {
            it.copy(
                tenantActive = tenantActive
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = false,
            paidLate = null,
            tenantActive = tenantActive
        )
    }

    fun resetFetchingStatus() {
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingTenantsNotPaidStatus.INITIAL
            )
        }
    }

    fun setMonthAndYear(month: String, year: String) {
        _uiState.update {
            it.copy(
                month = month,
                year = year
            )
        }
        fetchRentPaymentsData(
            month = month,
            year = year,
            tenantName = null,
            tenantId = null,
            rooms = null,
            roomName = null,
            rentPaymentStatus = false,
            paidLate = null,
            tenantActive = null
        )
    }

    init {
        loadUserDetails()
    }
}