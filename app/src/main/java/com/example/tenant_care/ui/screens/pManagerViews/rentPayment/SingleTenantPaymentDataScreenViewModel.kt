package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.message.MessageRequestBody
import com.example.tenant_care.model.pManager.RentPaymentDetailsResponseBodyData
import com.example.tenant_care.model.pManager.RentPaymentRowUpdateRequestBody
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.SendingMessageStatus
import com.example.tenant_care.util.waterMeterData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.abs

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
    val month: String? = "",
    val year: String? = "",
    val waterMeterDt: WaterMeterDt = waterMeterData,
    val waterUnitsConsumed: Double? = 0.0,
    val waterUnitsCurrentMonth: String = "",
    val waterUnitsPreviousMonth: String = "",
    val totalWaterPrice: Double? = 0.0,
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
    val sms: String = "",
    val rentPenaltySwitchResponse: String = "",
    val showPenaltyChangeDialog: Boolean = false,
    val singleTenantPenaltyToggleStatus: SingleTenantPenaltyToggleStatus = SingleTenantPenaltyToggleStatus.INITIAL,
    val fetchingStatus: FetchingSingleTenantPaymentStatus = FetchingSingleTenantPaymentStatus.INITIAL,
    val sendingMessageStatus: SendingMessageStatus = SendingMessageStatus.INITIAL
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
    private val month: String? = savedStateHandle[SingleTenantPaymentDetailsComposableDestination.month]
    private val year: String? = savedStateHandle[SingleTenantPaymentDetailsComposableDestination.year]
    private val roomName: String? = savedStateHandle[SingleTenantPaymentDetailsComposableDestination.roomName]
    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails->
                _uiState.update {
                    it.copy(
                        month = month,
                        year = year,
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
        Log.i("NAV_DETAILS: ", "TENANTID: $tenantId, ROOMNAME: $roomName MONTH: $month, YEAR: $year")
        getMeterReadings()
    }

    fun getMeterReadings() {
        Log.i("FETCHING", "$month, $year, ${uiState.value.userDetails.room}")
        viewModelScope.launch {
            try {
                val response = apiRepository.getMeterReadings(
                    month = month!!,
                    year = year!!,
                    meterReadingTaken = null,
                    tenantName = null,
                    propertyName = roomName!!,
                    role = null
                )
                if (response.isSuccessful) {
                    val meterData = response.body()?.data?.waterMeter!![0]
                    var waterPrice: Double? = 0.0
                    var unitsConsumed: Double? = 0.0
                    unitsConsumed = if(meterData.waterUnitsReading != null && meterData.previousWaterMeterData?.waterUnitsReading != null) {
                        abs(meterData.waterUnitsReading - meterData.previousWaterMeterData.waterUnitsReading)
                    } else if(meterData.waterUnitsReading != null && meterData.previousWaterMeterData?.waterUnitsReading == null) {
                        meterData.waterUnitsReading
                    } else {
                        null
                    }
                    if(unitsConsumed != 0.0 && unitsConsumed != null) {
                        waterPrice = waterMeterData.pricePerUnit?.times(unitsConsumed)
                    } else if(unitsConsumed == 0.0) {
                        waterPrice = 0.0
                    }
                    _uiState.update {
                        it.copy(
                            waterMeterDt = response.body()?.data?.waterMeter!![0],
                            waterUnitsConsumed = unitsConsumed,
                            waterUnitsCurrentMonth = response.body()?.data?.waterMeter!![0].month!!,
                            waterUnitsPreviousMonth = response.body()?.data?.waterMeter!![0].previousWaterMeterData?.month!!,
                            totalWaterPrice = waterPrice
                        )
                    }
                    Log.i("METER_READING_FETCH", "SUCCESS")
                } else {
                    Log.e("METER_READING_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                Log.e("METER_READING_FETCH_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchRentPaymentsData() {
        Log.i("FETCHING_WITH_TENANT_ID", tenantId.toString())
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingSingleTenantPaymentStatus.FETCHING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchRentPaymentStatusForAllTenants(
                    month = uiState.value.month!!,
                    year = uiState.value.year!!,
                    roomName = null,
                    rooms = null,
                    tenantName = null,
                    tenantId = tenantId!!.toInt(),
                    rentPaymentStatus = null,
                    paidLate = null,
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

    fun updateMessage(message: String) {
        _uiState.update {
            it.copy(
                sms = message
            )
        }
    }

    fun clearMessage() {
        _uiState.update {
            it.copy(
                sms = ""
            )
        }
    }

    fun sendSms() {
        _uiState.update {
            it.copy(
                sendingMessageStatus = SendingMessageStatus.LOADING
            )
        }
        val messageRequestBody = MessageRequestBody(
            message = uiState.value.sms
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.sendSms(messageRequestBody)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            sendingMessageStatus = SendingMessageStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            sendingMessageStatus = SendingMessageStatus.FAILURE
                        )
                    }
                    Log.e("SENDING_SMS_ERROR", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        sendingMessageStatus = SendingMessageStatus.FAILURE
                    )
                }
                Log.e("SENDING_SMS_EXCEPTION", e.toString())
            }
        }
    }

    fun resetSendingStatus() {
        _uiState.update {
            it.copy(
                sendingMessageStatus = SendingMessageStatus.INITIAL,
                sms = ""
            )
        }
    }

    init {
        loadUserDetails()
        fetchRentPaymentsData()
    }
}