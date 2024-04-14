package com.example.tenant_care.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
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


enum class FetchingAllPaymentsStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}

val allPaymentsDataInit = RentPaymentDetailsResponseBodyData(
    rentpayment = emptyList()
)

data class AllTenantsPaymentsScreenUiState(
    val rentPaymentsData: RentPaymentDetailsResponseBodyData = allPaymentsDataInit,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val fetchingStatus: FetchingAllPaymentsStatus = FetchingAllPaymentsStatus.INITIAL
)

@RequiresApi(Build.VERSION_CODES.O)
class AllTenantsPaymentsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = AllTenantsPaymentsScreenUiState())
    val uiState: StateFlow<AllTenantsPaymentsScreenUiState> = _uiState.asStateFlow()

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
        paidLate: Boolean?
    ) {
        Log.i("FETCHING_WITH_TENANT_ID", tenantId.toString())
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingAllPaymentsStatus.FETCHING
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
                    paidLate = paidLate
                )
                if(response.isSuccessful) {

                    _uiState.update {
                        it.copy(
                            rentPaymentsData = response.body()?.data!!,
                            fetchingStatus = FetchingAllPaymentsStatus.SUCCESS
                        )
                    }

                    Log.i("PAYMENTS_FETCHED", "PAYMENTS FETCHED SUCCESSFULLY, ")

                } else {
                    _uiState.update {
                        it.copy(
                            fetchingStatus = FetchingAllPaymentsStatus.FAIL
                        )
                    }
                    Log.e("PAYMENTS_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingStatus = FetchingAllPaymentsStatus.FAIL
                    )
                }
                Log.e("PAYMENTS_FETCH_EXCEPTION", e.toString())
            }
        }
    }


    fun resetFetchingStatus() {
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingAllPaymentsStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
        fetchRentPaymentsData(
            month = LocalDateTime.now().month.toString(),
            year = LocalDateTime.now().year.toString(),
            tenantName = null,
            tenantId = null,
            rooms = null,
            roomName = null,
            rentPaymentStatus = null,
            paidLate = null
        )
    }
}