package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.pManager.RentPaymentDetailsResponseBodyData
import com.example.tenant_care.model.pManager.RentPaymentRowUpdateRequestBody
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

enum class SingleTenantPenaltyToggleStatus {
    INITIAL,
    LOADING,
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
    val penaltyPerDay: Double = 0.0,
    val newPenaltyPerDay: Double? = null,
    val rentPaymentRowId: Int = 0,
    val rentPenaltySwitchResponse: String = "",
    val showPenaltyChangeDialog: Boolean = false,
    val singleTenantPenaltyToggleStatus: SingleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.INITIAL,
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
                    paidLate = paidLate,
                    tenantActive = null
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
                            penaltyPerDay = response.body()?.data!!.rentpayment[0].penaltyPerDay,
                            rentPaymentRowId = response.body()?.data!!.rentpayment[0].rentPaymentTblId,
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

    fun activateLatePaymentPenalty(penaltyPerDay: String) {
        Log.i("ACTIVATING_STATUS", "Deactivating")
        _uiState.update {
            it.copy(
                singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.LOADING
            )
        }
        val penalty = ReusableFunctions.formatMoneyValue(penaltyPerDay.toDouble())
        val rentPaymentRowUpdateRequestBody = RentPaymentRowUpdateRequestBody(
            penaltyPerDay = penaltyPerDay.toDouble()
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.activatePenaltyForSingleTenant(
                    rentPayment = rentPaymentRowUpdateRequestBody,
                    rentPaymentTblId = _uiState.value.rentPaymentRowId
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            penaltyPerDay = penaltyPerDay.toDouble(),
                            penaltyActive = true,
                            singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.SUCCESS,
                            rentPenaltySwitchResponse = "Penalty activated. Penalty amount: $penalty"
                        )
                    }
                    Log.i("PENALTY_ACTIVATED", "Penalty activated")
                } else {
                    _uiState.update {
                        it.copy(
                            singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.FAIL,
                            rentPenaltySwitchResponse = "Failed to activate penalty. Try again later"
                        )
                    }
                    Log.e("PENALTY_ACTIVATED_ERROR", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.SUCCESS,
                        rentPenaltySwitchResponse = "Failed to activate penalty. Try again later"
                    )
                }
                Log.e("PENALTY_ACTIVATED_EXCEPTION", e.toString())
            }
        }
    }

    fun deActivateLatePaymentPenalty() {
        Log.i("DEACTIVATING_STATUS", "Deactivating")
        _uiState.update {
            it.copy(
                singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.deActivatePenaltyForSingleTenant(
                    rentPaymentTblId = _uiState.value.rentPaymentRowId
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            penaltyActive = false,
                            singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.SUCCESS,
                            rentPenaltySwitchResponse = "Penalty deactivated."
                        )
                    }
                    Log.i("PENALTY_DEACTIVATED", "Penalty deactivated")
                } else {
                    _uiState.update {
                        it.copy(
                            singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.FAIL,
                            rentPenaltySwitchResponse = "Failed to deactivate penalty"
                        )
                    }
                    Log.e("PENALTY_DEACTIVATED_ERROR", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.SUCCESS,
                        rentPenaltySwitchResponse = "Failed to deactivate penalty"
                    )
                }
                Log.e("PENALTY_DEACTIVATED_EXCEPTION", e.toString())
            }
        }
    }

    fun changePenaltyAmount(penaltyAmount: String) {
        _uiState.update {
            it.copy(
                newPenaltyPerDay = penaltyAmount.toDouble()
            )
        }
    }

    fun dismissPenaltyChange() {
        _uiState.update {
            it.copy(
                newPenaltyPerDay = null
            )
        }
    }
    fun resetFetchingStatus() {
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingSingleTenantPaymentStatus.INITIAL
            )
        }
    }

    fun togglePenaltyChangeDialog() {
        _uiState.update {
            it.copy(
                showPenaltyChangeDialog = !(_uiState.value.showPenaltyChangeDialog)
            )
        }
    }
    fun resetPenaltySwitchingStatus() {
        _uiState.update {
            it.copy(
                singleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.INITIAL
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