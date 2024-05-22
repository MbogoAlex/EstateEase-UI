package com.example.tenant_care.ui.screens.tenantViews.rentPayment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.tenant.RentPayment
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FetchingStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}

data class PaymentHomeScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val rentPayments: List<RentPayment> = emptyList(),
    val roomName: String = "",
    val fetchingStatus: FetchingStatus = FetchingStatus.INITIAL
)
class PaymentHomeScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = PaymentHomeScreenUiState())
    val uiState: StateFlow<PaymentHomeScreenUiState> = _uiState.asStateFlow()
    fun loadStartUpData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {userDSDetails ->
                _uiState.update {
                    it.copy(
                        userDetails = userDSDetails.toUserDetails()
                    )
                }
            }

        }
    }
    fun getRentPayments(
        tenantId: Int,
        month: String?,
        year: String?,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ) {
        Log.i("FETCHING_ROWS", "FETCHING_ROWS")
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getRentPaymentRows(
                    tenantId = tenantId,
                    month = month,
                    year = year,
                    roomName = roomName,
                    rooms = rooms,
                    tenantName = tenantName,
                    rentPaymentStatus = rentPaymentStatus,
                    paidLate = paidLate,
                    tenantActive = tenantActive
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            rentPayments = response.body()?.data?.rentpayment!!,
                            roomName = response.body()?.data?.rentpayment!![0].propertyNumberOrName,
                            fetchingStatus = FetchingStatus.SUCCESS
                        )
                    }
                    Log.i("PAYMENT_ROW_FETCHED", response.toString())
                } else {
                    _uiState.update {
                        it.copy(
                            fetchingStatus = FetchingStatus.FAILURE
                        )
                    }
                    Log.e("FETCHING_PAYMENT_ROW_FAILED_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingStatus = FetchingStatus.FAILURE
                    )
                }
                Log.e("FETCHING_PAYMENT_ROW_FAILED_EXCEPTION", e.toString())
            }
        }
    }

    init {
        loadStartUpData()
        getRentPayments(
            tenantId = _uiState.value.userDetails.userId!!,
            month = null,
            year = null,
            roomName = null,
            rooms = null,
            tenantName = null,
            rentPaymentStatus = null,
            paidLate = null,
            tenantActive = null
        )
    }
}