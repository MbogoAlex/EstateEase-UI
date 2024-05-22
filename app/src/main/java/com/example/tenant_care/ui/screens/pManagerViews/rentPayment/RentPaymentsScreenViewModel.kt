package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

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

enum class RentPaymentsScreen {
    ALL_TENANTS,
    PAID,
    NOT_PAID
}

enum class FetchingStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}

val rentPaymentsDataInit = RentPaymentDetailsResponseBodyData(
    rentpayment = emptyList()
)

data class RentPaymentsScreenUiState(
    val rentPaymentsData: RentPaymentDetailsResponseBodyData = rentPaymentsDataInit,
    val singleTenantPaymentData: RentPaymentDetailsResponseBodyData = rentPaymentsDataInit,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val rentPaymentsScreen: RentPaymentsScreen = RentPaymentsScreen.ALL_TENANTS,
    val tenantId: String = "",
    val tenantAddedAt: String = "",
    val rentPaidOn: String = "",
    val rentPaymentDueOn: String = "",
    val fetchingStatus: FetchingStatus = FetchingStatus.INITIAL
)

class RentPaymentsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = RentPaymentsScreenUiState())
    val uiStatus: StateFlow<RentPaymentsScreenUiState> = _uiState.asStateFlow()

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
                fetchingStatus = FetchingStatus.FETCHING
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
                    tenantActive = null
                )
                if(response.isSuccessful) {
                    if(tenantId != null) {
                        if(response.body()?.data!!.rentpayment[0].paidAt != null) {
                            _uiState.update {
                                it.copy(
                                    singleTenantPaymentData = response.body()?.data!!,
                                    tenantAddedAt = ReusableFunctions.formatDateTimeValue(response.body()?.data!!.rentpayment[0].tenantAddedAt),
                                    rentPaidOn = ReusableFunctions.formatDateTimeValue(response.body()?.data!!.rentpayment[0].paidAt!!),
                                    rentPaymentDueOn = response.body()?.data!!.rentpayment[0].dueDate,
                                    fetchingStatus = FetchingStatus.SUCCESS
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    singleTenantPaymentData = response.body()?.data!!,
                                    tenantAddedAt = ReusableFunctions.formatDateTimeValue(response.body()?.data!!.rentpayment[0].tenantAddedAt),
                                    rentPaidOn = ReusableFunctions.formatDateTimeValue(response.body()?.data!!.rentpayment[0].paidAt!!),
                                    fetchingStatus = FetchingStatus.SUCCESS
                                )
                            }
                        }

                    } else {
                        _uiState.update {
                            it.copy(
                                rentPaymentsData = response.body()?.data!!,
                                rentPaymentDueOn = response.body()?.data!!.rentpayment[0].dueDate,
                                fetchingStatus = FetchingStatus.SUCCESS
                            )
                        }
                    }

                    Log.i("PAYMENTS_FETCHED", "PAYMENTS FETCHED SUCCESSFULLY")

                } else {
                    _uiState.update {
                        it.copy(
                            fetchingStatus = FetchingStatus.FAIL
                        )
                    }
                    Log.e("PAYMENTS_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingStatus = FetchingStatus.FAIL
                    )
                }
                Log.e("PAYMENTS_FETCH_EXCEPTION", e.toString())
            }
        }
    }

    fun changeScreen(screen: RentPaymentsScreen) {
        _uiState.update {
            it.copy(
                rentPaymentsScreen = screen
            )
        }
    }

    fun resetFetchingStatus() {
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
        _uiState.update {
            it.copy(
                tenantId = tenantId.takeIf { tenantId != null } ?: ""
            )
        }
    }
}