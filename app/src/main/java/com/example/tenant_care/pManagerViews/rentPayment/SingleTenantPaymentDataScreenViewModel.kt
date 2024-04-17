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

enum class FetchingSingleTenantPaymentStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}

val paymentsDataInit = RentPaymentDetailsResponseBodyData(
    rentpayment = emptyList()
)

data class SingleTenantPaymentScreenUiState(
    val rentPaymentsData: RentPaymentDetailsResponseBodyData = paymentsDataInit,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val tenantId: String = "",
    val tenantAddedAt: String = "",
    val rentPaidOn: String = "",
    val rentPaymentDueOn: String = "",
    val penaltyActive: Boolean = false,
    val fetchingStatus: FetchingSingleTenantPaymentStatus = FetchingSingleTenantPaymentStatus.INITIAL
)
@RequiresApi(Build.VERSION_CODES.O)
class SingleTenantPaymentDataScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(SingleTenantPaymentScreenUiState())
    val uiState: StateFlow<SingleTenantPaymentScreenUiState> = _uiState.asStateFlow()
    private val tenantId: String? = savedStateHandle[SingleTenantPaymentDetailsComposableDestination.tenantId]
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
                fetchingStatus = FetchingSingleTenantPaymentStatus.FETCHING
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
                    val paidAt: String
                    if(response.body()?.data!!.rentpayment[0].paidAt != null) {

                        paidAt = ReusableFunctions.formatDateTimeValue(response.body()?.data!!.rentpayment[0].paidAt!!)
                        Log.i("FORMATTING_DATE", paidAt)
                    } else {
                        paidAt = ""
                    }
                    _uiState.update {
                        it.copy(
                            rentPaymentsData = response.body()?.data!!,
                            tenantAddedAt = ReusableFunctions.formatDateTimeValue(response.body()?.data!!.rentpayment[0].tenantAddedAt),
                            rentPaidOn = paidAt,
                            rentPaymentDueOn = response.body()?.data!!.rentpayment[0].dueDate,
                            penaltyActive = response.body()?.data!!.rentpayment[0].penaltyActive,
                            fetchingStatus = FetchingSingleTenantPaymentStatus.SUCCESS
                        )
                    }

                    Log.i("PAYMENTS_FETCHED", "PAYMENTS FETCHED SUCCESSFULLY, ${response.body()?.data?.rentpayment!![0]}")

                } else {
                    _uiState.update {
                        it.copy(
                            fetchingStatus = FetchingSingleTenantPaymentStatus.FAIL
                        )
                    }
                    Log.e("PAYMENTS_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingStatus = FetchingSingleTenantPaymentStatus.FAIL
                    )
                }
                Log.e("PAYMENTS_FETCH_EXCEPTION", e.toString())
            }
        }
    }


    fun resetFetchingStatus() {
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingSingleTenantPaymentStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
        fetchRentPaymentsData(
            month = LocalDateTime.now().month.toString(),
            year = LocalDateTime.now().year.toString(),
            tenantName = null,
            tenantId = tenantId!!.toInt(),
            rooms = null,
            roomName = null,
            rentPaymentStatus = null,
            paidLate = null
        )
    }
}